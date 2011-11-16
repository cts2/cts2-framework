// JavaScript Document
var currentCodeSystemName;
var currentChangeSetUri;

var codeSystemTable;
var entityTable;
var changeSetTable;

var currentJson;

var selectedChangeSetUri;

var csViewModel;
var viewModel;

var urlPrefix = "";

function createNewChangeSet() {
	
	$( "#createChangeSetForm" ).dialog( "open" );	
	
}

function rollbackChangeSet() {
	

				  $.ajax( {
						"dataType": 'json', 
						"contentType": "application/json",
						"type": "DELETE", 
						"url": urlPrefix+"changeset/"+selectedChangeSetUri,
						"error":function (xhr, ajaxOptions, thrownError){
							alert(xhr.status);
							alert(xhr.statusText);
							alert(xhr.responseText);
							alert(jsonString);
						},
						"success": function (json, textStatus, jqXHR) {

							log("DELETE",
									"changeset/"+selectedChangeSetUri,
									"Rolling back the ChangeSet");
							
							changeSetTable.fnReloadAjax();	
							
						}
					} );

}

function commitChangeSet() {
	

	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"type": "POST", 
			"url": urlPrefix+"changeset/"+selectedChangeSetUri,
			"data": "{'updatedState':{'state':'FINAL'}}",
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				alert(jsonString);
			},
			"success": function (json, textStatus, jqXHR) {

				log("POST",
						"changeset/"+selectedChangeSetUri,
						"Committing the ChangeSet");
				
				changeSetTable.fnReloadAjax();	
				
			}
		} );

}

fnChangeSetObjectToArray = function ( )
{
	return function ( sSource, aoData, fnCallback ) {
		$.ajax( {
			"dataType": 'json', 
			"type": "GET", 
			"url": sSource, 
			"data": aoData, 
			"success": function (json) {
				var newJson = new Object();
				newJson.aaData = new Array();
				
				$("[id$=changeSetDropdown]").each(function(){
					$(this).html('');
				});
				
				$(".AddCurrentOption").each(function(){
					$(this).html("<option>CURRENT</option>");
				});
				
				json = json.entryList;
				
				for(i in json){
					newJson.aaData[i] = new Array();
					newJson.aaData[i][0] = json[i].changeSetURI;
					newJson.aaData[i][1] = json[i].creationDate;
					newJson.aaData[i][2] = getChangeInstructions(json[i]);
					newJson.aaData[i][3] = json[i].state;
					
					$("[id$=changeSetDropdown]").each(function(){
						$(this).append( $('<option></option>').val(json[i].changeSetURI).html(json[i].changeSetURI));
					});
				}
			
				fnCallback(newJson);
			}
		} );
	};
};

fnCodeSystemObjectToArray = function ( )
{
	return function ( sSource, aoData, fnCallback ) {
		$.ajax( {
			"dataType": 'json', 
			"type": "GET", 
			"url": sSource, 
			"data": aoData, 
			"success": function (json) {
				var newJson = new Object();
				newJson.aaData = new Array();
				
				json = json.entryList;
				
				for(i in json){
					newJson.aaData[i] = new Array();
					newJson.aaData[i][0] = json[i].codeSystemName;
					newJson.aaData[i][1] = json[i].about;
					newJson.aaData[i][2] = getResourceSynopsis(json[i]);
				}
				
				
				fnCallback(newJson);
			}
		} );
	};
};

fnEntityObjectToArray = function ( )
{
	return function ( sSource, aoData, fnCallback ) {
		$.ajax( {
			"dataType": 'json', 
			"type": "GET", 
			"url": sSource, 
			"data": aoData, 
			"success": function (json) {
				var newJson = new Object();
				newJson.aaData = new Array();
				
				json = json.entryList;
				
				for(i in json){
					newJson.aaData[i] = new Array();
					newJson.aaData[i][0] = json[i].name.namespace;
					newJson.aaData[i][1] = json[i].name.name;
					newJson.aaData[i][2] = getKnownEntityDescription(json[i]);
					newJson.aaData[i][3] = json[i].knownEntityDescriptionList[0].describingCodeSystemVersion.codeSystem.content;
					newJson.aaData[i][4] = json[i].knownEntityDescriptionList[0].describingCodeSystemVersion.version.content;
				}
				
				
				fnCallback(newJson);
			}
		} );
	};
};

function getChangeInstructions(json){
	var instructions = "-- no instructions --";
	
	if(json.changeSetElementGroup != null){
		var opaqueData = json.changeSetElementGroup.changeInstructions;
		
		if(opaqueData != null){
			instructions = opaqueData.value.content;
		}
	}

	return instructions;
}

function getResourceSynopsis(json){
	var synopsis = "-- no description --";
	
	if(json.resourceSynopsis != null){
		synopsis = json.resourceSynopsis.value.content;
	}

	return synopsis;
}

function getKnownEntityDescription(json){
	var description = "-- no description --";

	if(json.knownEntityDescriptionList != null){
		if(json.knownEntityDescriptionList[0] != null){
			if(json.knownEntityDescriptionList[0].designation != null){
				description = json.knownEntityDescriptionList[0].designation;
			}
		}
	}

	return description;
}

function createEditCodeSystemDialog(){
	   $( "#codeSystemEditForm" ).dialog({
			autoOpen: false,
			height: 450,
			width: 550,
			modal: true,
			buttons: {
				"Save!": function() {
	
					var newJson = ko.mapping.toJS(csViewModel);
					
					updateCodingScheme(newJson.codeSystemCatalogEntry);
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				//$(this).dialog('destroy');
			}
		});
}

function createEditEntityDialog(){
	   $( "#entityEditForm" ).dialog({
			autoOpen: false,
			height: 450,
			width: 550,
			modal: true,
			buttons: {
				"Save!": function() {
	
					var newJson = ko.mapping.toJS(viewModel);
					
					updateEntity(newJson);
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				//$(this).dialog('destroy');
			}
		});
}

function adjustArray(array,propertyName){
	for(i in array){
		var value = array[i];
		array[i] = value[propertyName];
	}
	
	return array;
}

function setResourceSynopsis(json,synopsis){
	if(json.resourceSynopsis == null){
		json.resourceSynopsis = new Object();
		json.resourceSynopsis.value = new Object();
		
	}

	json.resourceSynopsis.value.content = synopsis;
}

$(document).ready(function() {
	
	   createEditCodeSystemDialog();
	   createEditEntityDialog();

	   $("#pickChangeSetButton").button();
	   
		$("[id$=edit-search-changeSetDropdown]").each(function(){
			
			   $(this).change(function() {
		
				   var changeSet = $(this).val();	  
				   
				   eval($(this).attr("data-getAllFunctionName") + "('"+changeSet+"')");
			   });
		});

	   
	   $( "#createChangeSetForm" ).dialog({
			autoOpen: false,
			height: 450,
			width: 550,
			modal: true,
			buttons: {
				"Save!": function() {

					var changeInstructions = $( "#changeInstructions" ).val();
					createChangeSet(function(json0, text0, jqXHR0){
						var chgseturl = jqXHR0.getResponseHeader('Location');
						
						updateChangeSetMetadata(chgseturl,changeInstructions, function(){ alert("success!!"); } );
					});
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
				//
			}
		});


	  
	changeSetTable = $('#changeSetTable').dataTable( {
		"sDom": 'R<"H"lfr>t<"F"ip<',
		"bJQueryUI": true,
		"bProcessing": false,
		"bPaginate": true,
		"bFilter": true,
		"sAjaxSource": urlPrefix+'changesets?format=json',
		"fnServerData": fnChangeSetObjectToArray(),
	} );

	codeSystemTable = $('#codeSystemTable').dataTable( {
		"sDom": 'R<"H"lfr>t<"F"ip<',
		"bJQueryUI": true,
		"bProcessing": false,
		"bPaginate": true,
		"bFilter": true,
		"sAjaxSource": urlPrefix+'codesystems?format=json',
		"fnServerData": fnCodeSystemObjectToArray(),
	} );
	
	entityTable = $('#entityTable').dataTable( {
		"sDom": 'R<"H"lfr>t<"F"ip<',
		"bJQueryUI": true,
		"bProcessing": false,
		"bPaginate": true,
		"bFilter": true,
		"sAjaxSource": urlPrefix+'codesystems?format=json',
		"fnServerData": fnEntityObjectToArray(),
	} );
	
	var autocompleteTable = $("#autocompleteTable").dataTable({
		"sDom": 'R<"H"lfr>t<"F"ip<',
		"bJQueryUI": true,
		"bProcessing": false,
		"bPaginate": true,
		"bFilter": true,
		"fnServerData": fnCodeSystemObjectToArray(),
	});
	
	$('#editAutocomplete').keyup(function(){
	    var val = $(this).val();
	    
	    var changeSetUri = $('#changeSetDropdown').val();
	     
	    var query = urlPrefix+"codesystems?filtercomponent=resourceName&matchalgorithm=contains&format=json&matchvalue="+val;	
	    if(changeSetUri != 'CURRENT'){
	    	query += "&changesetcontext="+changeSetUri;
	    }
	    
	    codeSystemTable.fnReloadAjax(query);
	});
	
	$('#searchAutocomplete').keyup(function(){
	    var val = $(this).val();
	    
	    var changeSetUri = $('#searchChangeSetDropdown').val();
	     
	    var query = urlPrefix+"codesystems?filtercomponent=resourceName&matchalgorithm=contains&format=json&matchvalue="+val;	
	    if(changeSetUri != 'CURRENT'){
	    	query += "&changesetcontext='"+changeSetUri+"'";
	    }
	    
	    autocompleteTable.fnReloadAjax(query);
	});
	
	$( "#resourceTabs" ).tabs({
		 selected: 1,     // which tab to start on when page loads
	     select: function(e, ui) {
	    	 var index = ui.index;
	       
	         return true;
	     }
     });

	$( "#editCodeSystem" ).tabs();
	$( "#editEntity" ).tabs();
	$( "#entityEditTabs" ).tabs();
	
	$('#entityTable tbody tr').live('click', function () {
		var aData = entityTable.fnGetData( this );
		
		var changeSetUri = $('#ed-edit-search-changeSetDropdown').val();
		
		$(entityTable.fnSettings().aoData).each(function (){
			$(this.nTr).removeClass('row_selected');
		});

		  $.ajax( {
				"dataType": 'json', 
				"contentType": "application/json",
				"type": "GET", 
				"url": urlPrefix+"codesystem/"+aData[3]+"/version/"+aData[4]+"/entity/"+aData[0]+":"+aData[1]+(changeSetUri == 'CURRENT' ? "" : "?changesetcontext="+changeSetUri),
				"error":function (xhr, ajaxOptions, thrownError){
					alert(xhr.status);
					alert(xhr.statusText);
					alert(xhr.responseText);
					alert(jsonString);
				},
				"success": function (json) {
	
					
					if(viewModel == null){

						viewModel = ko.mapping.fromJS(json);
						
						viewModel.addDesignation = function () {
							viewModel.entityDescription.namedEntity.designationList.push(
									{
										value:{content:ko.observable('--new-description--')},
										designationRole:ko.observable('ALTERNATIVE')
									}			
							);
						};
						viewModel.addDefinition = function () {
							viewModel.entityDescription.namedEntity.definitionList.push(
									{
										value:{content:ko.observable('--new-description--')},
										definitionRole:ko.observable('NORMATIVE')
									}			
							);
						};

						ko.applyBindings(viewModel, document.getElementById('entityEditForm'));
					} else {
						ko.mapping.fromJS(json,viewModel);
					}
	
				   if(changeSetUri == 'CURRENT'){
					   $('#ed-chooseChangeSetForEditFieldset').show();
				   } else {
					   $('#ed-chooseChangeSetForEditFieldset').hide();
					   $('#ed-edit-choose-changeSetDropdown').val(changeSetUri);
				   }
			
				   $( "#entityEditForm" ).dialog( "open" );
					
				}			
					
		  });
	} );
	
	$('#codeSystemTable tbody tr').live('click', function () {
			var aData = codeSystemTable.fnGetData( this );
			
			var changeSetUri = $('#cs-edit-search-changeSetDropdown').val();
			
			$(codeSystemTable.fnSettings().aoData).each(function (){
				$(this.nTr).removeClass('row_selected');
			});

			  $.ajax( {
					"dataType": 'json', 
					"contentType": "application/json",
					"type": "GET", 
					"url": urlPrefix+"codesystem/"+aData[0]+ (changeSetUri == 'CURRENT' ? "" : "?changesetcontext="+changeSetUri),
					"error":function (xhr, ajaxOptions, thrownError){
						alert(xhr.status);
						alert(xhr.statusText);
						alert(xhr.responseText);
						alert(jsonString);
					},
					"success": function (json) {
						
						var mapping = {
							    'keywordList': {
							        create: function(options) {
							            return {keyword:ko.observable(options.data)};
							        }
							    }
							};
						
						if(csViewModel == null){
							csViewModel = ko.mapping.fromJS(json,mapping);
							
							csViewModel.addDescription = function () {
								csViewModel.codeSystemCatalogEntry.resourceSynopsis = {value:{content:ko.observable('--new-description--')}};
						        ko.applyBindings(csViewModel, document.getElementById('codeSystemEditForm'));   
							};
							
							csViewModel.addKeyword = function () {
								csViewModel.codeSystemCatalogEntry.keywordList.push( {keyword:ko.observable('--new-keyword--')} );
							};
							
							
							ko.applyBindings(csViewModel,document.getElementById('codeSystemEditForm'));
						} else {
							ko.mapping.fromJS(json,csViewModel);
						}
	
					   if(changeSetUri == 'CURRENT'){
						   $('#cs-chooseChangeSetForEditFieldset').show();
					   } else {
						   $('#cs-chooseChangeSetForEditFieldset').hide();
						   $('#cs-edit-choose-changeSetDropdown').val(changeSetUri);
					   }
				
					   $( "#codeSystemEditForm" ).dialog( "open" );
						
					}			
						
			  });
		} );
	 
	
	/* Click event handler */
	$('#changeSetTable tbody tr').live('click', function () {
		var aData = changeSetTable.fnGetData( this );
		
		selectedChangeSetUri = aData[0];

		$(changeSetTable.fnSettings().aoData).each(function (){
			$(this.nTr).removeClass('row_selected');
		});
		
		$(this).toggleClass('row_selected');
		
		if(aData[2]){
			$('#activateButton').button('disable');
		} else {
			$('#activateButton').button('enable');
		}
		
		$('#removeButton').button('enable');
	} );
	
});

function create(){
	var csn = $("#csn").val();
	var about = $("#about").val();
	var changeseturi = $("#cs-create-changeSetDropdown").val();
	
	createCodeSystem(csn,about,changeseturi);
}


function createEntity(){
	var name = $("#entityName").val();
	var namespace = $("#entityNamespace").val();
	var about = $("#entityNameAbout").val();
	var changeseturi = $("#ed-create-changeSetDropdown").val();
	
	doCreateEntity(name,namespace,about,changeseturi);
}


function updateChangeSetMetadata(url,changeInstructions,callback){
	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"data": "{'updatedChangeInstructions':{'changeInstructions':{'value':{'content':'"+changeInstructions+"'}}}}",
			"type": "POST", 
			"url": urlPrefix+"./"+url,
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				//alert(jsonString);
			},
			"success":callback
	  });
}

function createChangeSet(callback){
	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"type": "POST", 
			"url": urlPrefix+"changeset",
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				//alert(jsonString);
			},
			"success":callback
	  });
}

function doCreateEntity(name,namespace,about,changeseturi) {
	
	var json = {namedEntity:{
		describingCodeSystemVersion:{
			version:{content:"test"},
			codeSystem:{content:"test"}
		},
		entityTypeList: [
		                  {
		                       "namespace": "ns",
		                       "name": "class"
		                   }

		               ],
		about: about,
		entityID:{name:name, namespace:namespace}}};
	
	var createJson = JSON.stringify(json);
	
	var createFunction = function (json0, text0, jqXHR0) {
		changeSetTable.fnReloadAjax();
		
		var chgseturl = jqXHR0.getResponseHeader('Location');

		  $.ajax( {
				"dataType": 'json', 
				"contentType": "application/json",
				"type": "GET", 
				"url": urlPrefix+"./"+chgseturl,
				"error":function (xhr, ajaxOptions, thrownError){
					alert(xhr.status);
					alert(xhr.statusText);
					alert(xhr.responseText);
					alert(jsonString);
				},
				"success": function (json, textStatus, jqXHR) {
					currentChangeSetUri = json.changeSetURI;
					
					doCreate("entity",createJson,currentChangeSetUri);
					
				}
			} );
	};
	
	if(changeseturi == 'NEW'){
		createChangeSet(createFunction);
	} else {
		doCreate("entity",createJson,changeseturi);
	}
}

function createCodeSystem(csn,about,changeseturi) {
	
			var createFunction = function (json0, text0, jqXHR0) {
				changeSetTable.fnReloadAjax();
				
				var chgseturl = jqXHR0.getResponseHeader('Location');

				  $.ajax( {
						"dataType": 'json', 
						"contentType": "application/json",
						"type": "GET", 
						"url": urlPrefix+"./"+chgseturl,
						"error":function (xhr, ajaxOptions, thrownError){
							alert(xhr.status);
							alert(xhr.statusText);
							alert(xhr.responseText);
							alert(jsonString);
						},
						"success": function (json, textStatus, jqXHR) {
							currentChangeSetUri = json.changeSetURI;
							
							doCreate("codesystem",'{"codeSystemName":"'+csn+'","about":"'+about+'"}',currentChangeSetUri);
							
						}
					} );
			};
			
			if(changeseturi == 'NEW'){
				createChangeSet(createFunction);
			} else {
				doCreate("codesystem",'{"codeSystemName":"'+csn+'","about":"'+about+'"}',changeseturi);
			}
}


function doCreate(url,json,changeSetUri){
	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"type": "POST", 
			"data": json,
			"url": urlPrefix+url+"?changeseturi="+changeSetUri,
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				alert(jsonString);
			},
			"success": function (json, textStatus, jqXHR) {
				var location = jqXHR.getResponseHeader('Location');
				
				log("POST",
						url+"?changeseturi="+currentChangeSetUri,
						"Creating...");
			}
		} );
}

function updateEntity(json) {
	
	  var data = json.entityDescription;

	  data.namedEntity.changeableElementGroup.changeDescription.changeType = "UPDATE";

	  var changeSetUri = $("#ed-edit-search-changeSetDropdown").val();
	  
	  data.namedEntity.changeableElementGroup.changeDescription.containingChangeSet = changeSetUri;

	  var jsonString = $.toJSON(data);

	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"type": "PUT", 
			"url": urlPrefix+json.heading.resourceRoot,
			"data": jsonString,
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				alert(jsonString);
			},
			"success": function (json) {
				log("PUT",
						"entity/??",
						"Updating the Entity");
				
				$( "#entityEditForm" ).dialog( "close" );
				
				entityTable.fnReloadAjax(urlPrefix+"entities?format=json&changesetcontext="+changeSetUri);

			}
		} );

}

function updateCodingScheme(data) {

	  data.changeableElementGroup.changeDescription.changeType = "UPDATE";

	  var changeSetUri = $("#cs-edit-choose-changeSetDropdown").val();
	  
	  data.changeableElementGroup.changeDescription.containingChangeSet = changeSetUri;
	  
	  var newArray = adjustArray(data.keywordList, "keyword");
	  data.keywordList = newArray;
	  
	  var jsonString = $.toJSON(data);
	  

	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"type": "PUT", 
			"url": urlPrefix+"codesystem/"+data.codeSystemName,
			"data": jsonString,
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				alert(jsonString);
			},
			"success": function (json) {
				log("PUT",
						"codesystem/"+data.codeSystemName,
						"Updating the CodeSystem");
				
				$( "#codeSystemEditForm" ).dialog( "close" );
				
				codeSystemTable.fnReloadAjax(urlPrefix+"codesystems?format=json&changesetcontext="+changeSetUri);

			}
		} );

}

function onClearEditSearch(){
	$('editAutocomplete').val("");
	onListAllCodeSystems();
}

function onListAllCodeSystems(changeSetUri){
	var query = urlPrefix+"codesystems?format=json";

	if(changeSetUri != 'CURRENT'){
		query += ("&changesetcontext="+changeSetUri);
	}
	
	$('#editAutocomplete').val('');
	
	codeSystemTable.fnReloadAjax(query);
}

function onListAllEntities(changeSetUri){
	var query = urlPrefix+"entities?format=json";

	if(changeSetUri != 'CURRENT'){
		query += ("&changesetcontext="+changeSetUri);
	}
	
	$('#editAutocomplete').val('');
	
	entityTable.fnReloadAjax(query);
}

function log(method,url,msg){
	var txt = method.concat(" to ",url," - ",msg,"\n");
	$('#logmsgs').val($('#logmsgs').val()+txt); 
	$('#logmsgs').scrollTop($('#logmsgs')[0].scrollHeight);
}

$.fn.dataTableExt.oApi.fnReloadAjax = function ( oSettings, sNewSource, fnCallback, bStandingRedraw )
{
	if ( typeof sNewSource != 'undefined' && sNewSource != null )
	{
		oSettings.sAjaxSource = sNewSource;
	}
	this.oApi._fnProcessingDisplay( oSettings, true );
	var that = this;
	var iStart = oSettings._iDisplayStart;
	
	oSettings.fnServerData( oSettings.sAjaxSource, [], function(json) {
		/* Clear the old information from the table */
		that.oApi._fnClearTable( oSettings );
		
		/* Got the data - add it to the table */
		for ( var i=0 ; i<json.aaData.length ; i++ )
		{
			that.oApi._fnAddData( oSettings, json.aaData[i] );
		}
		
		oSettings.aiDisplay = oSettings.aiDisplayMaster.slice();
		that.fnDraw();
		
		if ( typeof bStandingRedraw != 'undefined' && bStandingRedraw === true )
		{
			oSettings._iDisplayStart = iStart;
			that.fnDraw( false );
		}
		
		that.oApi._fnProcessingDisplay( oSettings, false );
		
		/* Callback user function - for event handlers etc */
		if ( typeof fnCallback == 'function' && fnCallback != null )
		{
			fnCallback( oSettings );
		}
	}, oSettings );
};
