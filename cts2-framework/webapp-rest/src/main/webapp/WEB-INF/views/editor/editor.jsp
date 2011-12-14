<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!-- <link rel="stylesheet"  type="text/css" href="resources/editor/css/start/jquery-ui-1.8rc2.custom.css"  /> -->
<link rel="stylesheet" type="text/css"
	href="http://jqueryui.com/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet"
	href="resources/editor/css/demo_table_jui.css" />
<link type="text/css" rel="stylesheet"
	href="resources/editor/css/editor.css" />
<link type="text/css" rel="stylesheet"
	href="resources/editor/css/demo.css" />
<script type="text/javascript" src="resources/editor/js/jquery.js"></script>
<script type="text/javascript"
	src="resources/editor/js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript"
	src="resources/editor/js/jquery.dataTables.min.js"></script>
<script type="text/javascript"
	src="resources/editor/js/jquery.json-2.3.min.js"></script>
<!-- <script type="text/javascript" src="resources/editor/js/jquery.ui.selectmenu.js"></script> -->
<!-- <script type="text/javascript" src="resources/editor/js/jquery-dynamic-form.js"></script> -->
<script type="text/javascript" src="resources/editor/js/main.js"></script>
<script type="text/javascript" src="resources/editor/js/jquery.tmpl.js"></script>
<script type="text/javascript"
	src="resources/editor/js/knockout-1.3.0beta.js"></script>
<script type="text/javascript"
	src="resources/editor/js/knockout.mapping-latest.js"></script>
<script type="text/javascript" src="resources/editor/js/editor.js"></script>

<script>
	$(document).ready(function() {
		$('#switcher').themeswitcher();
	});

	$(function() {

		$("form").form();

	});
</script>

<script id='resourceDescriptionTemplate' type='text/html'>


<div id="tab1">
<fieldset class="ui-widget-content" title="Edit">
<legend class="ui-widget-header ui-corner-all">Formal Name</legend>
<label for="formalName">Formal Name </label>
{{if $data.formalName != null}}
	<input type="text" name="formalName" id="formalName" data-bind="value: formalName" class="text ui-widget-content ui-corner-all" />
{{else}}
 	<a href="#" data-bind="click: function() { $root.addFormalName() }">Add Formal Name</a>
{{/if}}
</fieldset>

<fieldset class="ui-widget-content" title="Edit">
<legend class="ui-widget-header ui-corner-all">Description</legend>
<label for="description">Description </label>
{{if $data.resourceSynopsis != null}}
	<textarea name="description" id="description" data-bind="value: resourceSynopsis.value.content" class="text ui-widget-content ui-corner-all" />
{{else}}
 	<a href="#" data-bind="click: function() { $root.addDescription() }">Add Description</a>
{{/if}}
</fieldset>

<fieldset class="ui-widget-content" title="Keywords">
<legend class="ui-widget-header ui-corner-all">KeyWords</legend>
<a href="#" data-bind="click: function() { $root.addKeyword() }">Add Keyword</a>
<br></br>
{{each $data.keywordList}}
	<input name="keyword" id="keyword" type="text" data-bind="value: keyword"/>
	<br></br>
{{/each}}
</fieldset>
</div>

<div id="tab2">
<fieldset class="ui-widget-content" title="Comments">
<legend class="ui-widget-header ui-corner-all">Comments</legend>
<a href="#" data-bind="click: function() { $root.addComment() }">Add Comment</a>
<br></br>
{{each $data.noteList }}
	<div data-bind="template: { name: 'commentTemplate', data: $value } "/>
	<br></br>
{{/each}}
</fieldset>
</div>

<div id="tab3">
<fieldset class="ui-widget-content" title="Properties">
<legend class="ui-widget-header ui-corner-all">Properties</legend>
<a href="#" data-bind="click: function() { $root.addProperty() }">Add Property</a>
<br></br>
{{each $data.propertyList}}
	
	<br></br>
{{/each}}
</fieldset>
</div>

</script>

<script id='abstractResourceDescriptionTemplate' type='text/html'>
	<span data-bind="template: { name: 'resourceDescriptionTemplate', data: $data, afterRender: addTabsToTemplate }"> </span>
</script>


<script id='commentTemplate' type='text/html'>
	<select data-bind="options: ['CHANGENOTE','EDITORIALNOTE','HISTORYNOTE', 'SCOPENOTE', 'NOTE'], value: type " ></select>
	<textarea data-bind="value: value.content" class="text ui-widget-content ui-corner-all" />
</script>


</head>

<body>
	
	<div>
		<img src="resources/images/dflogo-very-small.png"/>
	</div>

	<div id="createChangeSetForm" title="Create ChangeSet">

		<form id="changeSetEditForm" class="ui-widget">
			<fieldset class="ui-widget-content" title="Keywords">
				<legend class="ui-widget-header ui-corner-all">Change
					Instructions</legend>
				<label for="keyword">Change Instructions </label> 
					<textarea name="changeInstructions" id="changeInstructions"
					class="text ui-widget-content ui-corner-all" ></textarea>
			</fieldset>
		</form>

	</div>

	<div id="resourceTabs" class='verticalTabs'>

		<ul>
			<li><a href="#editCodeSystem">CodeSystems</a></li>
			<li><a href="#editEntity">Entities</a></li>
			<li><a href="#mappings">Mappings</a></li>
			<li><a href="#changeSetTab">Change Sets</a></li>
		</ul>

		<div id="changeSetTab">
			<input type="submit" value="Rollback" onclick="rollbackChangeSet();" />
			<input type="submit" value="Commit" onclick="commitChangeSet();" /> <br></br>
			<input type="submit" value="Create New ChangeSet"
				onclick="createNewChangeSet();" />
			<table id="changeSetTable" class="display">
				<thead>
					<tr>
						<th>ChangeSetUri</th>
						<th>Creation Date</th>
						<th>Change Instructions</th>
						<th>Status</th>
					</tr>
				</thead>
			</table>
		</div>

		<div id="editCodeSystem">
		
			<ul>
				<li><a href="#codesystem">CodeSystem</a></li>
				<li><a href="#codesystemversion">CodeSystemVersion</a></li>
			</ul>
			
			<div id="codesystem">
	
				<ul>
					<li><a href="#cs-tabs-1">Edit</a></li>
					<li><a href="#cs-tabs-2">Create New</a></li>
				</ul>
	
				<div id="codeSystemEditForm" title="Edit CodeSystem">
	
					<label id="codeSystemLNameLabel"><b>CodeSystemName:</b> <span
						data-bind="text: codeSystemCatalogEntry.codeSystemName"></span></label> <br></br>
					<label id="csAboutLabel"><b>About:</b> <span
						data-bind="text: codeSystemCatalogEntry.about"></span></label> <br></br>
						
						
					<fieldset id="cs-chooseChangeSetForEditFieldset"
						class="ui-widget-content" title="ChangeSet">
						<legend class="ui-widget-header ui-corner-all">ChangeSet</legend>
						<label for="cs-edit-choose-changeSetDropdown"><b>ChangeSet:</b></label>
						<select id="cs-edit-choose-changeSetDropdown"
							class="changeSetDropdown"
							name="cs-edit-choose-changeSetDropdown"></select>
					</fieldset>
						
					<form class="ui-widget">
	
						<div class="resourceDescriptionTemplate-edit-tabs">
							<ul>
									<li><a href="#tab1">Overview</a></li>		
									<li><a href="#tab2">Comments</a></li>
									<li><a href="#tab3">Properties</a></li>
							</ul>	
							<span
								data-bind="template: { name: 'abstractResourceDescriptionTemplate', data: codeSystemCatalogEntry, afterRender: addTabsToTemplate } ">
							</span>
						</div>
	
	
					</form>
	
				</div>
	
				<div id="cs-tabs-1">
	
	
					<label for="cs-edit-search-changeSetDropdown">ChangeSet: </label> <select
						id="cs-edit-search-changeSetDropdown"
						name="cs-edit-search-changeSetDropdown"
						data-getAllFunctionName="onListAllCodeSystems"
						class="AddCurrentOption changeSetDropdown"></select> <br></br> <label
						for="cs-editAutocomplete">Search: </label> <input
						id="cs-editAutocomplete" /> <input type="submit" id="clearSearch"
						value="Clear Search" name="clearSearch"
						onclick="onClearEditSearch()" /> <br></br>
	
					<table id="codeSystemTable" class="display">
						<thead>
							<tr>
								<th>CodeSystem Name</th>
								<th>About</th>
								<th>Description</th>
							</tr>
						</thead>
					</table>
				</div>
				<div id="cs-tabs-2">
	
					<label for="cs-create-changeSetDropdown">ChangeSet: </label> <select
						class="changeSetDropdown"
						id="cs-create-changeSetDropdown">
					</select>
	
					<form style="width: 50%">
	
						<fieldset>
							<legend>Create</legend>
							<table>
								<tr>
									<td>Code System Name</td>
									<td><input id="csn" name="csn" type="text" /></td>
								</tr>
								<tr>
									<td>About</td>
									<td><input id="about" name="about" type="text" /></td>
								</tr>
	
	
								<tr>
									<td><input type="submit" value="Create"
										onclick="createCodeSystem();" /></td>
								</tr>
	
							</table>
						</fieldset>
					</form>
				</div>
	
			</div>
			
			<div id="codesystemversion">
	
				<ul>
					<li><a href="#csv-tabs-1">Edit</a></li>
					<li><a href="#csv-tabs-2">Create New</a></li>
				</ul>
	
				<div id="codeSystemVersionEditForm" title="Edit CodeSystemVersion">
	
					<label id="codeSystemVersionNameLabel"><b>CodeSystemName:</b> <span
						data-bind="text: codeSystemVersionCatalogEntry.codeSystemVersionName"></span></label> <br></br>
					<label id="csvAboutLabel"><b>About:</b> <span
						data-bind="text: codeSystemVersionCatalogEntry.about"></span></label> <br></br>
						
						
					<fieldset id="csv-chooseChangeSetForEditFieldset"
						class="ui-widget-content" title="ChangeSet">
						<legend class="ui-widget-header ui-corner-all">ChangeSet</legend>
						<label for="csv-edit-choose-changeSetDropdown"><b>ChangeSet:</b></label>
						<select id="csv-edit-choose-changeSetDropdown"
							class="changeSetDropdown"
							name="csv-edit-choose-changeSetDropdown"></select>
					</fieldset>
						
					<form class="ui-widget">
	
						<div id="cstest-tabs" class="resourceDescriptionTemplate-edit-tabs">
							<ul>
									<li><a href="#tab1">Overview</a></li>		
									<li><a href="#tab2">Comments</a></li>
									<li><a href="#tab3">Properties</a></li>
							</ul>	
							<span
								data-bind="template: { name: 'abstractResourceDescriptionTemplate', data: codeSystemCatalogEntry, afterRender: addTabsToTemplate } ">
							</span>
						</div>
	
	
					</form>
	
				</div>
	
				<div id="csv-tabs-1">
	
	
					<label for="csv-edit-search-changeSetDropdown">ChangeSet: </label> <select
						id="csv-edit-search-changeSetDropdown"
						name="csv-edit-search-changeSetDropdown"
						data-getAllFunctionName="onListAllCodeSystemVersions"
						class="AddCurrentOption changeSetDropdown"></select> <br></br> <label
						for="csv-editAutocomplete">Search: </label> <input
						id="csv-editAutocomplete" /> <input type="submit" id="clearSearch"
						value="Clear Search" name="clearSearch"
						onclick="onClearEditSearch()" /> <br></br>
	
					<table id="codeSystemVersionTable" class="display">
						<thead>
							<tr>
								<th>CodeSystem Name</th>
								<th>CodeSystemVersion Name</th>
								<th>Docuemnt URI</th>
								<th>Description</th>
							</tr>
						</thead>
					</table>
				</div>
				<div id="csv-tabs-2">
	
					<label for="csv-create-changeSetDropdown">ChangeSet: </label> <select
						class="changeSetDropdown"
						id="csv-create-changeSetDropdown">
					</select>
	
					<form style="width: 50%">
	
						<fieldset>
							<legend>Create</legend>
							<table>
								<tr>
									<td>Code System Version Name</td>
									<td><input id="csvn" name="csn" type="text" /></td>
								</tr>
								<tr>
									<td>About</td>
									<td><input id="csvabout" name="csvabout" type="text" /></td>
								</tr>
								<tr>
									<td>Document URI</td>
									<td><input id="docuri" name="docuri" type="text" /></td>
								</tr>
								<tr>
									<td>Version Of</td>
									<td><input id="csvversionof" name="csvversionof" type="text" /></td>
								</tr>
	
								<tr>
									<td><input type="submit" value="Create"
										onclick="createCodeSystemVersion();" /></td>
								</tr>
	
							</table>
						</fieldset>
					</form>
				</div>
	
			</div>
			
		</div>

		<div id="editEntity">

			<ul>
				<li><a href="#ed-tabs-1">Edit</a></li>
				<li><a href="#ed-tabs-2">Create New</a></li>
				<li><a href="#ed-tabs-4">Search</a></li>
			</ul>

			<div id="entityEditForm" title="Edit Entity">

				<label id="entityNamespaceLabel"><b>Entity Namespace:</b> <span
					data-bind="text: entityDescription.namedEntity.entityID.namespace"></span></label>
				<br></br> <label id="entityNameLabel"><b>Entity Name:</b> <span
					data-bind="text: entityDescription.namedEntity.entityID.name"></span></label>
					
				<fieldset id="ed-chooseChangeSetForEditFieldset"
					class="ui-widget-content" title="ChangeSet">
					<legend class="ui-widget-header ui-corner-all">ChangeSet</legend>
					<label for="ed-edit-choose-changeSetDropdown"><b>ChangeSet:</b></label>
					<select id="ed-edit-choose-changeSetDropdown"
						class="changeSetDropdown"
						name="ed-edit-choose-changeSetDropdown"></select>
				</fieldset>

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
							<tbody
								data-bind='template: { name: "designationRowTemplate", foreach: entityDescription.namedEntity.designationList }'></tbody>
						</table>
						<a href="#" data-bind="click: function() { addDesignation() }">Add
							Description</a>
					</div>
					<div id="ed-definitions">
						<table class="display">
							<thead>
								<tr>
									<th class="ui-state-default" rowspan="1" colspan="1">Definition</th>
									<th class="ui-state-default" rowspan="1" colspan="1">Role</th>
								</tr>
							</thead>
							<tbody
								data-bind='template: { name: "definitionRowTemplate", foreach: entityDescription.namedEntity.definitionList }'></tbody>
						</table>
						<a href="#" data-bind="click: function() { addDefinition() }">Add
							Definition</a>
					</div>
				</div>
			</div>

			<script type="text/html" id="designationRowTemplate">
		<tr>
			<td>
				<textarea data-bind="value: value.content" class="text ui-widget-content ui-corner-all" />
			</td>
			<td>
				<select data-bind="options: ['PREFERRED','ALTERNATIVE','HIDDEN'], selectedOptions: designationRole" >
			</td>
		</tr>
	</script>
			<script type="text/html" id="definitionRowTemplate">
		<tr>
			<td>
				<textarea data-bind="value: value.content" class="text ui-widget-content ui-corner-all" />
			</td>
			<td>
				<select data-bind="options: ['NORMATIVE','INFORMATIVE'], value: definitionRole" >
			</td>
		</tr>
	</script>

			<div id="ed-tabs-1">


				<label for="ed-edit-search-changeSetDropdown">ChangeSet: </label> <select
					id="ed-edit-search-changeSetDropdown"
					name="ed-edit-search-changeSetDropdown"
					data-getAllFunctionName="onListAllEntities"
					class="AddCurrentOption changeSetDropdown"></select> <br></br> <label
					for="ed-editAutocomplete">Search: </label> <input
					id="ed-editAutocomplete" /> <input type="submit"
					id="ed-clearSearch" value="Clear Search" name="ed-clearSearch"
					onclick="onClearEditSearch()" /> <br></br>

				<table id="entityTable" class="display">
					<thead>
						<tr>
							<th>Entity Namespace</th>
							<th>Entity Name</th>
							<th>Description</th>
							<th>Code System</th>
							<th>Code System Version</th>
						</tr>
					</thead>
				</table>
			</div>
			<div id="ed-tabs-2">

				<label for="ed-create-changeSetDropdown">ChangeSet: </label> <select
					class="changeSetDropdown"
					id="ed-create-changeSetDropdown">
				</select>

				<form style="width: 50%">

					<fieldset>
						<legend>Create</legend>
						<table>
							<tr>
								<td>Entity Name</td>
								<td><input id="entityName" name="entityName" type="text" /></td>
							</tr>
							<tr>
								<td>Entity Namespace</td>
								<td><input id="entityNamespace" name="entityNamespace"
									type="text" /></td>
							</tr>
							<tr>
								<td>About</td>
								<td><input id="entityNameAbout" name="entityNameAbout"
									type="text" /></td>
							</tr>
							<tr>
								<td>Code System</td>
								<td><input type="text" id="entityCsSelect" name="entityCsSelect">
									</input>
								</td>
							</tr>
							<tr>
								<td>Code System Version</td>
								<td><input type="text" id="entityCsvSelect" name="entityCsvSelect">
									</input>
								</td>
							</tr>


							<tr>
								<td><input type="submit" value="Create"
									onclick="createEntity();" /></td>
							</tr>

						</table>
					</fieldset>
				</form>
			</div>
		</div>

		<div id="mappings">

			<ul>
				<li><a href="#mapping-tabs-map">Map</a></li>
				<li><a href="#mapping-tabs-mapversion">Map Version</a></li>
				<li><a href="#mapping-tabs-maptool">Mapping Tool</a></li>
			</ul>

			<div id="mapping-tabs-map">

				<ul>
					<li><a href="#editMapTab">Edit</a></li>
					<li><a href="#createNewMapTab">Create New</a></li>

				</ul>

				<div id="mapEditForm" title="Edit Map">

					<label id="mapNameLabel"><b>Map Name:</b> <span
						data-bind="text: map.mapName"></span></label> <br></br> <label
						id="aboutLabel"><b>About:</b> <span
						data-bind="text: map.about"></span></label> <br></br>
					<form class="ui-widget">
					
						<div class="resourceDescriptionTemplate-edit-tabs">
							<ul>
									<li><a href="#tab1">Overview</a></li>		
									<li><a href="#tab2">Comments</a></li>
									<li><a href="#tab3">Properties</a></li>
							</ul>	

							<span
								data-bind="template: { name: 'abstractResourceDescriptionTemplate', data: map, afterRender: addTabsToTemplate }">
							</span>
						</div>

						<fieldset id="map-chooseChangeSetForEditFieldset"
							class="ui-widget-content" title="ChangeSet">
							<legend class="ui-widget-header ui-corner-all">ChangeSet</legend>
							<label for="map-edit-choose-changeSetDropdown"><b>ChangeSet:</b></label>
							<select id="map-edit-choose-changeSetDropdown"
								class="changeSetDropdown"
								name="map-edit-choose-changeSetDropdown"></select>
						</fieldset>

					</form>

				</div>

				
				
				<div id="editMapTab">


					<label for="map-edit-search-changeSetDropdown">ChangeSet: </label>
					<select id="map-edit-search-changeSetDropdown"
						name="map-edit-search-changeSetDropdown"
						data-getAllFunctionName="onListAllMaps" class="AddCurrentOption changeSetDropdown"></select>

					<br></br> <label for="map-editAutocomplete">Search: </label> <input
						id="map-editAutocomplete" /> <input type="submit" id="clearSearch"
						value="Clear Search" name="clearSearch"
						onclick="onClearEditSearch()" /> <br></br>

					<table id="mapTable" class="display">
						<thead>
							<tr>
								<th>Map Name</th>
								<th>About</th>
								<th>Description</th>
							</tr>
						</thead>
					</table>
				</div>
				<div id="createNewMapTab">

					<label for="map-create-changeSetDropdown">ChangeSet: </label> <select
						class="changeSetDropdown"
						id="map-create-changeSetDropdown">
					</select>

					<form style="width: 50%">

						<fieldset>
							<legend>Create</legend>
							<table>
								<tr>
									<td>Map Name</td>
									<td><input id="mapName" name="mapName" type="text" /></td>
								</tr>
								<tr>
									<td>About</td>
									<td><input id="mapAbout" name="mapAbout" type="text" /></td>
								</tr>


								<tr>
									<td><input type="submit" value="Create"
										onclick="createMap();" /></td>
								</tr>

							</table>
						</fieldset>
					</form>
				</div>

			</div>

			<div id="mapping-tabs-mapversion">

				<ul>
					<li><a href="#editMapVersionTab">Edit</a></li>
					<li><a href="#createNewMapVersionVersionTab">Create</a></li>
				</ul>
				
				
				<div id="mapVersionEditForm" title="Edit Map Version">

					<label id="mapVersionNameLabel"><b>Map Version Name:</b> <span
						data-bind="text: mapVersion.mapVersionName"></span></label> <br></br> <label
						id="aboutLabel"><b>About:</b> <span
						data-bind="text: mapVersion.about"></span></label> <br></br>
					<form class="ui-widget">
		
						<div class="resourceDescriptionTemplate-edit-tabs">
						<ul>
								<li><a href="#tab1">Overview</a></li>		
								<li><a href="#tab2">Comments</a></li>
								<li><a href="#tab3">Properties</a></li>
								<li><a href="#tab4">To</a></li>
								<li><a href="#tab5">From</a></li>
						</ul>				
						<span data-bind="template: { name: 'abstractResourceDescriptionTemplate', data: mapVersion, afterRender: addTabsToTemplate }"></span>
						
							<div id="tab4">
								<fieldset class="ui-widget-content" title="To">
									
								</fieldset>
							</div>
							<div id="tab5">
								<fieldset class="ui-widget-content" title="From"></fieldset>
							</div>
		
		
							<fieldset id="mapversion-chooseChangeSetForEditFieldset"
								class="ui-widget-content" title="ChangeSet">
								<legend class="ui-widget-header ui-corner-all">ChangeSet</legend>
								<label for="mapversion-edit-choose-changeSetDropdown"><b>ChangeSet:</b></label>
								<select id="mapversion-edit-choose-changeSetDropdown" class="changeSetDropdown"
									name="mapversion-edit-choose-changeSetDropdown"></select>
							</fieldset>
						</div>

					</form>

				</div>

				<div id="editMapVersionTab">


					<label for="mapversion-edit-search-changeSetDropdown">ChangeSet:
					</label> <select id="mapversion-edit-search-changeSetDropdown"
						name="mapversion-edit-search-changeSetDropdown"
						data-getAllFunctionName="onListAllMapVersions"
						class="AddCurrentOption changeSetDropdown"></select> <br></br> <label
						for="mapversion-editAutocomplete">Search: </label> <input
						id="mapversion-editAutocomplete" /> <input type="submit"
						id="clearSearch" value="Clear Search" name="clearSearch"
						onclick="onClearEditSearch()" /> <br></br>

					<table id="mapVersionTable" class="display">
						<thead>
							<tr>
								<th>Map Version Name</th>
								<th>About</th>
								<th>Description</th>
								<th>Version Of</th>
							</tr>
						</thead>
					</table>
				</div>

				<div id="createNewMapVersionVersionTab">

					<label for="mapversion-create-changeSetDropdown">ChangeSet:
					</label> <select id="mapversion-create-changeSetDropdown" class="changeSetDropdown"></select>

					<form style="width: 50%">

						<fieldset>
							<legend>Create</legend>
							<table>
								<tr>
									<td>Map Version Name</td>
									<td><input id="mapVersionName" name="mapVersionName"
										type="text" /></td>
								</tr>
								<tr>
									<td>About</td>
									<td><input id="mapVersionAbout" name="mapVersionAbout"
										type="text" /></td>
								</tr>
								<tr>
									<td>Version Of</td>
									<td><select id="mapversion-versionof-dropdown" class="mapDropDown"></select></td>
								</tr>


							</table>
							
							<fieldset
								class="ui-widget-content" title="From">
								<legend>From</legend>
								<input name="mapVersionFromUrl" id="mapVersionFromUrl" class='urlInput' type="text"/>
											<button id="mapVersionFromUrlButton">Load From</button>
											<br />
											<label for="mapVersionFromCsvName"><b>MapVersion From CSV Name:</b></label>
											<input name="mapVersionFromCsvName" 
												id="mapVersionFromCsvName" 
												type="text"/>
											<br/>
											<label for="mapVersionFromCsvAbout"><b>MapVersion From CSV About:</b></label>
											<input name="mapVersionFromCsvAbout" 
												id="mapVersionFromCsvAbout" 
												type="text"/>
							</fieldset>
							
							<fieldset
								class="ui-widget-content" title="To">
								<legend>To</legend>
								<input name="mapVersionToUrl" id="mapVersionToUrl" class='urlInput' type="text"/>
											<button id="mapVersionToUrlButton">Load From</button>
											<br />
											<label for="mapVersionToCsvName"><b>MapVersion To CSV Name:</b></label>
											<input name="mapVersionToCsvName" 
												id="mapVersionToCsvName" 
												type="text"/>
											<br/>
											<label for="mapVersionToCsvAbout"><b>MapVersion To CSV About:</b></label>
											<input name="mapVersionToCsvAbout" 
												id="mapVersionToCsvAbout" 
												type="text"/>
							</fieldset>
							
							<br/>
							
							<input type="submit" value="Create"
										onclick="createMapVersion();" />
										
						</fieldset>
					</form>
				</div>

			</div>

			<div id="mapping-tabs-maptool">

<!-- 				<div style="padding: 10px 10px"> -->
<!-- 					<span style="width:100%" class="toolbar ui-widget-header ui-corner-all"> -->
<!-- 						Toolbar: -->
<!-- 						<button id="expandAll">expand all</button> -->
<!-- 						<button id="collapseAll">collapse all</button> -->
<!-- 						<span style="float:right"> -->
<!-- 							<button id="loadMapVersion">Load Map Version</button> -->
<!-- 							<input size="125" name="loadMapVersionUrl" id="loadMapVersionUrl" type="text"/> -->
<!-- 						</span> -->
<!-- 					</span> -->
<!-- 				</div> -->


				<table style="width: 100%">
					<tr>
						<td align="center" style="width: 25%"><label
							for="mappingSourceSearch">Search For Mapping Source: </label><span id="sourceMappingName"></span><br></br> <input
							id="mappingSourceSearch" /></td>

						<td style="width: 50%">
							<div style="padding: 10px 10px">
								<span
									class="toolbar ui-widget-header ui-corner-all"> Toolbar:
									<button id="addMapEntry">Add Map Entry</button>
									<span id="exandContractButtonSet">
										<button id="expandAll">expand all</button>
										<button id="collapseAll">collapse all</button>
									</span>
			 						<span style="float:right"> 
										<button id="loadMapVersion">Load Map Version</button>
									</span>
								
								</span>
							</div>
						</td>
						<td align="center" style="width: 25%"><label
							for="mappingSourceSearch">Search For Mapping Targets: </label><span id="targetMappingName"></span><br></br> <input
							id="mappingTargetSearch" /></td>
					</tr>
					<tr>
						<td>
							<ul id="sourceList" style="display: inline" class="droppable connectedSourceSortable"></ul>
						</td>

						<td>
							<ul id="mapEntryList" style="height:500px; overflow: auto">
							<li>
	
							</li>
							</ul>
						</td>
						
						<td>
							<ul id="targetList" style="display: inline" class="droppable connectedTargetSortable"></ul>
						</td>
					</tr>
				</table>

				<img id="trashcan" src="resources/editor/images/trash-icon.png"
					alt="trash" />

			</div>

		</div>

	</div>


	<textarea class="ui-widget ui-state-default ui-corner-all" id="logmsgs"
		readonly="readonly" rows="5">
</textarea>

	<script type="text/javascript"
		src="http://jqueryui.com/themeroller/themeswitchertool/">
		
	</script>

<!-- 	Hidden divs for cloning -->
	<div style="display:none">
		
		
		<div id='mapTarget'
			class="portlet dropPortlet mapTargetPortlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
			<div
				class="portlet-header ui-widget-header ui-corner-all">
				Map Target <span class='ui-icon ui-icon-minusthick'></span>
			</div>
			<div class="portlet-content">
	
				<label>Map Rule: </label>
				<form>
					<input type="text" class="mapRuleText"></input>
				</form>
	
				<ul
					class="targetDrop droppable dropzone connectedTargetSortable"></ul>
			</div>
		</div>
		
				<div id="loadMapVersionForm" title="Load Map Version">

	
					<form class="ui-widget">
						<fieldset>
							<label for="map-edit-choose-changeSetDropdown"><b>Map Version:</b></label>
							<select id="mapEntryMapVersionDropdown" 
								name="mapEntryMapVersionDropdown"></select>
						</fieldset>
					

					</form>

				</div>
		
		
				<div id="mapEntry"
								class="portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
								<div class="portlet-header ui-widget-header ui-corner-all">Map
									Entry <span class='ui-icon ui-icon-minusthick'></span></div>
								<div class="portlet-content">

									<table>
										<tr>
											<td>

												<div
													class="portlet dropPortlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
													<div class="portlet-header ui-widget-header ui-corner-all">Map
														Source</div>
													<div class="portlet-content">


														<ul 
															class="sourceDrop droppable dropzone connectedSourceSortable"></ul>

		   													
													</div>
												</div>

											</td>
											<td>
															

															
												<div style='width: auto'
													class="portlet dropPortlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all">
													<div class="portlet-header ui-widget-header ui-corner-all">Map
														Set</div>
													<div>
														<button class="addMapTarget">Add Map Target</button>
														<button class="expandAllMapTargets">expand all</button>
														<button class="collapseAllMapTargets">collapse all</button>

													</div>
													<div style="padding: 10px 10px;">
														<label>Processing Rule:
														</label> 
															<select class="mapSetProcessingRule" data-bind="options: ['ALL_MATCHES','FIRST_MATCH'], value: entry.processingRule ">
														</select>
													</div>
													<div>

														<div 
															style='height: 200px; overflow: auto'
															class="mapTargetList portlet-content">

														</div>
			
													</div>
												</div>
											</td>

										</tr>
									</table>
								</div>
								
								<span style="width:100%" class="toolbar ui-widget-header ui-corner-all">
								<button class="saveMapEntry">Save Map Entry</button>
									<label>ChangeSet:
									</label> <select class="changeSetDropdown"></select>
								</span>
							</div>
							
	</div>
	
	<div id="switcher"></div>
</body>
</html>
