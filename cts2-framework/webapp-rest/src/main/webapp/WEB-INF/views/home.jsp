<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge, chrome=1" />
<meta name="description" content="CTS2 Development Framework Admin Desktop." />
<title>CTS2 Development Framework Admin Desktop</title>
<!--[if lt IE 7]>
<script>
window.top.location = 'http://desktop.sonspring.com/ie.html';
</script>
<![endif]-->
<link rel="stylesheet" href="resources/desktop/assets/css/reset.css" />

<link href="resources/desktop/assets/css/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
<link rel="stylesheet" href="resources/desktop/assets/css/smoothness/jquery-ui-1.8.16.custom.css" />

<link rel="stylesheet" href="resources/desktop/assets/css/demo_table.css" />
<link rel="stylesheet" href="resources/desktop/assets/css/demo_page.css" />
<link rel="stylesheet" href="resources/desktop/assets/css/demo_table_jui.css" />
<link rel="stylesheet" href="resources/desktop/assets/css/desktop.css" />

<!--[if lt IE 9]>
<link rel="stylesheet" href="assets/css/ie.css" />
<![endif]-->
</head>
<body>
<div class="abs" id="wrapper">
  <div class="abs" id="desktop">
  
    <a class="abs icon" style="left:20px;top:20px;" href="#icon_dock_service_information">
      <img src="resources/desktop/assets/images/icons/service-info-icon.png" />
      Service Information
    </a>
  
    <a class="abs icon normalLink" style="left:20px;top:160px;" href="admin">
      <img src="resources/desktop/assets/images/icons/user-login-icon.png" />
      Login as Admin
    </a>
  
      
   <a class="abs icon" style="left:20px;top:300px;" href="editor">
      <img src="resources/desktop/assets/images/icons/editor-icon.png" />
      Editor
    </a>
    
    <div id="window_service_information" class="abs window" style="height: 350px;">
      <div class="abs window_inner">
        <div class="window_top">
          <span class="float_left">
            <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
            Service Information
          </span>
          <span class="float_right">
            <a href="#" class="window_min"></a>
            <a href="#" class="window_resize"></a>
            <a href="#icon_dock_service_information" class="window_close"></a>
          </span>
        </div>
        
        <div class="abs window_content">
          <div class="window_aside">
		
		<h1>Web Admin Password</h1>
		<p>This password controls access to this admin panel.</p>
		<br>
		<h1>Server Context</h1>
		<p>The Server Context contains information regarding the URL and WebApp Name of the service.
		Plugins will rely on these values to build correct links to other content in the service.
		</p>

          </div>
          <div class="window_main">
          	
	
          </div>
        </div>
        <div class="abs window_bottom">
            
        </div>
      </div>
      <span class="abs ui-resizable-handle ui-resizable-se"></span>
    </div>  
    
    
    
    
    
  </div>
  <div class="abs" id="bar_top">
    <span class="float_right" id="clock"></span>
    <ul>
      <li>
        <a class="menu_trigger" href="#">CTS2 Resources</a>
        <ul class="menu">
          <li>
            <a href="http://informatics.mayo.edu/cts2/framework">CTS2 Develepment Framework</a>
          </li>
          <li>
            <a href="http://informatics.mayo.edu/cts2/">CTS2 Main Page</a>
          </li>
        </ul>
      </li>

    </ul>
  </div>
  <div class="abs" id="bar_bottom">
    <a class="float_left" href="#" id="show_desktop" title="Show Desktop">
      <img src="resources/desktop/assets/images/icons/icon_22_desktop.png" />
    </a>
    <ul id="dock">
    
      <li id="icon_dock_service_information">
        <a href="#window_service_information">
          <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
          Service Information
        </a>
      </li>

    </ul>
    
  </div>
</div>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.3/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
<script src="resources/desktop/assets/js/jquery.json-2.3.min.js"></script>
<script src="resources/desktop/assets/js/jquery.form.js"></script>
<script src="resources/desktop/assets/js/jquery.jeditable.mini.js" type="text/javascript"></script>
<script src="resources/desktop/assets/js/jquery.dataTables.min.js"></script>
<script src="resources/desktop/assets/js/jquery.alerts.js" type="text/javascript"></script>
<script src="resources/desktop/assets/js/passwordStrengthMeter.js" type="text/javascript"></script>
<script src="resources/desktop/assets/js/jquery-ui-1.8.16.custom.min.js" type="text/javascript"></script>
<script src="resources/desktop/assets/js/jquery.validate.js" type="text/javascript"></script>

<script>
  !window.jQuery && document.write(unescape('%3Cscript src="assets/js/jquery.js"%3E%3C/script%3E'));
  !window.jQuery.ui && document.write(unescape('%3Cscript src="assets/js/jquery.ui.js"%3E%3C/script%3E'));
</script>
<script src="resources/desktop/assets/js/admin.js"></script>
<script src="resources/desktop/assets/js/jquery.desktop.js"></script>
</body>
</html>