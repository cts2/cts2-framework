<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge, chrome=1" />
<meta name="description" content="JavaScript desktop environment built with jQuery." />
<title>jQuery Desktop</title>
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
   
    <div id="window_computer" class="abs window">
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
  </div>
  <div class="abs" id="bar_top">
    <span class="float_right" id="clock"></span>
    <ul>
      <li>
        <a class="menu_trigger" href="#">jQuery Desktop</a>
        <ul class="menu">
          <li>
            <a href="http://www.amazon.com/dp/0596159773?tag=sons-20">jQuery Cookbook</a>
          </li>
          <li>
            <a href="http://jqueryenlightenment.com/">jQuery Enlightenment</a>
          </li>
          <li>
            <a href="http://jquery.com/">jQuery Home</a>
          </li>
          <li>
            <a href="http://jquerymobile.com/">jQuery Mobile</a>
          </li>
          <li>
            <a href="http://jqueryui.com/">jQuery UI</a>
          </li>
          <li>
            <a href="http://learningjquery.com/">Learning jQuery</a>
          </li>
        </ul>
      </li>
      <li>
        <a class="menu_trigger" href="#">HTML5 Resources</a>
        <ul class="menu">
          <li>
            <a href="http://diveintohtml5.org/">Dive Into HTML5</a>
          </li>
          <li>
            <a href="http://www.alistapart.com/articles/get-ready-for-html-5/">Get Ready for HTML5</a>
          </li>
          <li>
            <a href="http://html5boilerplate.com/">HTML5 Boilerplate</a>
          </li>
          <li>
            <a href="http://html5doctor.com/">HTML5 Doctor</a>
          </li>
          <li>
            <a href="http://html5.org/">HTML5 Intro</a>
          </li>
          <li>
            <a href="http://www.zeldman.com/superfriends/">HTML5 Super Friends</a>
          </li>
        </ul>
      </li>
      <li>
        <a class="menu_trigger" href="#">Code</a>
        <ul class="menu">
          <li>
            <a href="resources/desktop/assets/css/desktop.css">Desktop - CSS</a>
          </li>
          <li>
            <a href="resources/desktop/assets/js/jquery.desktop.js">Desktop - JavaScript</a>
          </li>
          <li>
            <a href="http://github.com/nathansmith/jQuery-Desktop">GitHub Repository</a>
          </li>
        </ul>
      </li>
      <li>
        <a class="menu_trigger" href="#">Credits</a>
        <ul class="menu">
          <li>
            <a href="http://sonspring.com/journal/jquery-desktop">Demo built by Nathan Smith</a>
          </li>
          <li>
            <a href="http://adrian-rodriguez.net/">Wallpaper by Adrian Rodriguez</a>
          </li>
          <li>
            <a href="http://tango.freedesktop.org/Tango_Desktop_Project">Icons - Tango Desktop Project</a>
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
        <a href="#window_computer">
          <img src="resources/desktop/assets/images/icons/plugin-edit-icon.png" />
          Service Plugins
        </a>
      </li>
    </ul>
    <a class="float_right" href="http://www.firehost.com/?ref=spon_nsmith_desktop-sonspring" title="Secure Hosting">
      <img src="resources/desktop/assets/images/misc/firehost.png" />
    </a>
  </div>
</div>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.3/jquery.min.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js"></script>
<script src="resources/desktop/assets/js/jquery.form.js"></script>
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