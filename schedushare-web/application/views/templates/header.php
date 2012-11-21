<!DOCTYPE html>

<html lang="en">
<head>
    <!-- META SETTINGS HERE -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">    
    
    <?php 
//         $path = str_replace('http:','',base_url());   
        $path = base_url();
    ?>
    
    <!-- STYLE SHEETS HERE -->
    <link rel="stylesheet" type="text/css" href="<?php echo $path;?>application/css/bootstrap-responsive.css">
    <link rel="stylesheet" type="text/css" href="<?php echo $path;?>application/css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="<?php echo $path;?>application/css/fullcalendar.css">
    
    <!-- JAVASCRIPTS HERE -->
    <script type="text/javascript" src="<?php echo $path;?>application/js/jquery-1.8.2.js"></script>
    <script type="text/javascript" src="<?php echo $path;?>application/js/bootstrap.js"></script>
    <script type="text/javascript" src="<?php echo $path;?>application/js/fullcalendar.js"></script>
    <script type="text/javascript" src="<?php echo $path;?>application/js/jquery-ui-1.8.23.custom.min.js"></script>
    <script type="text/javascript" src="<?php echo $path;?>application/js/timetable.js"></script>
    
    <title><?php echo $title ?> - Sched-u-Share</title>
</head>
<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
    <div class="container">
          <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        <a class="brand" href="#">Sched-U-Share</a>
        <div class="nav-collapse collapse">
        <ul class="nav">
<!--            <li class="active"><a href="#">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>-->
        </ul>
        </div><!--/.nav-collapse -->
    </div>
    </div>
</div>
     
<br>
<br>
<br>