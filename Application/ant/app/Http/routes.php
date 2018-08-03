<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the controller to call when that URI is requested.
|
*/

Route::get('/', ['as'=>'/' , function () {
	if(session()->has('current_user')){
		return redirect()->route('dashboard');
	}
    return view('welcome');
}]);




Route::get('dashboard', ['as'=>'dashboard',function(){
	if(session()->has('current_user')){
		$data = session('current_user');
		if(session()->has('devices')){
			session()->forget('devices');
		}
		if(\DB::table('devices')->where('email','=',$data['email'])->exists()){
			$devices = \DB::table('devices')->select('name','uuid')->where('email','=',$data['email'])->get();
			session()->put('devices',$devices);
		}

		return view('user')->with($data);
	}
	$error = "You have to login using Google first!!!";
	return redirect()->route('/')->with('error',$error);
}]);


Route::get('insert/{uuid}',function($uuid){
	if(session()->has('current_user')){
		$user = session('current_user');
		\DB::table('devices')->insertGetId(['uuid'=>$uuid,'name'=>'Wallet','email' => $user['email']]);
	}
	return redirect()->route('/');
});







Route::get('delete/{uuid}',function($uuid){
	if(session()->has('current_user')){
		$user = session('current_user');
		\DB::table('devices')->where('uuid','=',$uuid)->delete();
		$data = \DB::table('devices')->where('email','=',$user['email']);
	}
	return redirect()->route('/');
});



Route::get('user',function(){
	$users = \DB::table('user_table')->select('name')->get();

	return $users;
});






Route::get('devices',function(){
		$devices = \DB::table('devices')->select('name','uuid','email')->get();
		return $devices;
});


Route::get('maps', 'MapController@buildMap');
Route::get('logout',['as'=>'logout', 'uses'=>'AuthController@logout']);
Route::get('google', 'AuthController@redirectToProvider');
Route::get('gAuth', 'AuthController@handleProviderCallback');