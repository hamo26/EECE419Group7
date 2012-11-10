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
class Main extends CI_Controller {
    //put your code here   
    
    function index(){
        
        //load classes
        $this->load->helper('url');
        
        $app_id = "244995238959505";
        $app_secret = "d47346c1d60a1b60df939ef5464b28a2";
        $app_token_url = "https://graph.facebook.com/oauth/access_token?"
            . "client_id=" . $app_id
            . "&client_secret=" . $app_secret 
            . "&grant_type=client_credentials";

            $response = file_get_contents($app_token_url);
            $params = null;
        parse_str($response, $params);

        echo("This app's access token is: " . $params['access_token']);
        
        
        $data['title'] = "Sched-u-Share";
        
        //load view
        $this->load->view("templates/header",$data);
        $this->load->view("main_page",$data);
        $this->load->view("templates/footer",$data);
    }
}

?>
