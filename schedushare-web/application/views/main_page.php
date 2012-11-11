<div id="main" class="container-fluid">
    <div class="row-fluid">
        <div id="friends-time-tables" class="span3">

            <div>
                <input id="search_box" type="text" class="span6" name="friend_name" placeholder="Find a friend...">
                <button id="search_btn" class="btn-primary" style="margin-bottom: 10px;">Go!</button>                      
            </div>
            <div id="search_result">
                
            </div>
            
            <h3>Friends</h3>
            <div id="friend-list">
                <?php

                    echo '<ul class="nav nav-list">';
                    foreach ($friends["data"] as $value) {
                        echo '<li value="'.$value["id"].'">';
                        echo '<img src="https://graph.facebook.com/' . $value["id"] . '/picture"/>';
                        echo '&nbsp;&nbsp;&nbsp;'.$value["name"]; 
                        echo '</li>';
                    }
                    echo '</ul>';

                ?>
            </div>
            
           
        </div>
        <div id="time-tables" class="span6">
        </div>
        <div id="my-time-tables" class="span3">
            <img src="https://graph.facebook.com/<?php echo $user; ?>/picture">
            This is where my time tables are
        </div>
    </div>
</div>

<script type="text/javascript" charset="utf-8">

var today = new Date();

$(document).ready(function(){
    
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
                        $(document.createElement('li')).append(
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
    
    
    
    $('#time-tables').fullCalendar({
        // put your options and callbacks here
        select: function(start, end, allDay){
            console.log("in here");
            console.log("start="+start);
            console.log("end="+end);
            console.log("all day="+allDay);
        },
        
        eventSources: [
            {
                events: [
                    {
                        title  : 'This',
                        start  : '2012-'+(today.getMonth()+1)+'-'+today.getDate()+' 03:00:00',
                        end    : '2012-'+(today.getMonth()+1)+'-'+today.getDate()+' 04:30:00'
                    },
                    {
                        title  : 'Is',
                        start  : '2012-'+(today.getMonth()+1)+'-'+today.getDate()+' 06:00:00',
                        end    : '2012-'+(today.getMonth()+1)+'-'+today.getDate()+' 07:30:00'
                    },
                    {
                        title  : 'Sparta!!!',
                        start  : '2012-'+(today.getMonth()+1)+'-'+today.getDate()+' 12:00:00',
                        end    : '2012-'+(today.getMonth()+1)+'-'+today.getDate()+' 14:30:00'
                    }
                ],
                eventColor: 'blue'
                
            }
        ]
        
        
    }); 
});

</script>


