<?php

use Illuminate\Http\Request;
use App\Http\Controllers\Api\ApiAuthController;
use App\Http\Controllers\Api\InvoiceController;
use App\Http\Controllers\Api\PaymentController;
use App\Http\Controllers\Api\TaskStatsController;
use App\Http\Controllers\Api\OfferStatsController;
use App\Http\Controllers\Api\DiscountController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/


Route::post('/login', [ApiAuthController::class, 'login']);
Route::middleware('auth.api.token')->group(function () {
    Route::post('/logout', [ApiAuthController::class, 'logout']);
    Route::get('/user', [ApiAuthController::class, 'user']);
    Route::get('/invoices/count-by-status', [InvoiceController::class, 'countByStatus']);
    Route::get('/invoices/payment-stats', [InvoiceController::class, 'paymentStats']);
    Route::get('/invoices/status/{status}', [InvoiceController::class, 'getInvoicesByStatus']);
    Route::get('/invoices/{invoiceId}/payments', [InvoiceController::class, 'getInvoicePayments']);
    Route::get('/invoices/section/{section}', [InvoiceController::class, 'getInvoicesBySection']);

    // Routes pour la gestion des paiements
    Route::put('/payments/{paymentId}', [PaymentController::class, 'update']);
    Route::delete('/payments/{paymentId}', [PaymentController::class, 'destroy']);

    // Ajoutez cette route temporaire pour le débogage
    Route::get('/invoice-statuses', function() {
        return response()->json([
            'status' => 'success',
            'data' => array_map(function($status) {
                return [
                    'status' => $status->getStatus(),
                    'display' => $status->getDisplayValue()
                ];
            }, \App\Enums\InvoiceStatus::values())
        ]);
    });

    Route::get('tasks/stats', [TaskStatsController::class, 'byStatus']);
    Route::get('offers/stats', [OfferStatsController::class, 'byStatus']);
    // Routes pour la gestion des remises
    Route::get('/discounts/current', [DiscountController::class, 'getCurrentRate']);
    Route::post('/discounts', [DiscountController::class, 'store']);


});

// Route::group(['namespace' => 'App\Api\v1\Controllers'], function () {
//     Route::group(['middleware' => 'auth:api'], function () {
//         Route::get('users', ['uses' => 'UserController@index']);
//     });
// });