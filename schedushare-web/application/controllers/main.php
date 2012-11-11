<?php

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of main
 *
 * @author Cat
 */
require BASEPATH.'../application/src/facebook.php';

class Main extends CI_Controller {
    //put your code here   
    
    function index(){
        
        //load classes
        $this->load->helper('url');
        
        //$this->load->view("example");        

        $facebook = new Facebook(array(
            'appId'  => '244995238959505',
            'secret' => 'd47346c1d60a1b60df939ef5464b28a2',
        ));

                // Get User ID
        $user = $facebook->getUser();
        $friends = Array();
        
        if ($user) {
            try {
                    // Proceed knowing you have a logged in user who's authenticated.
                    $user_profile = $facebook->api('/me');
                    $friends = $facebook->api('/me/friends');
            } catch (FacebookApiException $e) {
                    error_log($e);
                    $user = null;
            }
        }
        
        $data['title'] = "Sched-u-Share";
        $data['user'] = $user;
        $data['facebook'] = $facebook;
        $data['friends'] = $friends;        
        
        if(!$user){
            $this->load->view("templates/header",$data);
            $this->load->view("login",$data);
            //$this->load->view("main_page",$data);
            $this->load->view("templates/footer",$data);  
        }else{        
            //load view
            $this->load->view("templates/header",$data);
            $this->load->view("main_page",$data);
            $this->load->view("templates/footer",$data);
        } 
    }
    
    function findfriend(){
        
        $name = $this->input->get('name',TRUE);
        //$friends = $this->input->get('friends',TRUE);
        $facebook = new Facebook(array(
            'appId'  => '244995238959505',
            'secret' => 'd47346c1d60a1b60df939ef5464b28a2',
        ));
        
        //var_dump($facebook);
        
        $friends = $facebook->api('/me/friends');
        
        //var_dump($friends);
        
        foreach ($friends["data"] as $value){
            if($name==$value["name"]){
                echo json_encode($value);
            }
        }
    }
}

?>
