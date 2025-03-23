<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Payment;
use App\Models\Invoice;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use App\Enums\InvoiceStatus;
use App\Services\Invoice\GenerateInvoiceStatus;

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
        app(GenerateInvoiceStatus::class, ['invoice' => $payment->invoice])->createStatus();

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
        app(GenerateInvoiceStatus::class, ['invoice' => $invoice])->createStatus();

        return response()->json([
            'status' => 'success',
            'message' => 'Payment deleted successfully',
            'invoice_status' => $invoice->status
        ]);
    }
} 