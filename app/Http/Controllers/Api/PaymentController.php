<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Payment;
use App\Models\Invoice;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use App\Enums\InvoiceStatus;

class PaymentController extends Controller
{
    /**
     * Update payment
     * 
     * @param Request $request
     * @param int $paymentId
     * @return JsonResponse
     */
    public function update(Request $request, $paymentId)
    {
        $payment = Payment::findOrFail($paymentId);
        
        $request->validate([
            'amount' => 'required|numeric|min:0'
        ]);

        $payment->update(['amount' => $request->amount]);

        // Recalculer le statut de la facture
        $this->updateInvoiceStatus($payment->invoice);

        return response()->json([
            'status' => 'success',
            'data' => [
                'payment' => $payment,
                'invoice_status' => $payment->invoice->status
            ]
        ]);
    }

    /**
     * Delete payment
     * 
     * @param int $paymentId
     * @return JsonResponse
     */
    public function destroy($paymentId)
    {
        $payment = Payment::findOrFail($paymentId);
        $invoice = $payment->invoice;
        
        $payment->delete();

        // Recalculer le statut de la facture
        $this->updateInvoiceStatus($invoice);

        return response()->json([
            'status' => 'success',
            'message' => 'Payment deleted successfully',
            'invoice_status' => $invoice->status
        ]);
    }

    /**
     * Update invoice status based on payments
     */
    private function updateInvoiceStatus(Invoice $invoice)
    {
        $totalPaid = $invoice->payments->sum('amount');
        
        if ($totalPaid == 0) {
            $invoice->status = InvoiceStatus::unpaid()->getStatus();
        } elseif ($totalPaid < $invoice->amount) {
            $invoice->status = InvoiceStatus::partialPaid()->getStatus();
        } elseif ($totalPaid == $invoice->amount) {
            $invoice->status = InvoiceStatus::paid()->getStatus();
        } else {
            $invoice->status = InvoiceStatus::overpaid()->getStatus();
        }

        $invoice->save();
    }
} 