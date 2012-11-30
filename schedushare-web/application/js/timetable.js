/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function load_active_schedule(user_id, user_name){
    $.getJSON("index.php/main/getactiveschedule",{userid: user_id},function(data){
        
        console.log(data);
        
        if(data){
            $('#short_list').find('li[data-id='+user_id+']').attr("data-schedule-id",data["schedule-id"]);
        
            load_timeblocks(data["schedule-id"], user_name);
            //loaded_schedules[data["schedule-id"]] = user_name;
            loaded_schedules.push(new Array(data["schedule-id"],user_name));
        }else{
            console.log("user has no active schedule, or is not a user of schedushare");
            
            var tag = '<span class="label label-warning pull-right"><i class="icon-ban-circle"></i><i class="icon-calendar"></i>!</span>';
            
            $('#sl-'+user_id).append(tag);
        }
        
        
    });
}

function load_schedule(schedule_id){
    
}

function load_schedules(user){
    $.getJSON("index.php/main/getschedules",{userid: user["user-id"]},function(data){
        console.log("schedules fetched");
        console.log(data);
        
        $('#my_schedules').empty();
        
        if(!(data.scheduleList.length)){
            $('#my_schedules').append("you have no schedules!");
        }else{
            
            $('#my_schedules').append(
                    $(document.createElement("ul")).addClass("nav nav-list")
            );
            
            $.each(data.scheduleList,function(index,value){
                
                var active_tag = '';
                var viewing_tag = '<i class="icon-calendar"></i>';
                
                //if schedule is active, load it to screen
                if(value.active){
                    console.log(value);
                    load_timeblocks(value["schedule-id"],user["name"]);
                    //loaded_schedules[value["schedule-id"]] = user["name"];
                    loaded_schedules.push(new Array(value["schedule-id"],user["name"]));
                    my_loaded_schedule = value["schedule-id"];
                    
                    active_tag = '<span class="label label-success pull-right">A</span>';
                    viewing_tag = '<i class="icon-eye-open"></i>';
                }

                $('#my_schedules').find('ul').append(
                    $(document.createElement("li")).attr("data-schedule-id",value["schedule-id"]).attr("data-name",user["name"]).append(
                        '<h5>'+viewing_tag+active_tag+value["schedule-name"]+'</h5>'
                    )
                );
 
            });
        }
    });
}

function load_timeblocks(schedule_id,owner){
    $.getJSON("index.php/main/gettimeblocks",{scheduleid: schedule_id},function(data){
        
        console.log(data);
        
        addEvents(data["time-blocks"],owner,schedule_id);
        
    });
}

function unload_timeblocks(schedule_id){
    
    if(schedule_id){
        //clear all timeblocks
        clear_table();       

        //remove the schedule of interest
        //delete loaded_schedules[schedule_id];

        $.each(loaded_schedules,function(index,value){
            if(value){
                if(value[0]==schedule_id){
                    //delete loaded_schedules[index];
                    loaded_schedules.splice(index,1);
                }
            }
        });

        //reload all timeblocks
        console.log(loaded_schedules);

        //reset the color index
        color_index = 0;

        $.each(loaded_schedules,function(index,value){

             if(value)load_timeblocks(value[0],value[1]);

        });
    }
}

//Helpers
function initcolors(){
    colors[0] = "red";
    colors[1] = "orange";
    colors[2] = "yellow";
    colors[3] = "green";
    colors[4] = "blue";
    colors[5] = "violet";
}

function addEvents(event_list, owner, schedule_id){
    $('#time_tables').fullCalendar( 'addEventSource',        
        function(s, e, callback) {
            // When requested, dynamically generate virtual
            // events for every monday and wednesday.
            var events = [];
            
            $.each(event_list,function(index,event){
                
                var description = ''; //blank
                
                if(event.description) description = event.description;
                
                events.push({
                    title: owner,
                    start: convertToDate(event.day,event["start-time"]),
                    end: convertToDate(event.day,event["end-time"]),
                    color: colors[color_index%num_of_colors]
                });
            });

            color_index++;
            
            //loaded_events[schedule_id] = events;
            
            // return events generated
            callback( events );
        }
    );
}

function addEvent(name,start,end){
    $('#time_tables').fullCalendar( 'addEventSource',        
        function(s, e, callback) {
            // When requested, dynamically generate virtual
            // events for every monday and wednesday.
            var events = [];

            events.push({
                title: name,
                start: start,
                end: end,
                color: colors[color_index%num_of_colors]
            });

            color_index++;
            
            //loaded_events[schedule_id] = events;
            
            // return events generated
            callback( events );
        }
    );
}

function convertToDate(day,time){
        
    var full_date;
    
    var day_of_week_today = today.getDay();
    var diff = getDayOfWeek(day) - day_of_week_today;
    var new_date = today.getDate() + diff;
    
    full_date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+new_date+' '+time;
    
    console.log(full_date);
    
    return full_date;
}

function getDayOfWeek(day){
    
    switch(day){
        case "Monday":return 1;
        case "Tuesday":return 2;
        case "Wednesday":return 3;   
        case "Thursday":return 4;    
        case "Friday":return 5;
        case "Saturday":return 6;
        case "Sunday":return 0;     
    }
}

function clear_table(){
    $('#time_tables').fullCalendar( 'removeEvents').fullCalendar('removeEventSources'); 
    
    
}

//testing

function fetchEvents(){
    $.getJSON(
        'index.php/main/fetchevents', // current page
        function(data) {
            // ...

            addEvents(data);
            
            console.log("success");
        } 
    );
}
