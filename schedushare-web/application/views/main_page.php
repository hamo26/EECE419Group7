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
            <button href="#create_schedule_modal" class="btn-primary" id="new_schedule_btn" data-toggle="modal">+ New Schedule</button>
            <h3><img src="https://graph.facebook.com/<?php echo $user; ?>/picture">&nbsp;My Schedules</h3>
            <div id="my_schedules">
            </div>
            <div id="create_schedule_modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h3 id="myModalLabel">Add New Schedule</h3>
                </div>
                
                <div class="modal-body">
                    <input id="search_box" type="text" name="schedule_name" placeholder="Schedule name...">
                </div>
                
                <div class="modal-footer">
                    <button class="btn" data-dismiss="modal" aria-hidden="true">Close</button>
                    <button class="btn btn-primary" id="create_schedule_submit" data-dismiss="modal">Save changes</button>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript" charset="utf-8">

var today = new Date();
var myemail = null;

$(document).ready(function(){     
    
    //verify user exist in DB
    check_login();
    
    //force load, must remove later
    fetchEvents();
    
    $('#new_schedule_btn').click(function(){
       console.log("create new schedule");
       
    });
    
    $('#create_schedule_submit').click(function(){

    });
    
    $('.friend_check').click(function(){
        if($(event.target).attr("checked")) {
            console.log("haha");
            console.log($(event.target).attr("data-id"));
            console.log($(event.target).attr("data-name"));
            
            $('#short_list').find('li[data-id='+$(event.target).attr("data-id")+']').remove();
            
            $('#short_list').find('ul').append(
                $(document.createElement('li')).attr('data-id',$(event.target).attr("data-id")).append(                
                    '<img src="https://graph.facebook.com/' + $(event.target).attr("data-id") + '/picture"/>&nbsp;&nbsp;&nbsp;'+$(event.target).attr("data-name")
                )
            );
                
            //load the person's active schedule
            get_active_schedule($(event.target).attr("data-id"));
            //load_timeblocks();
            
        }else{
            console.log("no");
        }
    });
    
    $('#short_list').click(function(){
        
        var id = $(event.target).parent().attr("data-id");
        
        $('.friend_check[data-id="'+id+'"]').attr("checked",false);
        
        $(event.target).parent().remove();
    });
    
    $('#search_result').click(function(){
        
        var id = $(event.target).parent().attr("data-id");
        var name = $(event.target).parent().attr("data-name");
        
        $('#short_list').find('li[data-id='+id+']').remove();
        
        $('#short_list').find('ul').append(
            $(document.createElement('li')).attr('data-id',id).append(                
                '<img src="https://graph.facebook.com/' + id + '/picture"/>&nbsp;&nbsp;&nbsp;'+name
            )
        );
    });
    
    $('#search_btn').click(function(){
        console.log("ready for search");
        console.log($('#search_box').val());
        
        $('#search_result').empty();
        
       $.ajax({
            url: 'index.php/main/findfriend', // current page
            type: 'GET',
            contentType: 'json',
            data: {name: $('#search_box').val()},
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
        
        //$('#search_result').append("nice!");
    });
    
    $('#time_tables').fullCalendar({
        // put your options and callbacks here
        select: function(start, end, allDay){
            console.log("in here");
            console.log("start="+start);
            console.log("end="+end);
            console.log("all day="+allDay);
        } 
    });    
    
    //fetchEvents();    
    //$('#time-tables').fullCalendar( 'removeEvents').fullCalendar('removeEventSources');   
});



function check_login(){
    $.getJSON("index.php/main/finduser",function(data){       
        
        if(!data){
            console.log("user not found");
            register_user();
        }else{
            console.log("user found");
            console.log(data);
            var user_id = data["user-id"];
            console.log(user_id);
            
            load_schedules(user_id);
        } 
        
    });  
}

function register_user(){
    $.getJSON("index.php/main/registeruser",function(data){
        console.log("user created");
        console.log(data);
    });
}



</script>


