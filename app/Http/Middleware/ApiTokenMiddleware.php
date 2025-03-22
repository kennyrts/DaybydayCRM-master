<?php

namespace App\Http\Middleware;

use Closure;
use App\Models\User;

class ApiTokenMiddleware
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        $token = $request->header('Authorization');
        
        if (!$token) {
            return response()->json([
                'status' => 'error',
                'message' => 'Token d\'authentification manquant'
            ], 401);
        }
        
        // Supprimer le préfixe "Bearer " si présent
        $token = str_replace('Bearer ', '', $token);
        
        // Vérifier si le token existe dans la base de données
        $user = User::where('api_token', $token)->first();
        
        if (!$user) {
            return response()->json([
                'status' => 'error',
                'message' => 'Token d\'authentification invalide'
            ], 401);
        }
        
        // Ajouter l'utilisateur à la requête
        $request->user = $user;
        
        return $next($request);
    }
} 