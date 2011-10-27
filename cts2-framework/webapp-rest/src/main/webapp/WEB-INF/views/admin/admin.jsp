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
  
    <a class="abs icon" style="left:20px;top:160px;" href="#icon_dock_service_plugins">
      <img src="resources/desktop/assets/images/icons/folder-preferences-icon.png" />
      Service Plugins
    </a>
    
    <a class="abs icon" style="left:20px;top:300px;" href="#icon_dock_plugin_admin">
      <img src="resources/desktop/assets/images/icons/box-config-icon.png" />
      Plugin Admin
    </a>
    
    <a class="abs icon" style="left:20px;top:440px;" href="#icon_dock_service_admin">
      <img src="resources/desktop/assets/images/icons/service-config-icon.png" />
      Service Admin
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
		
		<h1>TODO:</h1>
		<p>...</p>
		<br>
		<h1>TODO:</h1>
		<p>...</p>

          </div>
          <div class="window_main">
          <div>
          
	</div>
          </div>
        </div>
        <div class="abs window_bottom">
            
        </div>
      </div>
      <span class="abs ui-resizable-handle ui-resizable-se"></span>
    </div>  
   
    <div id="window_service_plugins" class="abs window">
      <div class="abs window_inner">
        <div class="window_top">
          <span class="float_left">
            <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
            Service Plugins
          </span>
          <span class="float_right">
            <a href="#" class="window_min"></a>
            <a href="#" class="window_resize"></a>
            <a href="#icon_dock_service_plugins" class="window_close"></a>
          </span>
        </div>
        <div class="abs window_content">
          <div class="window_aside" style="height: 60px">
          	<h1>Removing a Plugin</h1>
          	<p>Removing a Plugin will completely remove it frome the service,
          	deactivating it if it is currently active.</p>
			<br>
			<h1>Activating a Plugin</h1>
          	<p>Activating a Plugin will set that Plugin as set current active
          	service plugin.</p>

          </div>
          <div class="window_main">

            <table id="pluginTable" class="display">
              <thead>
                <tr>
                  <th>
                    Plugin Name
                  </th>
                  <th>
                    Plugin Version
                  </th>
                   <th>
                    Is Active
                  </th>
                </tr>
              </thead>
            </table>
          </div>
        </div>
 
        <div id="uploadFormDiv" class="abs window_bottom" style="height: 70px;">
        	<div style="padding: 10px;">
      			<button id="removeButton" type="button">Remove Plugin</button>
         	 	<button id="activateButton" type="button">Activate Plugin</button>
         	 </div>
           <form id="uploadForm" action="admin/plugins" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="MAX_FILE_SIZE" value="100000" />
                <img style="margin-bottom:4px;" src="resources/desktop/assets/images/icons/plugin-add-icon.png"></img>
                Upload Plugin: <input style="display: inline" type="file" name="file" />
                <input type="submit" style="display: inline" id="fileUploadButton" value="Upload" />
            </form>
            <img src="resources/desktop/assets/images/icons/plugin-add-icon.png"></img>
        </div>
 
      </div>
      <span class="abs ui-resizable-handle ui-resizable-se"></span>
  
    </div>

    <div id="window_plugin_admin" class="abs window" style="height: 350px;">
      <div class="abs window_inner">
        <div class="window_top">
          <span class="float_left">
            <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
            Plugin Admin
          </span>
          <span class="float_right">
            <a href="#" class="window_min"></a>
            <a href="#" class="window_resize"></a>
            <a href="#icon_dock_plugin_admin" class="window_close"></a>
          </span>
        </div>
        <div class="abs window_content">
          <div class="window_aside">
           	 	<h1>Current Plugin Settings</h1>
           	 	<p>These are the settings for the current in-use Service Plugin.</p>
           	 	<br>
           	 	<h1>Edit a Settings</h1>
           	 	<p>
           	 	Click on a setting in the table to modify it, and 
           	 	press 'ENTER' to change the value. Press the "Save Settings" button
           	 	to save the modifications, or "Reset Settings" to return to defaults.
           	 	</p>
          </div>
          <div class="window_main">
      
            <table id="pluginAdminTable" class="display">
              <thead>
                <tr>
                  <th>
                    Property Name
                  </th>
                  <th>
                    Property Value
                  </th>
                </tr>
              </thead>
             
            </table>
       
   
          </div>
        </div>
       
        <div class="abs window_bottom" style="height:50px">
           <div>
           	 	<button id="saveConfigButton" type="button">Save Settings</button>
				<button id="resetConfigButton" type="button">Reset Settings</button>
          </div>
        </div>
      </div>
      <span class="abs ui-resizable-handle ui-resizable-se"></span>
    </div>
    
  
    
    
    <div id="window_service_admin" class="abs window" style="height: 350px;">
      <div class="abs window_inner">
        <div class="window_top">
          <span class="float_left">
            <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
            Service Admin
          </span>
          <span class="float_right">
            <a href="#" class="window_min"></a>
            <a href="#" class="window_resize"></a>
            <a href="#icon_dock_service_admin" class="window_close"></a>
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
          	<div id="tabs">
	<ul>
		<li><a href="#tabs-1">Web Admin Password</a></li>
		<li><a href="#tabs-2">Server Context</a></li>
	</ul>
	<div id="tabs-1"  title="Change Web Admin Password">
				<form class="cmxform" id="changeAdminPasswordForm">
				<fieldset>
					<p>
						<label for="old_password">Old Password</label>
						<input type="password" name="old_password" id="old_password" value="" class="text ui-widget-content ui-corner-all" />
					</p>
					<p>
						<label for="new_password">New Password</label>
						<input type="password" name="new_password" id="new_password" value="" class="text ui-widget-content ui-corner-all" />
					</p>
					<p>
						<label for="confirm_password">Confirm Password</label>
						<input type="password" name="confirm_password" id="confirm_password" value="" class="text ui-widget-content ui-corner-all" />
					</p>
				</fieldset>
				<button id="change-web-admin-password-button" type="button">Change Password</button>
				<button id="cancel_web-admin-change_password_button" type="button">Cancel</button>
				</form>	
		
	</div>
	<div id="tabs-2">
			<form class="cmxform" id="changeServerUrlForm">
						<fieldset>
							<p>
								<label for="server_url">Server URL:</label>
								<input name="server_url" id="server_url" value="" class="text ui-widget-content ui-corner-all" />
							</p>
							<input type="submit" value="Change URL" /> 
						</fieldset>
					</form>
					
		<form class="cmxform" id="changeWebappNameForm">
						<fieldset>
							<p>
								<label for="webapp_name">WebApp Name:</label>
								<input name="webapp_name" id="webapp_name" value="" class="text ui-widget-content ui-corner-all" />
							</p>
							<input type="submit" value="Change WebApp Name" /> 
						</fieldset>
					</form>
	</div>

</div>
	
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
      
      <li id="icon_dock_service_plugins">
        <a href="#window_service_plugins">
          <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
          Service Plugins
        </a>
      </li>
      
       <li id="icon_dock_plugin_admin">
        <a href="#window_plugin_admin">
          <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
          Plugin Admin
        </a>
      </li>
      
      <li id="icon_dock_service_admin">
        <a href="#window_service_admin">
          <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
          Service Admin
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