<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Invoice;
use App\Models\Payment;
use App\Enums\InvoiceStatus;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class InvoiceController extends Controller
{
    /**
     * Get the count of invoices grouped by status
     *
     * @return JsonResponse
     */
    public function countByStatus()
    {
        $counts = Invoice::select('status', DB::raw('count(*) as total'))
            ->groupBy('status')
            ->get()
            ->mapWithKeys(function ($item) {
                try {
                    $status = InvoiceStatus::fromStatus($item->status);
                    return [
                        $status->getDisplayValue() => [
                            'count' => $item->total,
                            'status' => $status->getStatus()
                        ]
                    ];
                } catch (\Exception $e) {
                    return [];
                }
            });

        // Ajouter les statuts qui n'ont pas de factures (count = 0)
        foreach (InvoiceStatus::values() as $status) {
            if (!$counts->has($status->getDisplayValue())) {
                $counts[$status->getDisplayValue()] = [
                    'count' => 0,
                    'status' => $status->getStatus()
                ];
            }
        }

        return response()->json([
            'status' => 'success',
            'data' => [
                'total' => Invoice::count(),
                'by_status' => $counts
            ]
        ]);
    }

    /**
     * Get invoices by status
     * 
     * @param string $status
     * @return JsonResponse
     */
    public function getInvoicesByStatus($status)
    {
        try {
            // Débogage
            \Log::info('Status reçu: ' . $status);
            
            $statusEnum = InvoiceStatus::fromStatus($status);
            
            $invoices = Invoice::where('status', $status)
                ->with(['client', 'payments'])
                ->get()
                ->map(function ($invoice) {
                    return [
                        'id' => $invoice->id,
                        'number' => $invoice->invoice_number,
                        'client_name' => $invoice->client->company_name,
                        'amount' => $this->calculateInvoiceAmount($invoice)/100,
                        'due_date' => $invoice->due_at,
                        'created_at' => $invoice->created_at,
                        'total_paid' => $this->calculateTotalPaid($invoice)/100,
                        'remaining' => ($this->calculateInvoiceAmount($invoice)/100) - ($this->calculateTotalPaid($invoice)/100)
                    ];
                });

            return response()->json([
                'status' => 'success',
                'data' => [
                    'status_display' => $statusEnum->getDisplayValue(),
                    'status_code' => $status,
                    'invoices' => $invoices
                ]
            ]);
        } catch (\Exception $e) {
            \Log::error('Erreur: ' . $e->getMessage());
            return response()->json([
                'status' => 'error',
                'message' => 'Invalid status: ' . $e->getMessage()
            ], 400);
        }
    }

    /**
     * Calculate total amount of invoice from invoice lines
     */
    private function calculateInvoiceAmount($invoice)
    {
        return $invoice->invoiceLines->sum(function($line) {
            return $line->price * $line->quantity;
        });
    }

    /**
     * Calculate total paid amount from payments
     */
    private function calculateTotalPaid($invoice)
    {
        return $invoice->payments->sum('amount');
    }

    /**
     * Get payments for a specific invoice
     * 
     * @param int $invoiceId
     * @return JsonResponse
     */
    public function getInvoicePayments($invoiceId)
    {
        $invoice = Invoice::with(['client', 'payments', 'invoiceLines'])
            ->findOrFail($invoiceId);

        return response()->json([
            'status' => 'success',
            'data' => [
                'invoice' => [
                    'id' => $invoice->id,
                    'number' => $invoice->invoice_number,
                    'client_name' => $invoice->client->company_name,
                    'amount' => $this->calculateInvoiceAmount($invoice)/100,
                    'due_date' => $invoice->due_at
                ],
                'payments' => $invoice->payments->map(function ($payment) {
                    return [
                        'id' => $payment->id,
                        'amount' => $payment->amount/100,
                        'date' => $payment->payment_date,
                        'source' => $payment->payment_source,
                        'description' => $payment->description
                    ];
                })
            ]
        ]);
    }

    /**
     * Get payment statistics for all invoices
     * 
     * @return JsonResponse
     */
    public function paymentStats()
    {
        // Récupérer uniquement les factures avec les statuts qui nous intéressent
        $invoices = Invoice::with(['payments', 'invoiceLines'])
            ->whereIn('status', [
                InvoiceStatus::partialPaid()->getStatus(),
                InvoiceStatus::paid()->getStatus(),
                InvoiceStatus::overpaid()->getStatus(),
                InvoiceStatus::unpaid()->getStatus()
            ])
            ->get();
        
        $totalPaid = 0;      // Montant total payé
        $totalUnpaid = 0;    // Montant total impayé
        
        foreach ($invoices as $invoice) {
            $invoiceAmount = $this->calculateInvoiceAmount($invoice)/100;
            $paidAmount = $this->calculateTotalPaid($invoice)/100;
            
            $totalPaid += $paidAmount;
            $totalUnpaid += ($invoiceAmount - $paidAmount);
        }

        $total = $totalPaid + $totalUnpaid;  // Différence entre payé et impayé

        return response()->json([
            'status' => 'success',
            'data' => [
                'total_paid' => $totalPaid,
                'total_unpaid' => $totalUnpaid,
                'total' => $total
            ]
        ]);
    }

    /**
     * Get invoices by section (total, paid, unpaid)
     * 
     * @param string $section
     * @return JsonResponse
     */
    public function getInvoicesBySection($section)
    {
        try {
            // Définir les statuts à inclure selon la section
            $statuses = [];
            switch($section) {
                case 'total':
                    $statuses = [
                        InvoiceStatus::paid()->getStatus(),
                        InvoiceStatus::unpaid()->getStatus(),
                        InvoiceStatus::partialPaid()->getStatus(),
                        InvoiceStatus::overpaid()->getStatus()
                    ];
                    break;
                case 'paid':
                    $statuses = [
                        InvoiceStatus::paid()->getStatus(),
                        InvoiceStatus::partialPaid()->getStatus(),
                        InvoiceStatus::overpaid()->getStatus()
                    ];
                    break;
                case 'unpaid':
                    $statuses = [
                        InvoiceStatus::unpaid()->getStatus(),
                        InvoiceStatus::partialPaid()->getStatus(),
                        InvoiceStatus::overpaid()->getStatus()
                    ];
                    break;
                default:
                    throw new \Exception("Invalid section: $section");
            }

            $invoices = Invoice::whereIn('status', $statuses)
                ->with(['client', 'payments'])
                ->get()
                ->map(function ($invoice) {
                    return [
                        'id' => $invoice->id,
                        'number' => $invoice->invoice_number,
                        'client_name' => $invoice->client->company_name,
                        'amount' => $this->calculateInvoiceAmount($invoice)/100,
                        'due_date' => $invoice->due_at,
                        'created_at' => $invoice->created_at,
                        'total_paid' => $this->calculateTotalPaid($invoice)/100,
                        'remaining' => ($this->calculateInvoiceAmount($invoice)/100) - ($this->calculateTotalPaid($invoice)/100)
                    ];
                });

            return response()->json([
                'status' => 'success',
                'data' => [
                    'section' => $section,
                    'invoices' => $invoices
                ]
            ]);

        } catch (\Exception $e) {
            \Log::error('Erreur: ' . $e->getMessage());
            return response()->json([
                'status' => 'error',
                'message' => $e->getMessage()
            ], 400);
        }
    }
} 