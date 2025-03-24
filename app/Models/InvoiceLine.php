<?php
namespace App\Models;

use App\Repositories\Money\Money;
use App\Repositories\Money\MoneyConverter;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

class InvoiceLine extends Model
{
    use SoftDeletes;

    protected $fillable = [
        'external_id',
        'type',
        'quantity',
        'title',
        'comment',
        'price',
        'invoice_id',
        'product_id',
        'offer_id',
    ];

    /**
     * The "booted" method of the model.
     *
     * @return void
     */
    protected static function booted()
    {
        static::creating(function ($invoiceLine) {
            // N'appliquer la remise que pour les nouvelles lignes qui ne sont pas copiées d'une offre
            if (!$invoiceLine->invoice_id && !$invoiceLine->wasRecentlyCreated) {
                $discountRate = Discount::getCurrentRate();
                $originalPrice = $invoiceLine->price;
                $invoiceLine->price = $originalPrice * (1 - ($discountRate / 100));
            }
        });
    }

    /**
     * Get the route key for the model.
     *
     * @return string
     */
    public function getRouteKeyName()
    {
        return 'external_id';
    }

    public function tasks()
    {
        return $this->belongsTo(Task::class);
    }

    public function invoice()
    {
        return $this->belongsTo(Invoice::class);
    }

    public function task()
    {
        return $this->invoice->task;
    }

    public function getTotalValueAttribute()
    {
        return $this->quantity * $this->price;
    }

    public function product()
    {
        return $this->belongsTo(Product::class);
    }
    
    public function getTotalValueConvertedAttribute()
    {
        $money = new Money($this->total_value);
        return app(MoneyConverter::class, ['money' => $money])->format();
    }
    
    public function getPriceConvertedAttribute()
    {
        $money = new Money($this->price);
        return app(MoneyConverter::class, ['money' => $money])->format();
    }

    public function offer()
    {
        return $this->belongsTo(Offer::class);
    }
}
