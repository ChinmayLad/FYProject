<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;

use Socialite;

class AuthController extends Controller
{
    /**
     * Redirect the user to the GitHub authentication page.
     *
     * @return Response
     */
    public function redirectToProvider()
    {
        return Socialite::driver('google')->redirect();
    }

    /**
     * Obtain the user information from GitHub.
     *
     * @return Response
     */
    public function handleProviderCallback()
    {
        $user = Socialite::driver('google')->user();
        if($user!=null){
	        $data = array(
			    'name'  => $user->getName(),
			    'email'   => $user->getEmail(),
			    'nickname' => $user->getNickname(),
			    'id'=> $user->getId(),
			    'avatar' =>$user->getAvatar()
			);

			
	        if(\DB::table('user_table')->where('email','=',$user->getEmail())->exists()){}
	        else{
				\DB::table('user_table')->insertGetId(['g_id'=>$user->getId(),'name'=>$user->getName(),'email' => $user->getEmail()]);
			}
	        /*
	       	$users = new User();
	        $users->name = $user->getName();
	        $users->email = $user->getName();
	       	$user->save();
	       	*/
	      	//$dat = $data->first();
	      	
	      	session()->put('current_user',$data);
       }
       return  redirect()->route('dashboard');
    }


    public function logout(){
    	session()->forget('current_user');
    	return redirect()->route('/');
    }
}
