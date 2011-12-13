// JavaScript Document
var currentCodeSystemName;
var currentChangeSetUri;

var codeSystemTable;
var entityTable;
var mapTable;
var mapVersionTable;
var changeSetTable;

var currentJson;

var selectedChangeSetUri;

var mapViewModel;
var mapVersionViewModel;
var csViewModel;
var csvViewModel;
var viewModel;

var urlPrefix = "";

var tab = 0;

var mappingFromCsvEntitiesHref;
var mappingToCsvEntitiesHref;
var currentMappingMapVersion;

var openChangesets = [];

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

function onAfterChangeSetCommit(){
	loadMapDropdowns();
}

function loadMapDropdowns(){
	$.getJSON('maps?format=json', function(data) {
		$('.mapDropDown').html('');
		
		$.each(data.entryList,function(index){
			var txt = data.entryList[index].mapName;
			var option = $('<option>'+txt+'</option>').val(txt);
			
			$('.mapDropDown').append(option);
		});
	});
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
				
				onAfterChangeSetCommit();		
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
				
				$(".changeSetDropdown").each(function(){
					$(this).html('');
				});
				
				openChangesets = [];
				
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

					if(json[i].state == 'OPEN'){
						openChangesets.push(json[i]);
					}

				}
				
				$(".changeSetDropdown").each(function(){
					populateChangeSetDropdown( $(this) );
				});
			
				fnCallback(newJson);
			}
		} );
	};
};

function populateChangeSetDropdown($dropdown){
	for(i in openChangesets){
		$dropdown.append( $('<option></option>').val(openChangesets[i].changeSetURI).html(openChangesets[i].changeSetURI));
	}
}

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

fnMapObjectToArray = function ( )
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
					newJson.aaData[i][0] = json[i].mapName;
					newJson.aaData[i][1] = json[i].about;
					newJson.aaData[i][2] = getResourceSynopsis(json[i]);
				}

				fnCallback(newJson);
			}
		} );
	};
};

fnMapVersionObjectToArray = function ( )
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
					newJson.aaData[i][0] = json[i].mapVersionName;
					newJson.aaData[i][1] = json[i].about;
					newJson.aaData[i][2] = getResourceSynopsis(json[i]);
					newJson.aaData[i][3] = json[i].versionOf.content;
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

function createEditMapDialog(){
	   $( "#mapEditForm" ).dialog({
			autoOpen: false,
			height: 450,
			width: 550,
			modal: true,
			buttons: {
				"Save!": function() {
	
					var newJson = ko.mapping.toJS(mapViewModel);
					
					updateMap(newJson);
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

function createEditMapVersionDialog(){
	   $( "#mapVersionEditForm" ).dialog({
			autoOpen: false,
			height: 450,
			width: 550,
			modal: true,
			buttons: {
				"Save!": function() {
	
					var newJson = ko.mapping.toJS(mapVersionViewModel);
					
					updateMapVersion(newJson);
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

function createEditCodeSystemVersionDialog(){
	   $( "#codeSystemVersionEditForm" ).dialog({
			autoOpen: false,
			height: 450,
			width: 550,
			modal: true,
			buttons: {
				"Save!": function() {
	
					var newJson = ko.mapping.toJS(csvViewModel);
					
					updateCodeSystemVersion(newJson.codeSystemVersionCatalogEntry);
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

function createLoadMapVersionFormDialog(){
	   $( "#loadMapVersionForm" ).dialog({
			autoOpen: false,
			height: 250,
			width: 550,
			modal: true,
			open: function(){
				$.ajax({
					  "url": urlPrefix+"mapversions?format=json",
					  "type": "GET", 
					  "dataType": 'json',
					  success: function(data){ 
						  $('#mapEntryMapVersionDropdown').html('');
						  
						  $.each(data.entryList, function(i){
							  var href = data.entryList[i].href;
							  var $option = "<option data-href='"+href+"'>"+data.entryList[i].mapVersionName+"</option>";
	
							  $('#mapEntryMapVersionDropdown').append( $option );
						  });
					  },
					  failure: function(data){ 
						  alert( "failed" ); 
					  }
				});
			},
			buttons: {
				"Load": function() {
					var href = $('#mapEntryMapVersionDropdown option:selected').data("href");
					
					$.ajax({
						  "url": href,
						  "type": "GET", 
						  "dataType": 'json',
						  success: function(data){ 
						  	$('#sourceMappingName').text(data.mapVersion.fromCodeSystemVersion.version.content);
						  	$('#targetMappingName').text(data.mapVersion.toCodeSystemVersion.version.content);
						  	
						  	mappingFromCsvEntitiesHref = data.mapVersion.fromCodeSystemVersion.version.href + "/entities";
						  	mappingToCsvEntitiesHref = data.mapVersion.toCodeSystemVersion.version.href + "/entities";
						  	
						  	currentMappingMapVersion = data.mapVersion;
						  	
						  	$( '#loadMapVersionForm' ).dialog( "close" );
						  },
						  failure: function(data){ 
							  alert( "failed" ); 
						  }
					});
				},
				Cancel: function() {
					$( this ).dialog( "close" );
				}
			},
			close: function() {
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

function resetForm(formName){
	$('#'+formName).find(":input").val('');
}

function addPortlet(div,style,name,description){
	var html =
	$('<div class="portlet">' +
	'<div class="portlet-header">'+name+'</div>' +
	'<div class="portlet-content">'+description+'</div>');
	
	$(html).addClass( "ui-widget ui-widget-content ui-helper-clearfix ui-corner-all " + style)
		.find( ".portlet-header" )
		.addClass( "ui-widget-header ui-corner-all" )
		.prepend( "<span class='ui-icon ui-icon-minusthick'></span>")
		.end()
		.find( ".portlet-content" ).show();

	$(html).find( ".portlet-header .ui-icon" ).click(function() {
		$( this ).toggleClass( "ui-icon-plusthick" ).toggleClass( "ui-icon-minusthick" );
		$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).toggle();
	});

	div.append($('<li>').append($(html)).append('</li>'));
	
	return $(html);
}


$(document).ready(function() {
	
	$("#trashcan").droppable({
	    accept: ".connectedSourceSortable li, .connectedTargetSortable li",
	    hoverClass: "ui-state-hover",
	    drop: function(ev, ui) {
	        ui.draggable.remove();
	    }
	});


	$('#targetList').sortable({
		connectWith: ".connectedTargetSortable:not(:has(li)), #trashcan"
	}).disableSelection();
	
	$('#sourceList').sortable({
		connectWith: ".connectedSourceSortable:not(:has(li)), #trashcan"  
	}).disableSelection();
	
	//end drag and drop
	
	loadMapDropdowns();
	
	$( "#collapseAll" ).button().click(function() {
		$( ".portlet-header .ui-icon" ).each(function() {
			$( this ).addClass( "ui-icon-plusthick" );
			$( this ).removeClass( "ui-icon-minusthick" );
			$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).hide();
		});
	});
	
	$( "#loadMapVersion" ).button().click(function() {
		$( "#loadMapVersionForm" ).dialog( "open" );
		/*
		var url = $( '#loadMapVersionUrl' ).val();
		
		$.ajax({
			  "url": url+( (url.indexOf("?") >= 0) ? "&" : "?" )+"format=json",
			  "type": "GET", 
			  "dataType": 'jsonp',
			  success: function(data){ 
			  	$('#sourceMappingName').text(data.mapVersion.fromCodeSystemVersion.version.content);
			  	$('#targetMappingName').text(data.mapVersion.toCodeSystemVersion.version.content);
			  	
			  	mappingToCsvEntitiesHref = data.mapVersion.fromCodeSystemVersion.version.href + "/entities";
			  	mappingFromCsvEntitiesHref = data.mapVersion.toCodeSystemVersion.version.href + "/entities";
			  	
			  	currentMappingMapVersion = data.mapVersion;
			  },
			  failure: function(data){ 
				  alert( "failed" ); 
			  }
		});
		*/
	});
	
	$( "#expandAll" ).button().click(function() {
		$( ".portlet-header .ui-icon" ).each(function() {
			$( this ).addClass( "ui-icon-minusthick" );
			$( this ).removeClass( "ui-icon-plusthick" );
			$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).show();
		});
	});
	
	$( "#exandContractButtonSet" ).buttonset();
/*
	$( "#addMapTarget" ).button().click(function() {
			var $newTarget = cloneMapTarget();
			
			$('#mapTargetList').append($newTarget);	
	});
*/	
	$( "#mapVersionFromUrlButton" ).button().click(function(event) {
			event.preventDefault(); 
			var url = $( '#mapVersionFromUrl' ).val();
			
			$.ajax({
				  "url": url+"?format=json",
				  "type": "GET", 
				  "dataType": 'jsonp',
				  success: function(data){ 
				  	$('#mapVersionFromCsvName').val(data.codeSystemVersionCatalogEntry.codeSystemVersionName);
				  	$('#mapVersionFromCsvAbout').val(data.codeSystemVersionCatalogEntry.about);
				  },
				  failure: function(data){ 
					  alert( "failed" ); 
				  }
			});
	});
	
	$( "#mapVersionToUrlButton" ).button().click(function(event) {
		event.preventDefault(); 
		var url = $( '#mapVersionToUrl' ).val();
		
		$.ajax({
			  "url": url+"?format=json",
			  "type": "GET", 
			  "dataType": 'jsonp',
			  success: function(data){ 
			  	$('#mapVersionToCsvName').val(data.codeSystemVersionCatalogEntry.codeSystemVersionName);
			  	$('#mapVersionToCsvAbout').val(data.codeSystemVersionCatalogEntry.about);

			  },
			  failure: function(data){ 
				  alert( "failed" ); 
			  }
		});
	});
	
	$( "#addMapEntry" ).button().click(function() {
			var $newEntry = cloneMapEntry();
			
			$('#mapEntryList').append($newEntry);
	});

	$( '#mapTarget' ).find( ".portlet-header .ui-icon" ).first().click(function() {
		$( this ).toggleClass( "ui-icon-plusthick" ).toggleClass( "ui-icon-minusthick" );
		$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).first().toggle();
	});
	
	$( '#mapEntry' ).find( ".portlet-header .ui-icon" ).first().click(function() {
		$( this ).toggleClass( "ui-icon-plusthick" ).toggleClass( "ui-icon-minusthick" );
		$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).first().toggle();
	});
	
	//source mapping autocomplete

		 var data = $("#mappingSourceSearch").autocomplete({
			 	open: function(event, ui) {
			        $('ul.ui-autocomplete').removeAttr('style').hide();
			    },
				source: function( request, response ) {
					$.ajax({
						url: mappingFromCsvEntitiesHref+"?maxtoreturn=5&format=json&matchvalue="+request.term,
						dataType: "jsonp",
						success: function( data ) {
							var responseArray = [];
							
							$.each(data.entryList, function(index,item){
								responseArray.push( {'name': item.name.name,
													 'namespace': item.name.namespace,
													 'about': item.about,
													 'description': item.knownEntityDescriptionList[0].designation} );
							});
						
							response(responseArray);
						}
					});
				},
		  minLength: 1
		}).data("autocomplete");

		data._renderMenu = function( ul, items ) {
			$('#sourceList').empty();
			   $.each( items, function( index, item ) {
				   var $portlet = addPortlet($('#sourceList'), 'sourceEntities', item.name,item.description);
				   $portlet.data("item",item);
			    });

	    };



		//end source mapping autocomplete
	    
		//target mapping autocomplete

		 var data = $("#mappingTargetSearch").autocomplete({
				source: function( request, response ) {
					$.ajax({
						url: mappingToCsvEntitiesHref+"?maxtoreturn=5&format=json&matchvalue="+request.term,
						dataType: "jsonp",
						success: function( data ) {
							var responseArray = [];
							
							$.each(data.entryList, function(index,item){
								responseArray.push( {'name': item.name.name,
									 'namespace': item.name.namespace,
									 'about': item.about,
									 'description': item.knownEntityDescriptionList[0].designation} );
							});
						
							response(responseArray);
						}
					});
				},
		  minLength: 1
		}).data("autocomplete");

		data._renderMenu = function( ul, items ) {
			$('#targetList').empty();
			   $.each( items, function( index, item ) {
				   var $portlet = addPortlet($('#targetList'), 'targetEntities', item.name, item.description);
				   $portlet.data("item",item);
			    });
	    };



		//end target mapping autocomplete
	
	   createEditCodeSystemDialog();
	   createEditCodeSystemVersionDialog();
	   createEditEntityDialog();
	   createEditMapDialog();
	   createEditMapVersionDialog();
	   createLoadMapVersionFormDialog();

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
						
						updateChangeSetMetadata(chgseturl,changeInstructions, function(){ 
							changeSetTable.fnReloadAjax();	
							
							$( "#createChangeSetForm" ).dialog( "close" );
							
							resetForm("createChangeSetForm");
						} );
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
		"fnServerData": fnChangeSetObjectToArray()
	} );

	codeSystemTable = $('#codeSystemTable').dataTable( {
		"sDom": 'R<"H"lfr>t<"F"ip<',
		"bJQueryUI": true,
		"bProcessing": false,
		"bPaginate": true,
		"bFilter": true,
		"sAjaxSource": urlPrefix+'codesystems?format=json',
		"fnServerData": fnCodeSystemObjectToArray()
	} );
	
	entityTable = $('#entityTable').dataTable( {
		"sDom": 'R<"H"lfr>t<"F"ip<',
		"bJQueryUI": true,
		"bProcessing": false,
		"bPaginate": true,
		"bFilter": true,
		"sAjaxSource": urlPrefix+'entities?format=json',
		"fnServerData": fnEntityObjectToArray()
	} );
	
	mapTable = $('#mapTable').dataTable( {
		"sDom": 'R<"H"lfr>t<"F"ip<',
		"bJQueryUI": true,
		"bProcessing": false,
		"bPaginate": true,
		"bFilter": true,
		"sAjaxSource": urlPrefix+'maps?format=json',
		"fnServerData": fnMapObjectToArray()
	} );
	
	mapVersionTable = $('#mapVersionTable').dataTable( {
		"sDom": 'R<"H"lfr>t<"F"ip<',
		"bJQueryUI": true,
		"bProcessing": false,
		"bPaginate": true,
		"bFilter": true,
		"sAjaxSource": urlPrefix+'mapversions?format=json',
		"fnServerData": fnMapVersionObjectToArray()
	} );
	
	var autocompleteTable = $("#autocompleteTable").dataTable({
		"sDom": 'R<"H"lfr>t<"F"ip<',
		"bJQueryUI": true,
		"bProcessing": false,
		"bPaginate": true,
		"bFilter": true,
		"fnServerData": fnCodeSystemObjectToArray()
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
	
	$.ajax({
		  "url": urlPrefix+"codesystemversions?format=json",
		  "type": "GET", 
		  "dataType": 'json',
		  success: function(data){ 
			  $.each(data.entryList, function(entry){
				  $('#entityCsvSelect').append("<option>"+data.entryList[entry].codeSystemVersionName+"</option>");
			  });
		  },
		  failure: function(data){ 
			  alert( "failed" ); 
		  }
	});
	
	$( "#resourceTabs" ).tabs({
		 selected: 0,     // which tab to start on when page loads
	     select: function(e, ui) {
	    	 var index = ui.index;
	       
	         return true;
	     }
     });

	$( "#editCodeSystem" ).tabs();
	$( "#codesystem" ).tabs();
	$( "#codesystemversion" ).tabs();
	$( "#editEntity" ).tabs();
	$( "#entityEditTabs" ).tabs();
	$( "#mappings" ).tabs();
	$( "#mapping-tabs-map" ).tabs();
	$( "#mapping-tabs-mapversion" ).tabs();
	
	$('.resourceDescriptionTemplate-edit-tabs').tabs();
	
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

							csViewModel = ko.mapping.fromJS(json,mapping);
							
							csViewModel.addDescription = function () {
								csViewModel.codeSystemCatalogEntry.resourceSynopsis = {value:{content:ko.observable('--new-description--')}};
						        ko.applyBindings(csViewModel, document.getElementById('codeSystemEditForm'));   
							};
							
							csViewModel.addFormalName = function () {
								csViewModel.codeSystemCatalogEntry.formalName = ko.observable('--new-formal-name--');
								ko.applyBindings(csViewModel,document.getElementById('codeSystemEditForm'));
							};
							
							csViewModel.addComment = function () {
								csViewModel.codeSystemCatalogEntry.noteList.push( 
										{
											type:ko.observable('NOTE'),
											value:{content:ko.observable('--new-comment--')}
										}
								);
								ko.applyBindings(csViewModel,document.getElementById('codeSystemEditForm'));
							};
							
							csViewModel.addKeyword = function () {
								csViewModel.codeSystemCatalogEntry.keywordList.push( {keyword:ko.observable('--new-keyword--')} );
								ko.applyBindings(csViewModel, document.getElementById('codeSystemEditForm'));   
							};
							
							
							ko.applyBindings(csViewModel,document.getElementById('codeSystemEditForm'));
	
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
	 
	$('#mapTable tbody tr').live('click', function () {
		var aData = mapTable.fnGetData( this );
		
		var changeSetUri = $('#map-edit-search-changeSetDropdown').val();
		
		$(mapTable.fnSettings().aoData).each(function (){
			$(this.nTr).removeClass('row_selected');
		});

		  $.ajax( {
				"dataType": 'json', 
				"contentType": "application/json",
				"type": "GET", 
				"url": urlPrefix+"map/"+aData[0]+ (changeSetUri == 'CURRENT' ? "" : "?changesetcontext="+changeSetUri),
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

						mapViewModel = ko.mapping.fromJS(json,mapping);
						
						mapViewModel.addDescription = function () {
							mapViewModel.map.resourceSynopsis = {value:{content:ko.observable('--new-description--')}};
					        ko.applyBindings(mapViewModel, document.getElementById('mapEditForm'));   
						};
						
						mapViewModel.addKeyword = function () {
							mapViewModel.map.keywordList.push( {keyword:ko.observable('--new-keyword--')} );
							ko.applyBindings(mapViewModel, document.getElementById('mapEditForm'));   
						};
						
						mapViewModel.addComment = function () {
							mapViewModel.map.noteList.push( 
									{
										type:ko.observable('NOTE'),
										value:{content:ko.observable('--new-comment--')}
									}
							);
							ko.applyBindings(mapViewModel,document.getElementById('mapEditForm'));
						};
						
						mapViewModel.addFormalName = function () {
							mapViewModel.map.formalName = ko.observable('--new-formal-name--');
							ko.applyBindings(mapViewModel,document.getElementById('mapEditForm'));
						};

						ko.applyBindings(mapViewModel,document.getElementById('mapEditForm'));

				   if(changeSetUri == 'CURRENT'){
					   $('#map-chooseChangeSetForEditFieldset').show();
				   } else {
					   $('#map-chooseChangeSetForEditFieldset').hide();
					   $('#map-edit-choose-changeSetDropdown').val(changeSetUri);
				   }
			
				   $( "#mapEditForm" ).dialog( "open" );
					
				}			
					
		  });
	} );
	
	$('#mapVersionTable tbody tr').live('click', function () {
		var aData = mapVersionTable.fnGetData( this );
		
		var changeSetUri = $('#mapversion-edit-search-changeSetDropdown').val();
		
		$(mapVersionTable.fnSettings().aoData).each(function (){
			$(this.nTr).removeClass('row_selected');
		});

		  $.ajax( {
				"dataType": 'json', 
				"contentType": "application/json",
				"type": "GET", 
				"url": urlPrefix+"map/"+aData[3]+"/version/"+aData[0]+(changeSetUri == 'CURRENT' ? "" : "?changesetcontext="+changeSetUri),
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

						mapVersionViewModel = ko.mapping.fromJS(json,mapping);
						
						mapVersionViewModel.addDescription = function () {
							mapVersionViewModel.mapVersion.resourceSynopsis = {value:{content:ko.observable('--new-description--')}};
					        ko.applyBindings(mapVersionViewModel, document.getElementById('mapVersionEditForm'));   
						};
						
						mapVersionViewModel.addKeyword = function () {
							mapVersionViewModel.mapVersion.keywordList.push( {keyword:ko.observable('--new-keyword--')} );
							ko.applyBindings(mapVersionViewModel, document.getElementById('mapVersionEditForm'));   
						};
						
						mapVersionViewModel.addComment = function () {
							mapVersionViewModel.mapVersion.noteList.push( 
									{
										type:ko.observable('NOTE'),
										value:{content:ko.observable('--new-comment--')}
									}
							);
							ko.applyBindings(mapVersionViewModel,document.getElementById('mapVersionEditForm'));
						};
						
						mapVersionViewModel.addFormalName = function () {
							mapVersionViewModel.mapVersion.formalName = ko.observable('--new-formal-name--');
							ko.applyBindings(mapVersionViewModel,document.getElementById('mapVersionEditForm'));
						};

						ko.applyBindings(mapVersionViewModel,document.getElementById('mapVersionEditForm'));

				   if(changeSetUri == 'CURRENT'){
					   $('#mapversion-chooseChangeSetForEditFieldset').show();
				   } else {
					   $('#mapversion-chooseChangeSetForEditFieldset').hide();
					   $('#mapversion-edit-choose-changeSetDropdown').val(changeSetUri);
				   }
			
				   $( "#mapVersionEditForm" ).dialog( "open" );
					
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

function createCodeSystem(){
	var csn = $("#csn").val();
	var about = $("#about").val();
	var changeseturi = $("#cs-create-changeSetDropdown").val();
	
	doCreateCodeSystem(csn,about,changeseturi);
}

function createCodeSystemVersion(){
	var csvn = $("#csvn").val();
	var about = $("#csvabout").val();
	var docuri = $("#docuri").val();
	var versionOf = $("#csvversionof").val();
	var changeseturi = $("#csv-create-changeSetDropdown").val();
	
	doCreateCodeSystemVersion(csvn,about,docuri,versionOf,changeseturi);
}

function createEntity(){
	var name = $("#entityName").val();
	var namespace = $("#entityNamespace").val();
	var about = $("#entityNameAbout").val();
	var changeseturi = $("#ed-create-changeSetDropdown").val();
	
	doCreateEntity(name,namespace,about,changeseturi);
}

function createMap(){
	var name = $("#mapName").val();
	var about = $("#mapAbout").val();
	var changeseturi = $("#map-create-changeSetDropdown").val();
	
	doCreateMap(name,about,changeseturi);
}


function createMapVersion(){
	var name = $("#mapVersionName").val();
	var about = $("#mapVersionAbout").val();
	var changeseturi = $("#mapversion-create-changeSetDropdown").val();
	
	var versionof = $('#mapversion-versionof-dropdown').val();
	
	var toCsvName = $('#mapVersionToCsvName').val();
	var toCsvAbout = $('#mapVersionToCsvAbout').val();
	var toCsvHref = $('#mapVersionToUrl').val();
	
	var fromCsvName = $('#mapVersionFromCsvName').val();
	var fromCsvAbout = $('#mapVersionFromCsvAbout').val();
	var fromCsvHref = $('#mapVersionFromUrl').val();
	
	var fromRef = {
			version:{
				content:fromCsvName,
				uri:fromCsvAbout,
				href:fromCsvHref
			}
	};
	
	var toRef = {
			version:{
				content:toCsvName,
				uri:toCsvAbout,
				href:toCsvHref
			}
	};
	
	doCreateMapVersion(name,"CTS2 Development Framework Editor",about,versionof,fromRef,toRef,changeseturi);
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
	
	doCreate(json,"entity",changeseturi);
}

function doCreate(json,url,changeseturi,oncreate) {
	
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
					
					_doCreate(url,createJson,currentChangeSetUri,oncreate);
					
				}
			} );
	};
	
	if(changeseturi == 'NEW'){
		createChangeSet(createFunction);
	} else {
		_doCreate(url,createJson,changeseturi,oncreate);
	}
}

function doCreateCodeSystem(csn,about,changeseturi) {
	
	var json = {"codeSystemName":csn,"about":about};
	
	doCreate(json,"codesystem",changeseturi);
}

function doCreateCodeSystemVersion(csvn,about,docuri,versionOf,changeseturi) {
	
	var json = {
			"codeSystemVersionName":csvn,
			"about":about,
			"documentURI":docuri,
			"sourceAndNotation":{
				"sourceAndNotationDescription":"CTS2 Development Framework Editor"
			},
			"versionOf":{
				"content":versionOf
			}
	};
	
	
	doCreate(json,"codesystemversion",changeseturi);
}

function doCreateMap(mapname,about,changeseturi) {
	
	var json = {"mapName":mapname,"about":about};
	
	doCreate(json,"map",changeseturi);
}

function doCreateMapVersion(
		mapversionname,
		sourcedescription,
		about,
		versionof,
		fromRef,
		toRef,
		changeseturi) {
	
	var json = {
			"mapVersionName":mapversionname,
			"about":about,
			"documentURI":about+"#"+mapversionname,
			"sourceAndNotation": {
				"sourceAndNotationDescription":sourcedescription
			},
			"versionOf":{ content:versionof },
			"fromCodeSystemVersion":fromRef,
			"toCodeSystemVersion":toRef
		};
	
	doCreate(json,"mapversion",changeseturi);
}


function _doCreate(url,json,changeSetUri,oncreate){
	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"type": "POST", 
			"data": json,
			"url": urlPrefix+url+"?changesetcontext="+changeSetUri,
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				alert(jsonString);
			},
			"success": function (json, textStatus, jqXHR) {
				var location = jqXHR.getResponseHeader('Location');
				
				if(oncreate){
					oncreate(location);
				}
				
				log("POST",
						url+"?changesetcontext="+changeSetUri,
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

function updateMap(data) {

	  data.map.changeableElementGroup.changeDescription.changeType = "UPDATE";

	  var changeSetUri = $("#map-edit-choose-changeSetDropdown").val();
	  
	  data.map.changeableElementGroup.changeDescription.containingChangeSet = changeSetUri;
	  
	  var newArray = adjustArray(data.map.keywordList, "keyword");
	  data.map.keywordList = newArray;

	  var jsonString = $.toJSON(data.map);

	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"type": "PUT", 
			"url": urlPrefix+data.heading.resourceRoot,
			"data": jsonString,
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				alert(jsonString);
			},
			"success": function (json) {
				log("PUT",
						"map/??",
						"Updating the Map");
				
				$( "#entityEditForm" ).dialog( "close" );
				
				mapTable.fnReloadAjax(urlPrefix+"maps?format=json&changesetcontext="+changeSetUri);

			}
		} );

}

function updateMapVersion(data) {

	  data.mapVersion.changeableElementGroup.changeDescription.changeType = "UPDATE";

	  var changeSetUri = $("#mapversion-edit-choose-changeSetDropdown").val();
	  
	  data.mapVersion.changeableElementGroup.changeDescription.containingChangeSet = changeSetUri;
	  
	  var newArray = adjustArray(data.mapVersion.keywordList, "keyword");
	  data.mapVersion.keywordList = newArray;

	  var jsonString = $.toJSON(data.mapVersion);

	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"type": "PUT", 
			"url": urlPrefix+data.heading.resourceRoot,
			"data": jsonString,
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				alert(jsonString);
			},
			"success": function (json) {
				log("PUT",
						"map/??",
						"Updating the MapVersion");
				
				$( "#mapVersionEditForm" ).dialog( "close" );
				
				mapVersionTable.fnReloadAjax(urlPrefix+"mapversions?format=json&changesetcontext="+changeSetUri);

			}
		} );

}

function updateMapEntry(data,changeSetUri) {

	  data.entry.changeableElementGroup.changeDescription.changeType = "UPDATE";

	  data.entry.changeableElementGroup.changeDescription.containingChangeSet = changeSetUri;
	  
	  var jsonString = $.toJSON(data.entry);

	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"type": "PUT", 
			"url": urlPrefix+data.heading.resourceRoot,
			"data": jsonString,
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				alert(jsonString);
			},
			"success": function (json) {
				log("PUT",
						"map/??",
						"Updating the Map");
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

				$("#cs-edit-search-changeSetDropdown").val(changeSetUri);
			}
		} );

}

function addTabsToTemplate(elements) {
	$tabs = $(elements[1]).closest('.resourceDescriptionTemplate-edit-tabs').tabs('destroy').tabs();

	$tabs.bind('tabsselect', function(event, ui) {
	    tab = ui.index;

	});
	
	$tabs.tabs('select',tab);

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

function cloneMapEntry(){
	var $newEntry = $( '#mapEntry' ).clone();
	$newEntry.removeAttr("id");

	$newEntry.find( ".portlet-header .ui-icon" ).first().click(function() {
		$( this ).toggleClass( "ui-icon-plusthick" ).toggleClass( "ui-icon-minusthick" );
		$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).first().toggle();
	});
	
	$newEntry.find('.mapTargetList').sortable();
	
	$newEntry.find('.saveMapEntry').button().click(function() {
		var model = $newEntry.data('model');

		var changeSetUri = $newEntry.find('.changeSetDropdown').val();

		if(model.heading){
			var json = ko.mapping.toJS(model);
			
			updateMapEntry(json,changeSetUri);
		} else {
			var json = ko.mapping.toJS(model.entry);
			
			doCreate(json,
					"mapentry",
					changeSetUri, 
					function(location){
						  $.ajax( {
								"dataType": 'json', 
								"contentType": "application/json",
								"type": "GET", 
								"url": urlPrefix+location,
								"success": function (newJson) {
									var newModel = ko.mapping.fromJS(newJson);
									
									$newEntry.data('model', newModel);
									
									ko.applyBindings(newModel, $($newEntry).get(0) );
								}
							} );
					}
			);
		}
	});
	
	var $changeSetDropdown = $newEntry.find('.changeSetDropdown');
	populateChangeSetDropdown( $changeSetDropdown );
	
	$newEntry.find( ".expandAllMapTargets" ).button().click(function() {
		$(".mapTargetPortlet").each(function(){
			$(this).find($( ".portlet-header .ui-icon" )).each(function() {
				$( this ).addClass( "ui-icon-minusthick" );
				$( this ).removeClass( "ui-icon-plusthick" );
				$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).show();
			});
		
		});
	});
	
	$newEntry.find( ".collapseAllMapTargets" ).button().click(function() {
		$(".mapTargetPortlet").each(function(){
			$(this).find($( ".portlet-header .ui-icon" )).each(function() {
				$( this ).addClass( "ui-icon-plusthick" );
				$( this ).removeClass( "ui-icon-minusthick" );
				$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).hide();
			});
		
		});
	});
	

	var json = {
		entry: {
			assertedBy: {
				map: currentMappingMapVersion.versionOf,
				mapVersion:{
					content:currentMappingMapVersion.mapVersionName
				}
			},
			processingRule:"ALL_MATCHES",
			mapSetList:[
			     {
			            processingRule:"ALL_MATCHES",
			 			entryOrder:"1",
			 			mapTargetList:[]
			     }
			]
		}
	};
	
	var model = ko.mapping.fromJS(json);
	
	$newEntry.find( ".addMapTarget" ).button().click(function() {
		var $newTarget = cloneMapTarget(model);
		
		$newEntry.find('.mapTargetList').append($newTarget);	
	});
	
	$newEntry.find('.sourceDrop').sortable({
		connectWith: ".connectedSourceSortable",
		receive: function(event, ui) {
			var data = ui.item.find(".portlet").data("item");
			
			model.entry.mapFrom = {
					name:data.name,
					namespace:data.namespace,
					uri:data.about
			};
		}
	}).disableSelection();
	
	ko.applyBindings(model, $($newEntry).get(0) );

	$newEntry.data('model', model);
	
	return $newEntry;
}

function cloneMapTarget(model){

	
	var $newTarget = $( '#mapTarget' ).clone();
	$newTarget.removeAttr("id");
	
	$newTarget.find( ".portlet-header .ui-icon" ).click(function() {
		$( this ).toggleClass( "ui-icon-plusthick" ).toggleClass( "ui-icon-minusthick" );
		$( this ).parents( ".portlet:first" ).find( ".portlet-content" ).toggle();
	});

	$newTarget.find('.targetDrop').sortable({
		connectWith: ".connectedTargetSortable",
		receive: function(event, ui) {
			var data = ui.item.find(".portlet").data("item");
			
			var mapTarget = {
					name:data.name,
					namespace:data.namespace,
					uri:data.about,
			};
			
			model.entry.mapSetList()[0].mapTargetList.push(
				{ 
					entryOrder:"1",
					mapTo:mapTarget 
				}
			);
		}
	}).disableSelection();
	
	return $newTarget;	
}

function onListAllMaps(changeSetUri){
	_onListAll("maps",mapTable,changeSetUri);
}

function onListAllMapVersions(changeSetUri){
	_onListAll("mapversions",mapVersionTable,changeSetUri);
}

function _onListAll(url,table,changeSetUri){
	var query = urlPrefix+url+"?format=json";

	if(changeSetUri != 'CURRENT'){
		query += ("&changesetcontext="+changeSetUri);
	}

	table.fnReloadAjax(query);
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