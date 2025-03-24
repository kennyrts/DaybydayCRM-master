<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Discount extends Model
{
    protected $fillable = ['rate'];

    /**
     * Get the current global discount rate
     *
     * @return float
     */
    public static function getCurrentRate()
    {
        $latestDiscount = self::latest('created_at')->first();
        return $latestDiscount ? $latestDiscount->rate : 0.00;
    }
} 