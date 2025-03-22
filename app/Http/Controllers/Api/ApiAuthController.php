<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Str;
use App\Models\User;

class ApiAuthController extends Controller
{
    /**
     * Authentifie un utilisateur et retourne un token simple
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function login(Request $request)
    {
        

        $credentials = $request->only('email', 'password');

        if (Auth::attempt($credentials)) {
            $user = Auth::user();
            
            // Générer un token simple (dans un vrai système, utilisez quelque chose de plus sécurisé)
            $token = Str::random(60);
            
            // Stocker le token dans la base de données
            $user->api_token = $token;
            $user->save();
            
            return response()->json([
                'status' => 'success',
                'message' => 'Authentification réussie',
                'user' => [
                    'id' => $user->id,
                    'name' => $user->name,
                    'email' => $user->email,
                ],
                'token' => $token
            ]);
        }
        
        return response()->json([
            'status' => 'error',
            'message' => 'Identifiants invalides'
        ], 401);
    }
    
    /**
     * Déconnecte l'utilisateur en invalidant son token
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function logout(Request $request)
    {
        $token = $request->header('Authorization');
        
        if ($token) {
            // Supprimer le préfixe "Bearer " si présent
            $token = str_replace('Bearer ', '', $token);
            
            // Trouver l'utilisateur par token
            $user = User::where('api_token', $token)->first();
            
            if ($user) {
                // Invalider le token
                $user->api_token = null;
                $user->save();
                
                return response()->json([
                    'status' => 'success',
                    'message' => 'Déconnexion réussie'
                ]);
            }
        }
        
        return response()->json([
            'status' => 'error',
            'message' => 'Token invalide ou utilisateur non connecté'
        ], 401);
    }
    
    /**
     * Récupère les informations de l'utilisateur connecté
     *
     * @param Request $request
     * @return \Illuminate\Http\JsonResponse
     */
    public function user(Request $request)
    {
        $token = $request->header('Authorization');
        
        if ($token) {
            // Supprimer le préfixe "Bearer " si présent
            $token = str_replace('Bearer ', '', $token);
            
            // Trouver l'utilisateur par token
            $user = User::where('api_token', $token)->first();
            
            if ($user) {
                return response()->json([
                    'status' => 'success',
                    'user' => [
                        'id' => $user->id,
                        'name' => $user->name,
                        'email' => $user->email,
                    ]
                ]);
            }
        }
        
        return response()->json([
            'status' => 'error',
            'message' => 'Token invalide ou utilisateur non connecté'
        ], 401);
    }

    public function test2(Request $request) {
        $data = $request->json()->all();
        $email = $data['email'];
        $password = $data['password'];
        return response()->json([
            'message' => $email
        ]);
    }
} 