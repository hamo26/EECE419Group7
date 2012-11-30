<div id="main" class="container-fluid">
    <div class="row-fluid">
        <div id="friends-time-tables" class="span3">

            <div>
                <input id="search_box" type="text" class="span6" name="friend_name" placeholder="Find a friend...">
                <button id="search_btn" class="btn-primary" style="margin-bottom: 10px;">Go!</button>                      
            </div>
            (click to add)
            <div id="search_result">           
            </div>
            
            <hr>
            
            <h3>Selected</h3> (click to remove)
            <div id="short_list">
                <ul class="nav nav-list"> 
                </ul>
            </div>
            
            <hr>
            
            <h3>Friends</h3>
            <div id="friend_list" style="overflow:auto; height:500px; width:230px">
                <?php

                    echo '<ul class="nav nav-list">';
                    foreach ($friends["data"] as $value) {
                        echo '<li value="'.$value["id"].'">';
                        echo '<img src="https://graph.facebook.com/' . $value["id"] . '/picture"/>';
                        echo '&nbsp;&nbsp;&nbsp;'.$value["name"];
                        echo '<input type="checkbox" class="friend_check pull-right" data-id="'.$value["id"].'" data-name="'.$value["name"].'" style="margin-bottom: 5px;">';
                        echo '</li>';
                    }
                    echo '</ul>';

                ?>
            </div>
            
           
        </div>

        <div id="time_tables" class="span6">
        </div>
        <div class="span3">
            <button href="#create_schedule_modal" class="btn-primary hidden" id="new_schedule_btn" data-toggle="modal">+ New Schedule</button>
            <button href="#create_event_modal" class="btn-primary" id="new_event_btn" data-toggle="modal" >+ New Event</button>
            
            <h4><img src="https://graph.facebook.com/<?php echo $user; ?>/picture">&nbsp;My Schedules</h4>
            <div id="my_schedules">
            </div> 
            
<!--            CREATE SCHEDULE -- NOT USED -->
            <div id="create_schedule_modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="new_schedule_box_label" aria-hidden="true">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h3 id="new_schedule_box_label">Add New Schedule</h3>
                </div>
                
                <div class="modal-body">
                    <input id="" type="text" name="schedule_name" placeholder="Schedule name...">
                </div>
                
                <div class="modal-footer">
                    <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                    <button class="btn btn-primary" id="create_schedule_submit" data-dismiss="modal">Save changes</button>
                </div>
            </div>

<!--            CREATE EVENT -->     

            <div id="create_event_modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="new_event_box_label" aria-hidden="true">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h3 id="new_event_box_label">Create New Event</h3>
                </div>
                
                <div class="modal-body">
                    <input id="event_name" type="text" name="event_name" placeholder="Event name..." >
                    <input id="event_location" type="text" name="event_location" placeholder="Event location..." >
                    <br>(Drag time table time slots to set start/end time)<br>
                    <input id="event_start" type="text" name="event_start" placeholder="Event start..." disabled="disabled">
                    <input id="event_end" type="text" name="event_end" placeholder="Event end..." disabled="disabled">
                    <div id="event_people" style="overflow:auto; height:150px; width:230px">
                        <h5>Invite List...</h5>
                        <ul class="nav nav-list">                            
                        </ul>
                    </div>
                </div>
                
                <div class="modal-footer">
                    <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                    <button class="btn btn-primary" id="create_event_submit" data-dismiss="modal">Save changes</button>
                </div>
            </div>

<!--            ERROR MSG --> 

        </div>
    </div>
</div>

<script type="text/javascript" charset="utf-8">

var today = new Date();
var myemail = null;
var colors = new Array();
var color_index = 0;
var num_of_colors = 6;
var loaded_schedules = new Array();
var my_loaded_schedule;
var short_list = new Array();

$(document).ready(function(){     
    
    //initialize available event colors
    initcolors();
    
    //verify user exist in DB
    check_login();
    
    $('#my_schedules').click(function(event){
       var name = $(event.target).parent().attr("data-name"); 
       var schedule_id = $(event.target).parent().attr("data-schedule-id");
       //console.log($(event.target));
       
       var new_schedule = $(event.target);
       var old_schedule = $(event.target).parent().parent().find(".icon-eye-open").parent();
 
       //remove the eye icon
       old_schedule.find(".icon-eye-open").remove();
       old_schedule.append($(document.createElement("i")).addClass("icon-calendar pull-left"));
       
       //add eye icon to new selected scheduloe
       new_schedule.find(".icon-calendar").remove();
       new_schedule.append('<i class="icon-eye-open pull-left"></i>');
       
       //unload the old time blocks
       unload_timeblocks(my_loaded_schedule);
       
       //set new schedule
       my_loaded_schedule = schedule_id;
       
       //load new schedule
       load_timeblocks(schedule_id,name);
       loaded_schedules.push(new Array(schedule_id,name));
    });
    
    $('#new_schedule_btn').click(function(event){
       console.log("create new schedule");
       
    });
    
    $('#create_schedule_submit').click(function(event){

    });
    
    $('.friend_check').click(function(event){
        
        var user_id = $(event.target).attr("data-id");
        var user_name = $(event.target).attr("data-name");
        
        if($(event.target).attr("checked")) {
            
            add_shortlist_element(user_id,user_name);
            
        }else{
            console.log("no");
            
            var schedule_id = $('#sl-'+user_id).attr("data-schedule-id");
            
            //remove the schedule
            unload_timeblocks(schedule_id);
            
            //remove from short list
            $('#sl-'+user_id).remove();
        }
    });
    
    $('#short_list').click(function(event){
        
        var user_id = $(event.target).parent().attr("data-id");
        var schedule_id = $(event.target).parent().attr("data-schedule-id");
        var user_name = $(event.target).parent().attr("data-name");
        
        $('.friend_check[data-id="'+user_id+'"]').attr("checked",false);
        
        //remove the person from the shortlist
        var index = short_list.indexOf(new Array(user_id,user_name));
        short_list.splice(index,1);
        
        //remove the person from event modal
        
        
        //remove the schedule
        unload_timeblocks(schedule_id);
        
        $(event.target).parent().remove();
    });
    
    $('#search_result').click(function(event){
        
        var id = $(event.target).parent().attr("data-id");
        var name = $(event.target).parent().attr("data-name");
        
        add_shortlist_element(id,name);
            
    });
    
    $('#search_btn').click(function(event){
        console.log("ready for search");
        console.log($('#search_box').val());
        
        $('#search_result').empty();
        
        find_friend($('#search_box').val());     
        
        //$('#search_result').append("nice!");
    });
    
    $('#time_tables').fullCalendar({
        // put your options and callbacks here
        select: function(start, end, allDay){
            console.log("in here");
            console.log(start);
            console.log(end);
            console.log("all day="+allDay);
            
            start = $.fullCalendar.formatDate( start, "u");
            end = $.fullCalendar.formatDate( end, "u");
            
            $('#event_start').attr("value",start);
            $('#event_end').attr("value",end);
        },
        unselect: function(){
            console.log("deselected");
        }
    });       
    
    $('#create_event_submit').click(function(event){
        console.log("submit new event!");
        var event_name = $('#event_name').val();
        var event_location = $('#event_location').val();
        var event_start = $('#event_start').val();
        var event_end = $('#event_end').val();
        //var event_people = [];
        
        make_new_event(event_name,event_start,event_end,event_location,short_list);
        //make_new_event();
    });
    
    $('#create_event_modal').on("show",function(event){
        
        $('#event_people').find('ul').empty();
        
        if(short_list.length==0){
           $('#event_people').find("ul").append('<i>Empty...add friends to Select List to invite them</i>');
        }else{
           $.each(short_list,function(index,value){
                $('#event_people').find("ul").append(
                    $(document.createElement("li")).attr("id","ev-"+value[0]).append(
                    '<img src="https://graph.facebook.com/' + value[0] + '/picture"/>&nbsp;&nbsp;&nbsp;' + value[1]
                    )
                );
            }); 
        }
    });
    
    //fetchEvents();    
    //$('#time-tables').fullCalendar( 'removeEvents').fullCalendar('removeEventSources');   
});

function check_login(){

    $.ajax({
        type: "GET",
        url: "index.php/main/finduser",
        dataType: "json",
        success: function(data){

            if(!data){
                console.log("no response from server!!");
                
                $('#my_schedules').append("<i>we are experiencing server issue, please try again later...</i>");
            }else{
                console.log("user found");
                
                console.log(data);
                var user_id = data["user-id"];
                console.log(user_id);

                load_schedules(data);
            }
        },
        error: function(data){
            console.log("user not found!");
            register_user();
        }
    });
}

function register_user(){

    $.ajax({
        type: "GET",
        url: "index.php/main/registeruser",
        dataType: "json/text",
        success: function(data){

            console.log("user created!");
            check_login();
        },
        error: function(data){
            console.log("register error!");
            
            //most likely server error
            $('#my_schedules').append("<i>we are experiencing server issue, please try again later...</i>");
            
            load_error('Server error! Please try again later!');
        }
    });
}

function load_error(msg){
    $('#time_tables').prepend('<div id="error_msg_area"></div>');
            
    $('#error_msg_area').empty();

    $('#error_msg_area').append(
        $(document.createElement("div")).addClass('alert alert-error').append(
            msg,
            '<button type="button" class="close" data-dismiss="alert">×</button>'
        )
    );
}

function find_friend(friend_name){
    $.ajax({
        url: 'index.php/main/findfriend', // current page
        type: 'GET',
        contentType: 'json',
        data: {name: friend_name},
        success: function(data) {
            // ...
            console.log(data);

            var friend = JSON.parse(data);

            console.log(friend.name);
            console.log(friend.id);               

            $('#search_result').append(
                $(document.createElement('ul')).addClass('nav nav-list').append(
                    $(document.createElement('li')).attr('data-id',friend.id).attr('data-name',friend.name).append(
                        '<img src="https://graph.facebook.com/' + friend.id + '/picture"/>&nbsp;&nbsp;&nbsp;'+friend.name         
                    )
                )
            );

            console.log("success");
        },
        error: function (){
            console.log("fail!");
        }
    });
}

function make_new_event(name,start,end,location,people){
 
    $.ajax({
        type: "POST",
        url: "index.php/main/create_event",
        dataType: "json",
        data:{name: name, start: start, end: end, location: location, people: people},
        success: function(data){
            
            console.log("event created!");
            console.log(data);
            
            if(people){
                invite_to_event(data["id"],people);
            }
            
            //add new event block (temp)
            addEvent(name,start,end);
            
            window.open("http://www.facebook.com/events/list", '_blank');
        },
        error: function(data){
            console.log("event error!");
            console.log(data);
            
            load_error('An error has occured while creating event! Be sure all fields are filled in!');

           addEvent(name,start,end);     
        }
    });
}

function invite_to_event(event_id,people){
    $.ajax({
        type: "POST",
        url: "index.php/main/event_invite",
        dataType: "json",
        data:{event_id: event_id, people: people},
        success: function(data){
            
            console.log("people invited!");
            console.log(data);            
            
        },
        error: function(data){
            console.log("invite error!");
            console.log(data); 
        }
    });
}

function add_shortlist_element(user_id, user_name){
    $('#short_list').find('li[data-id='+user_id+']').remove();
            
    $('#short_list').find('ul').append(
        $(document.createElement('li')).attr('id','sl-'+user_id).attr('data-id',user_id).attr('data-name',user_name).append(                
            '<img src="https://graph.facebook.com/' + user_id + '/picture"/>&nbsp;&nbsp;&nbsp;' + user_name
        )
    );

    //add user to a list, so we can invite them to an event if one so choose
    short_list.push(new Array(user_id,user_name));    
    
    //load the person's active schedule
    load_active_schedule(user_id,user_name);

}

</script>


