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
<!-- <script type="text/javascript" src="resources/editor/js/jquery.ui.selectmenu.js"></script> -->
<!-- <script type="text/javascript" src="resources/editor/js/jquery-dynamic-form.js"></script> -->
<script type="text/javascript" src="resources/editor/js/main.js"></script>
<script type="text/javascript" src="resources/editor/js/jquery.tmpl.js"></script>
<script type="text/javascript" src="resources/editor/js/knockout-1.3.0beta.js"></script>
<script type="text/javascript" src="resources/editor/js/knockout.mapping-latest.js"></script>
<script type="text/javascript" src="resources/editor/js/editor.js"></script>

  <script>
 
  $(document).ready(function(){
    $('#switcher').themeswitcher();
  });
 
$(function(){

$("form").form();

});

</script>


<script id='entityDescriptionTemplate' type='text/html'>
<label for="description">Description </label>
{{if codeSystemCatalogEntry.resourceSynopsis != null}}
	<input type="text" name="description" id="description" data-bind="value: csViewModel.codeSystemCatalogEntry.resourceSynopsis.value.content" class="text ui-widget-content ui-corner-all" />
{{else}}
 	<a href="#" data-bind="click: function() { csViewModel.addDescription() }">Add Description</a>
{{/if}}
</script>

<script id='keywordTemplate' type='text/html'>
<input name="keyword" id="keyword" type="text" data-bind="value: keyword"/>
<br></br>
</script>

<script id='keywordDisplayTemplate' type='text/html'>
  <span data-bind="text: keyword"></span>
<br></br>
</script>

</head>

<body>
Select A Theme: <div id="switcher"></div>
<br /><br />


<div id="createChangeSetForm" title="Create ChangeSet">

	<form id="changeSetEditForm" class="ui-widget">
		<fieldset class="ui-widget-content" title="Keywords">
			<legend class="ui-widget-header ui-corner-all">Change Instructions</legend>
			<label for="keyword">Change Instructions </label>
			<input type="text" name="changeInstructions" id="changeInstructions" class="text ui-widget-content ui-corner-all" />
		</fieldset>
	</form>

</div>

<div id="resourceTabs">

	<ul>
		<li><a href="#editCodeSystem">CodeSystems</a></li>		
		<li><a href="#editEntity">Entities</a></li>	
		<li><a href="#mappings">Mappings</a></li>		
		<li><a href="#changeSetTab">Change Sets</a></li>		
	</ul>
	
	<div id="changeSetTab">
		<input type="submit" value="Rollback" onclick="rollbackChangeSet();"/>
		<input type="submit" value="Commit" onclick="commitChangeSet();"/>
		<br></br>
		<input type="submit" value="Create New ChangeSet" onclick="createNewChangeSet();"/>
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
                    Change Instructions
                  </th>
                  <th>
                    Status
                  </th>
                </tr>
              </thead>
            </table>
	</div>

<div id="editCodeSystem">

	<ul>
		<li><a href="#cs-tabs-1">Edit</a></li>
		<li><a href="#cs-tabs-2">Create New</a></li>
		<li><a href="#cs-tabs-4">Search</a></li>
		
	</ul>
	
	<div id="codeSystemEditForm" title="Edit CodeSystem">

		<label id="codeSystemLNameLabel"><b>CodeSystemName:</b> <span data-bind="text: codeSystemCatalogEntry.codeSystemName" ></span></label>
		<br></br>
		<label id="aboutLabel"><b>About:</b> <span data-bind="text: codeSystemCatalogEntry.about"></span></label>
		<br></br>
		<form id="editForm" class="ui-widget">
		
			<fieldset class="ui-widget-content" title="Edit">
				<legend class="ui-widget-header ui-corner-all">Description</legend>
				<div data-bind='template: { name: "entityDescriptionTemplate" }'> </div>
			</fieldset>
			<fieldset id="cs-chooseChangeSetForEditFieldset" class="ui-widget-content" title="ChangeSet">
				<legend class="ui-widget-header ui-corner-all">ChangeSet</legend>
				<label for="cs-edit-choose-changeSetDropdown"><b>ChangeSet:</b></label>
				<select id="cs-edit-choose-changeSetDropdown" name="cs-edit-choose-changeSetDropdown"></select>
			</fieldset>
			
			<fieldset class="ui-widget-content" title="Keywords">
				<legend class="ui-widget-header ui-corner-all">KeyWords</legend>
				<a href="#" data-bind="click: function() { viewModel.addKeyword() }">Add Keyword</a>
				<div data-bind='template: { name: "keywordTemplate", foreach: csViewModel.codeSystemCatalogEntry.keywordList }'></div>
			</fieldset>
		</form>
	
	</div>

	<div id="cs-tabs-1">

		
		<label for="cs-edit-search-changeSetDropdown">ChangeSet: </label>
		<select id="cs-edit-search-changeSetDropdown" 
			name="cs-edit-search-changeSetDropdown" 
			data-getAllFunctionName="onListAllCodeSystems"
			class="AddCurrentOption"></select>
			
		<br></br>	
		
		<label for="cs-editAutocomplete">Search: </label>
		<input id="cs-editAutocomplete"/>
		<input type="submit" id="clearSearch" value="Clear Search" name="clearSearch" onclick="onClearEditSearch()"/>
	
		<br></br>
						
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
	<div id="cs-tabs-2">
	
		<label for="cs-create-changeSetDropdown">ChangeSet: </label>
		<select id="cs-create-changeSetDropdown">
			</select>
			
		<form style="width:50%">
		
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

		<div id="cs-tabs-4">
		
		<label for="cs-search-changeSetDropdown">ChangeSet: </label>
		<select id="cs-search-changeSetDropdown" name="cs-search-changeSetDropdown" class="AddCurrentOption"></select>

		<input id="cs-searchAutocomplete"/>
				
		<br></br>	
						
		<table id="cs-autocompleteTable" class="display">
              <thead>
                <tr>
                  <th>
                    CodeSystem Name
                  </th>
                </tr>
              </thead>
            </table>
	</div>
</div>

<div id="editEntity">

	<ul>
		<li><a href="#ed-tabs-1">Edit</a></li>
		<li><a href="#ed-tabs-2">Create New</a></li>
		<li><a href="#ed-tabs-4">Search</a></li>	
	</ul>
	
	<div id="entityEditForm" title="Edit Entity">

		<label id="entityNamespaceLabel"><b>Entity Namespace:</b> <span data-bind="text: entityDescription.namedEntity.entityID.namespace" ></span></label>
		<br></br>
		<label id="entityNameLabel"><b>Entity Name:</b> <span data-bind="text: entityDescription.namedEntity.entityID.name"></span></label>
		
		<div id="entityEditTabs">
			<ul>
				<li><a href="#ed-designations">Designations</a></li>
				<li><a href="#ed-definitions">Definitions</a></li>	
			</ul>
			<div id="ed-designations">
				<table class="display">
			        <thead>
			            <tr>
			                <th class="ui-state-default" rowspan="1" colspan="1">Description</th>
			                <th class="ui-state-default" rowspan="1" colspan="1">Role</th>
			            </tr>
			        </thead>
			        <tbody data-bind='template: { name: "designationRowTemplate", foreach: entityDescription.namedEntity.designationList }'></tbody>
		   	 	</table>
				<a href="#" data-bind="click: function() { addDesignation() }">Add Description</a>
			</div>
			<div id="ed-definitions">
				<table class="display">
			        <thead>
			            <tr>
			                <th class="ui-state-default" rowspan="1" colspan="1">Definition</th>
			                <th class="ui-state-default" rowspan="1" colspan="1">Role</th>
			            </tr>
			        </thead>
			        <tbody data-bind='template: { name: "definitionRowTemplate", foreach: entityDescription.namedEntity.definitionList }'></tbody>
		   	 	</table>
				<a href="#" data-bind="click: function() { addDefinition() }">Add Definition</a>
			</div>
		</div>
	</div>
	
	<script type="text/html" id="designationRowTemplate">
		<tr>
			<td>
				<input type="text" name="designation" id="designation" data-bind="value: value.content" class="text ui-widget-content ui-corner-all" />
			</td>
			<td>
				<select data-bind="options: ['PREFERRED','ALTERNATIVE','HIDDEN'], selectedOptions: designationRole" >
			</td>
		</tr>
	</script>
	<script type="text/html" id="definitionRowTemplate">
		<tr>
			<td>
				<input type="text" name="definition" id="designation" data-bind="value: value.content" class="text ui-widget-content ui-corner-all" />
			</td>
			<td>
				<select data-bind="options: ['NORMATIVE','INFORMATIVE'], selectedOptions: definitionRole" >
			</td>
		</tr>
	</script>

	<div id="ed-tabs-1">

		
		<label for="ed-edit-search-changeSetDropdown">ChangeSet: </label>
		<select id="ed-edit-search-changeSetDropdown" 
			name="ed-edit-search-changeSetDropdown" 
			data-getAllFunctionName="onListAllEntities"
			class="AddCurrentOption"></select>
			
		<br></br>	
		
		<label for="ed-editAutocomplete">Search: </label>
		<input id="ed-editAutocomplete"/>
		<input type="submit" id="ed-clearSearch" value="Clear Search" name="ed-clearSearch" onclick="onClearEditSearch()"/>
	
		<br></br>
						
		<table id="entityTable" class="display">
              <thead>
                <tr>
                  <th>
                    Entity Namespace
                  </th>
                  <th>
                    Entity Name
                  </th>
                   <th>
                    Description
                  </th>
                   <th>
                    Code System
                  </th>
                   <th>
                    Code System Version
                  </th>
                </tr>
              </thead>
            </table>
	</div>
	<div id="ed-tabs-2">
	
		<label for="ed-create-changeSetDropdown">ChangeSet: </label>
		<select id="ed-create-changeSetDropdown">
			</select>
			
		<form style="width:50%">
		
			<fieldset>
			<legend>Create</legend>
			<table>
			<tr><td>Entity Name </td><td><input id="entityName" name="entityName" type="text"  /></td></tr>
			<tr><td>Entity Namespace </td><td><input id="entityNamespace" name="entityNamespace" type="text"  /></td></tr>
			<tr><td>About </td><td><input id="entityNameAbout" name="entityNameAbout" type="text"  /></td></tr>
	
			
			<tr><td><input type="submit" value="Create" onclick="createEntity();"/></td></tr>
			
			</table>
			</fieldset>
		</form>
	</div>
	</div>

<div id="mappings">

	<table>
		<tr>
	
		<td>
	
		<table id="sourceEntityTable" class="display">
              <thead>
                <tr>
                  <th>
                    Entity Namespace
                  </th>
                  <th>
                    Entity Name
                  </th>
                   <th>
                    Description
                  </th>
                </tr>
              </thead>
            </table>
		</td>
		<td>
		
		</td>
		<td>
		<table id="targetEntityTable" class="display">
              <thead>
                <tr>
                  <th>
                    Entity Namespace
                  </th>
                  <th>
                    Entity Name
                  </th>
                   <th>
                    Description
                  </th>
                </tr>
              </thead>
            </table>
          </td>  
            
            
            </tr>
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
