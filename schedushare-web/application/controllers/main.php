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
        
        $data['title'] = "Sched-u-Share";
        
        //load view
        $this->load->view("templates/header",$data);
        $this->load->view("main_page",$data);
        $this->load->view("templates/footer",$data);
    }
}

?>
