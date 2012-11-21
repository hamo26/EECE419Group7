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

//define('APP_ID','244995238959505');
//define('APP_SECRET','d47346c1d60a1b60df939ef5464b28a2');

class Main extends CI_Controller {
    //put your code here   
    
    function index(){
        
        //echo "in index<br>";
        
        //load classes
        $this->load->helper('url');

        // Get User Token
        $app_id = '244995238959505';
        $app_secret = 'd47346c1d60a1b60df939ef5464b28a2';
        
        $facebook = $this->getfacebooktoken($app_id, $app_secret); //get FB token
        
        $user = $facebook->getUser(); //get user token
        
        if(!$user){
            //Test Code
            $user = 0;
            $friends = Array("data"=>Array(Array("name"=>"Ash","id"=>"1")));
        }else{
            //$user_profile = $this->getfacebookrequest($facebook, $user, '/me');
            $friends = $this->getfacebookrequest($facebook, $user, '/me/friends');
        }
        
        $data['title'] = "Sched-u-Share";
        $data['user'] = $user;
        $data['facebook'] = $facebook; //needed for login page
        $data['friends'] = $friends;        
        
        if(!$user){
            $this->load->view("templates/header",$data);
            //$this->load->view("login",$data);
            $this->load->view("main_page",$data); //for DEBUG, REMEMBER TO PUT BACK LOGIN!
            $this->load->view("templates/footer",$data);  
        }else{        
            //load view
            $this->load->view("templates/header",$data);
            $this->load->view("main_page",$data);
            $this->load->view("templates/footer",$data);
        } 
    }
    
    /***********************************************************/
    //GETTERS
    function findfriend(){
        
        $name = $this->input->get('name',TRUE);
        //$friends = $this->input->get('friends',TRUE);
        $facebook = new Facebook(array(
            'appId'  => '244995238959505',
            'secret' => 'd47346c1d60a1b60df939ef5464b28a2',
        ));
        
        $user = $facebook->getUser(); //get user token
        
        $friends = $facebook->api('/me/friends');
        
        //var_dump($friends);
        
        foreach ($friends["data"] as $value){
            if(strtoupper($name)==strtoupper($value["name"])){
                echo json_encode($value);
            }
        }
    }
    
    function fetchevents(){
        
        $events = Array();
        
        $events[0]['day'] = 0;
        $events[0]['start-time'] = "08:00";
        $events[0]['end-time'] = "09:00";
        $events[0]['description'] = "no";
        
        $events[1]['day'] = 2;
        $events[1]['start-time'] = "08:00";
        $events[1]['end-time'] = "09:00";
        $events[1]['description'] = "way";
        
        echo json_encode($events);
        
    }
    
    function finduser(){
        
        $app_id = '244995238959505';
        $app_secret = 'd47346c1d60a1b60df939ef5464b28a2';
        
        $facebook = $this->getfacebooktoken($app_id, $app_secret); //get FB token
        
        $user = $facebook->getUser(); //get user token    
        $user_profile = $this->getfacebookrequest($facebook, $user, '/me');
        
        if(!$user){
            //for offline testing
            $id = 2;
            $email = "a@b.com";
            $name = "John Doe";
            
            $response = $this->get_request(RESTPATH.'/schedushare/users/'.$id,2);

            if(strpos($response,"schedushare-error")){
                if(strpos($response,"User id does not exist")){
                    //user not found, make new user
                    echo ''; //return empty array
                }
            }else{
                echo $response;
            }
        }else{        
            //load view
            //var_dump($user_profile);
            $response = $this->get_request(RESTPATH.'/schedushare/user/'.$user_profile["email"],2);
            echo $response;
        } 

    }
    
    function getschedules(){
        
        $user_id = $this->input->get("userid");
        
        $response = $this->get_request(RESTPATH.'/schedushare/schedules/users/'.$user_id,2);
        
        echo $response;
        
    }
    
    function gettimeblocks(){
        
        $schedule_id = $this->input->get("scheduleid");
        
        $response = $this->get_request(RESTPATH.'/schedushare/timeblocks/schedules/'.$schedule_id,2);
        
    }
    
    function getactiveschedule(){
        $user_id = $this->input->get("userid");
        
        $response = $this->get_request(RESTPATH.'/schedushare/schedules/active/'.$user_id,2);
        
        echo $response;
    }
    /***********************************************************/
    //SETTERS
    function registeruser(){
        
        $app_id = '244995238959505';
        $app_secret = 'd47346c1d60a1b60df939ef5464b28a2';
        
        $facebook = $this->getfacebooktoken($app_id, $app_secret); //get FB token
        
        $user = $facebook->getUser(); //get user token   
        
        if(!$user){
            //for offline testing
            $email = "a@b.com";
            $name = "John Doe";
            
            $arg = json_encode(Array("email"=>$email,"name"=>$name));
            
            $response = $this->post_request(RESTPATH.'/schedushare/register/user', $arg,2);
            echo $response;

        }else{        
            //load view
            //var_dump($user_profile);

        } 
    }
    
    function create_event(){
        $app_id = '244995238959505';
        $app_secret = 'd47346c1d60a1b60df939ef5464b28a2';
        
        $facebook = $this->getfacebooktoken($app_id, $app_secret); //get FB token
        
        $user = $facebook->getUser(); //get user token   
        
        if(!$user){
            //for offline testing

        }else{        
            //load view
            //$event_name = $this->input->get("name");
            $event_name = "My Event";
            $start_time = date("today");
            $location = "home";
            
            $event_param = array(
                "access_token" => $facebook->getAccessToken(),
                "name" => $event_name,
                "start_time" => $start_time,
                "location" => $location
            );
            
            $fb_event_array = array('name' => "Test event in Group nnn",
            'start_time' => mktime("14","30","00","11","20","2012"),
                'category' => "1",
                'subcategory' => "1",
                'location' => "Location",
                'end_time' => mktime("15","30","00","11","21","2012"),
                'street' => "123 Street Address",
                'city' => "Sheffield",
                'phone' => "0123 456 7890",
                'email' => "info@email.com",
                'description' => "Description of the test event",
                'privacy_type' => "OPEN",
                'tagline' => "Event tagline",
                'host' => "Event host",
                'page_id' => "nnn"
            );
            
            $fb_event_utf8 = array_map("utf8_encode", $fb_event_array);
            
            $param = array(
            "method" => "events.create",
            "uids" => $user,
            "event_info" => json_encode($fb_event_utf8),
            "callback" => ""
            );
            
            //var_dump($facebook->api($param));
            //var_dump($facebook);
            
            //var_dump($this->getfacebookrequest($facebook, $user, '/me'));
            //echo json_encode(Array($event_name,$start_time));
            //echo $facebook->api("/me/events", "POST", $event_param);
            //echo $this->postfacebookrequest($facebook,$user,"/me/events",$event_param);

        }
    }
    /***********************************************************/
    //CUSTOM HTTP REQUEST BLOCKS
    
    function get_request($uri,$mode=NULL){
        
        if($mode==1){
            $r = new HttpRequest($uri, HttpRequest::METH_GET);
            $result = $r->send();
            
            return $result->getBody();
        }else if($mode==2){
            $ch = curl_init();

            // set URL and other appropriate options
            curl_setopt($ch, CURLOPT_URL, $uri);
            curl_setopt($ch, CURLOPT_HEADER, 0);
            curl_setopt($ch, CURLOPT_HTTPGET, true); //reset curl to do GET requests
            
            // grab URL and pass it to the browser
            $content = rtrim(curl_exec($ch),1);

            // close cURL resource, and free up system resources
            curl_close($ch);

            return $content;
        }else{
            $opts = array('http' =>
                array(
                    'method'  => 'GET',
                    'header'  => 'Content-type: text/plain; charset=UTF-8\r\n
                                  Server: Restlet-Framework/2.0.0\r\n
                                  Connection: close\r\n'
                )
            );
            
            $context = stream_context_create($opts);
            
            $r = file_get_contents($uri,false,$context);
            return $r;
        }  
    }
    
    function post_request($uri,$arg,$mode=NULL){
        
        if($mode==1){
            
            $r = new HttpRequest($uri, HttpRequest::METH_POST);
            $r->setContentType('Content-Type: text/xml');
            $r->setRawPostData($arg);
            $response = $r->send();
            
            return $response->getBody();
            
        }else if($mode==2){
            $ch = curl_init();
            
            curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));
            curl_setopt($ch, CURLOPT_URL, $uri);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $arg);
            curl_setopt($ch, CURLOPT_POST, true);
            
            $response = rtrim(curl_exec($ch),1);
            
            curl_close($ch);
            
            return $response;
        }else{
            $opts = array('http' =>
                array(
                    'method'  => 'POST',
                    'header'  => 'Content-type: text/plain; charset=UTF-8\r\n
                                  Server: Restlet-Framework/2.0.0\r\n
                                  Connection: close\r\n',
                    'content' => $arg
                )
            );
            
            $context = stream_context_create($opts);
            
            $r = file_get_contents($uri,false,$context);
            return $r;
        }
    }
    
    /***********************************************************/
    //FACEBOOK API ABSTRACTION
    function getfacebooktoken($app_id, $app_secret){
        $facebook = new Facebook(array(
            'appId'  => $app_id,
            'secret' => $app_secret,
        ));

        return $facebook;
    }
    
    function getfacebookrequest($facebook, $user, $arg){
        if ($user) {
            try {
                // Proceed knowing you have a logged in user who's authenticated.
                return $facebook->api($arg);

            } catch (FacebookApiException $e) {
                    error_log($e);
                    $user = null;
            }
        }else{
            return null;
        }
    }
    
    function postfacebookrequest($facebook, $user, $arg, $content){
        if ($user) {
            try {
                // Proceed knowing you have a logged in user who's authenticated.
                echo "in post<br>";
                return $facebook->api($arg,"POST",$content);

            } catch (FacebookApiException $e) {
                    error_log($e);
                    $user = null;
            }
        }else{
            return null;
        }
    }
    /***********************************************************/
    //DEBUG
    
    function testhttprequest(){

//        $r = new HttpRequest('http://localhost:8189/schedushare/user/a@b.com', HttpRequest::METH_GET);
        //$r = new HttpRequest('http://www.google.ca', HttpRequest::METH_GET);
//        $result = $r->send();
//        
//        echo $result->getBody();
        
//        $r = file_get_contents('http://www.google.ca');
//        
//        echo $r;
        $ch = curl_init();

        // set URL and other appropriate options
        curl_setopt($ch, CURLOPT_URL, "http://localhost:8189/schedushare/user/a@b.com");
        curl_setopt($ch, CURLOPT_HEADER, 0);

        curl_setopt($ch, CURLOPT_HTTPGET, true);
        
        // grab URL and pass it to the browser
        $content = rtrim(curl_exec($ch),1);

        // close cURL resource, and free up system resources
        curl_close($ch);
        
        echo $content;
        
    }
    
    
    
    
    
    function showphpinfo(){
        echo phpinfo();
    }
    
    
}

?>
