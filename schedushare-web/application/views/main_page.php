<div id="main" class="container-fluid">
    <div class="row-fluid">
        <div id="friends-time-tables" class="span3">

            <form>
                <input type="text" class="span6" name="friend_name" placeholder="Find a friend...">
                <input class="btn-primary" type="submit" value="Submit" style="margin-bottom: 10px;">                      
            </form>

            <h3>Friends</h3>
            
            You have no friends...
            <br>
            Forever alone...
        </div>
        <div id="time-tables" class="span6">
        </div>
        <div id="my-time-tables" class="span3">
            This is where my time tables are
        </div>
    </div>
</div>

<script type="text/javascript" charset="utf-8">

var today = new Date();

$(document).ready(function(){
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


