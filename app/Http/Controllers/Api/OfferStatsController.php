<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Models\Offer;
use App\Enums\OfferStatus;
use Illuminate\Http\JsonResponse;

class OfferStatsController extends Controller
{
    public function byStatus(): JsonResponse
    {
        $stats = [];
        
        // Récupérer tous les statuts possibles
        foreach (OfferStatus::values() as $status) {
            $count = Offer::where('status', $status->getStatus())->count();
            $stats[$status->getDisplayValue()] = $count;
        }
        
        // Ajouter le total
        $stats['Total'] = Offer::count();
        
        return response()->json([
            'status' => 'success',
            'data' => $stats
        ]);
    }
} 