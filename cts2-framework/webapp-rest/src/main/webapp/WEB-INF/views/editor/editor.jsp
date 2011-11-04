<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet"  type="text/css" href="resources/editor/css/start/jquery-ui-1.8rc2.custom.css"  />
<link type="text/css" rel="stylesheet" href="resources/editor/css/demo_table_jui.css" /> 
<link type="text/css" rel="stylesheet" href="resources/editor/css/editor.css" /> 
<script type="text/javascript" src="resources/editor/js/jquery.js"></script>
<script type="text/javascript" src="resources/editor/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="resources/editor/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="resources/editor/js/jquery.json-2.3.min.js"></script>
<script type="text/javascript" src="resources/editor/js/main.js"></script>
<script type="text/javascript" src="resources/editor/js/editor.js"></script>

  <script>
 
  $(document).ready(function(){
    $('#switcher').themeswitcher();
  });
 
$(function(){

$("form").form();

});

</script>
 
</head>

<body>
Select A Theme: <div id="switcher"></div>
<br /><br />

<div id="codeSystemEditForm" title="Edit CodeSystem">
	
	<label id="codeSystemLNameLabel"><b>CodeSystemName:</b> <span id="codeSystemNameText"></span></label>
	<br></br>
	<label id="aboutLabel"><b>About:</b> <span id="aboutText"></span></label>
	<br></br>
<!-- 	<label id="changeSetUriLabel"><b>ChangeSet:</b> <span id="changeSetUriText"></span></label> -->
<!-- 	<br></br> -->
	<form>
	<fieldset>
		<label for="description">Description </label>
		<input type="text" name="description" id="description" class="text ui-widget-content ui-corner-all" />
	</fieldset>

	</form>
</div>

<div id="tabs">
	<ul>
		<li><a href="#tabs-1">Edit</a></li>
		<li><a href="#tabs-2">Create New</a></li>
		<li><a href="#tabs-3">Change Sets</a></li>
	</ul>
	<div id="tabs-1">

		<select id="changeSetDropdown" name="changeSetDropdown"></select>
		
		<input type="button" id="listCodeSystems" value="List CodeSystems" name="listCodeSystems"/>
				
		<table id="codeSystemTable" class="display">
              <thead>
                <tr>
                  <th>
                    CodeSystem Name
                  </th>
                  <th>
                    About
                  </th>
                   <th>
                    Description
                  </th>
                </tr>
              </thead>
            </table>
	</div>
	<div id="tabs-2">
		<select id="codeSystemChangeSetDropdown" name="codeSystemChangeSetDropdown"></select>
			
		<form id="themeform"  style="margin-left:200px;width:50%">
		
			<fieldset>
			<legend>Create</legend>
			<table>
			<tr><td>Code System Name </td><td><input id="csn" name="csn" type="text"  /></td></tr>
			<tr><td>About </td><td><input id="about" name="about" type="text"  /></td></tr>
			
			<tr><td><input type="submit" value="Create" onclick="create();"/></td></tr>
			
			</table>
			</fieldset>
		</form>
	</div>
	<div id="tabs-3">
		<input type="submit" value="Rollback" onclick="rollback();"/>
		<input type="submit" value="Commit" onclick="commit();"/>
	   	<table id="changeSetTable" class="display">
              <thead>
                <tr>
                  <th>
                    ChangeSetUri
                  </th>
                  <th>
                    Creation Date
                  </th>
                  <th>
                    Status
                  </th>
                </tr>
              </thead>
            </table>
	</div>
</div>

<textarea class="ui-widget ui-state-default ui-corner-all" id="logmsgs" readonly="readonly" rows="5">
</textarea> 

<script type="text/javascript"
  src="http://jqueryui.com/themeroller/themeswitchertool/">
</script>


</body>
</html>
