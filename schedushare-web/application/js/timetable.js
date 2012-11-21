/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function get_active_schedule(user_id){
    $.getJSON("index.php/main/getactiveschedule",{userid: user_id},function(data){
        
        console.log(data);
        
        load_timeblocks(data["schedule-id"]);
        //addEvents(data["time-blocks"]);
        
    });
}

function load_schedules(user_id){
    $.getJSON("index.php/main/getschedules",{userid: user_id},function(data){
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
                console.log("iterating");
                //console.log(value["schedule-name"]);
                $('#my_schedules').find('ul').append(
                    $(document.createElement("li")).append('<h5><i class="icon-calendar"></i>'+value["schedule-name"]+'</h5>')
                );
                    
                //if schedule is active, load it to screen
                if(value.active){
                    console.log(value);
                    load_timeblocks(value["schedule-id"]);
                }
            });
        }
    });
}

function load_timeblocks(schedule_id){
    $.getJSON("index.php/main/gettimeblocks",{scheduleid: schedule_id},function(data){
        
        console.log(data);
        
        addEvents(data["time-blocks"]);
        
    });
}

//Helpers

function addEvents(event_list){
    $('#time_tables').fullCalendar( 'addEventSource',        
        function(s, e, callback) {
            // When requested, dynamically generate virtual
            // events for every monday and wednesday.
            var events = [];
            
            $.each(event_list,function(index,event){
                
                var description = ''; //blank
                
                if(event.description) description = event.description;
                
                events.push({
                    title: description,
                    start: convertToDate(event.day,event["start-time"]),
                    end: convertToDate(event.day,event["end-time"])
                });
            });
            
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
        case "Monday": return 0;
        case "Tuesday": return 1;
        case "Wednesday": return 2;   
        case "Thursday": return 3;    
        case "Friday": return 4;
        case "Saturday": return 5;
        case "Sunday": return 6;     
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
