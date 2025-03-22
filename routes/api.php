<?php

use Illuminate\Http\Request;

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

// Route simple pour tester l'API
Route::get('/test', function() {
    return response()->json([
        'message' => 'Ca marche!'
    ]);
});

// Route::group(['namespace' => 'App\Api\v1\Controllers'], function () {
//     Route::group(['middleware' => 'auth:api'], function () {
//         Route::get('users', ['uses' => 'UserController@index']);
//     });
// });