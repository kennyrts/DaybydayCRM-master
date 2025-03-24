<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Discount;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;

class DiscountController extends Controller
{
    /**
     * Get the current discount rate
     *
     * @return JsonResponse
     */
    public function getCurrentRate(): JsonResponse
    {
        $rate = Discount::getCurrentRate();
        
        return response()->json([
            'status' => 'success',
            'data' => [
                'rate' => $rate
            ]
        ]);
    }

    /**
     * Add a new discount rate
     *
     * @param Request $request
     * @return JsonResponse
     */
    public function store(Request $request): JsonResponse
    {
        try {
            // Valider la requête
            $request->validate([
                'rate' => 'required|numeric|min:0|max:100'
            ]);

            // Créer le nouveau taux de remise
            $discount = Discount::create([
                'rate' => $request->rate
            ]);

            return response()->json([
                'status' => 'success',
                'message' => 'Discount rate added successfully',
                'data' => [
                    'id' => $discount->id,
                    'rate' => $discount->rate,
                    'created_at' => $discount->created_at
                ]
            ], 201);
        } catch (\Exception $e) {
            return response()->json([
                'status' => 'error',
                'message' => 'Failed to add discount rate: ' . $e->getMessage()
            ], 500);
        }
    }
} 