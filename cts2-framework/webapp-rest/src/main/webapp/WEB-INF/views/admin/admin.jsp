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
<link rel="stylesheet" href="resources/desktop/assets/css/desktop.css" />
<link href="resources/desktop/assets/css/jquery.alerts.css" rel="stylesheet" type="text/css" media="screen" />
<!--[if lt IE 9]>
<link rel="stylesheet" href="assets/css/ie.css" />
<![endif]-->
</head>
<body>
<div class="abs" id="wrapper">
  <div class="abs" id="desktop">
    <a class="abs icon" style="left:20px;top:20px;" href="#icon_dock_service_plugins">
      <img src="resources/desktop/assets/images/icons/folder-preferences-icon.png" />
      Service Plugins
    </a>
    
    <a class="abs icon" style="left:20px;top:160px;" href="#icon_dock_admin_app">
      <img src="resources/desktop/assets/images/icons/box-config-icon.png" />
      Admin App
    </a>
   
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
          <div class="window_aside">
			<button id="removeButton" type="button">Remove Plugin</button>
			<button id="activateButton" type="button">Activate Plugin</button>
          </div>
          <div class="window_main">
            <table id="pluginTable" class="data">
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
        <div class="abs window_bottom">
            <form id="uploadForm" action="admin/plugins" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="MAX_FILE_SIZE" value="100000" />
                <img style="margin-bottom:4px;" src="resources/desktop/assets/images/icons/plugin-add-icon.png"></img>
                Upload Plugin: <input type="file" name="file" />
                <input type="submit" value="Upload" />
            </form>
            <img src="resources/desktop/assets/images/icons/plugin-add-icon.png"></img>
        </div>
      </div>
      <span class="abs ui-resizable-handle ui-resizable-se"></span>
    </div>
    
    
    
    
    
    
    <div id="window_admin_app" class="abs window">
      <div class="abs window_inner">
        <div class="window_top">
          <span class="float_left">
            <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
            Admin App
          </span>
          <span class="float_right">
            <a href="#" class="window_min"></a>
            <a href="#" class="window_resize"></a>
            <a href="#icon_dock_admin_app" class="window_close"></a>
          </span>
        </div>
        <div class="abs window_content">
          <div class="window_aside">
			<button id="saveConfigButton" type="button">Save Settings</button>
			<button id="resetConfigButton" type="button">Reset Settings</button>
          </div>
          <div class="window_main">
            <table id="contextConfigTable" class="data">
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
      <li id="icon_dock_service_plugins">
        <a href="#window_service_plugins">
          <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
          Service Plugins
        </a>
      </li>
      
       <li id="icon_dock_admin_app">
        <a href="#window_admin_app">
          <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
          Admin App
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

<script>
  !window.jQuery && document.write(unescape('%3Cscript src="assets/js/jquery.js"%3E%3C/script%3E'));
  !window.jQuery.ui && document.write(unescape('%3Cscript src="assets/js/jquery.ui.js"%3E%3C/script%3E'));
</script>
<script src="resources/desktop/assets/js/admin.js"></script>
<script src="resources/desktop/assets/js/jquery.desktop.js"></script>
</body>
</html>