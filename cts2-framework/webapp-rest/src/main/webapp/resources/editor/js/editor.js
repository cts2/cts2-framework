// JavaScript Document
var currentCodeSystemName;
var currentChangeSetUri;

var codeSystemTable;
var changeSetTable;

var currentJson;

var selectedChangeSetUri;

function rollbackChangeSet() {
	

				  $.ajax( {
						"dataType": 'json', 
						"contentType": "application/json",
						"type": "DELETE", 
						"url": "changeset/"+selectedChangeSetUri,
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
			"url": "changeset/"+selectedChangeSetUri,
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
				
				$('#changeSetDropdown').html("<option>CURRENT</option>");
				$('saveAsChangeSetDropdown').html("<option>NEW</option>");
				$('#csChangeSetDropdown').html('<option>NEW</option>');
				$('#searchChangeSetDropdown').html('<option>CURRENT</option>');
				
				json = json.entryList;
				
				for(i in json){
					newJson.aaData[i] = new Array();
					newJson.aaData[i][0] = json[i].changeSetURI;
					newJson.aaData[i][1] = json[i].creationDate;
					newJson.aaData[i][2] = json[i].state;
					
					$('#changeSetDropdown').append( $('<option></option>').val(json[i].changeSetURI).html(json[i].changeSetURI));
					$('#csChangeSetDropdown').append( $('<option></option>').val(json[i].changeSetURI).html(json[i].changeSetURI));
					$('#searchChangeSetDropdown').append( $('<option></option>').val(json[i].changeSetURI).html(json[i].changeSetURI));
					$('#saveAsChangeSetDropdown').append( $('<option></option>').val(json[i].changeSetURI).html(json[i].changeSetURI));

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

function getResourceSynopsis(json){
	var synopsis = "-- no description --";
	
	if(json.resourceSynopsis != null){
		synopsis = json.resourceSynopsis.value.content;
	}

	return synopsis;
}

function setResourceSynopsis(json,synopsis){
	if(json.resourceSynopsis == null){
		json.resourceSynopsis = new Object();
		json.resourceSynopsis.value = new Object();
		
	}

	json.resourceSynopsis.value.content = synopsis;
}

$(document).ready(function() {
	
	   $("#saveAsChangeSetDropdown").hide();
	   
	   $("#pickChangeSetButton").button();
	   
	   $('#changeSetDropdown').change(function() {
		   var changeSet = $('#changeSetDropdown').val();
		   if(changeSet == 'CURRENT'){
			   $("#saveAsChangeSetDropdown").show();
		   } else {
			   $("#saveAsChangeSetDropdown").hide();
		   }
	   });
	
	   $( "#codeSystemEditForm" ).dialog({
			autoOpen: false,
			height: 450,
			width: 550,
			modal: true,
			buttons: {
				"Save!": function() {
					var csn = $("#codeSystemNameText").text();
					var about = $("#aboutText").text();
					var des = $("#description").val();
	
					updateCodingScheme(csn,about,des);
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
		"sAjaxSource": 'changesets?format=json',
		"fnServerData": fnChangeSetObjectToArray(),
	} );

	codeSystemTable = $('#codeSystemTable').dataTable( {
		"sDom": 'R<"H"lfr>t<"F"ip<',
		"bJQueryUI": true,
		"bProcessing": false,
		"bPaginate": true,
		"bFilter": true,
		"sAjaxSource": 'codesystems?format=json',
		"fnServerData": fnCodeSystemObjectToArray(),
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
	     
	    var query = "codesystems?filtercomponent=resourceName&matchalgorithm=contains&format=json&matchvalue="+val;	
	    if(changeSetUri != 'CURRENT'){
	    	query += "&changesetcontext='"+changeSetUri+"'";
	    }
	    
	    codeSystemTable.fnReloadAjax(query);
	});
	
	$('#searchAutocomplete').keyup(function(){
	    var val = $(this).val();
	    
	    var changeSetUri = $('#searchChangeSetDropdown').val();
	     
	    var query = "codesystems?filtercomponent=resourceName&matchalgorithm=contains&format=json&matchvalue="+val;	
	    if(changeSetUri != 'CURRENT'){
	    	query += "&changesetcontext='"+changeSetUri+"'";
	    }
	    
	    autocompleteTable.fnReloadAjax(query);
	});
	
	$( "#tabs" ).tabs();
	 
	$('#codeSystemTable tbody tr').live('click', function () {
			var aData = codeSystemTable.fnGetData( this );
			
			$(codeSystemTable.fnSettings().aoData).each(function (){
				$(this.nTr).removeClass('row_selected');
			});
		
			$('#codeSystemNameText').text(aData[0]);
			$('#aboutText').text(aData[1]);
			$('#description').val(aData[2]);

		
			$( "#codeSystemEditForm" ).dialog( "open" );
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
	var changeseturi = $("#csChangeSetDropdown").val();
	
	createCodeSystem(csn,about,changeseturi);
}

function populateFormFields() {
	$("#csn").val(currentJson.codeSystemName);
	$("#about").val(currentJson.about);
	$("#desc").val();
	$('#changeSetUri').val(currentJson.changeableElementGroup.changeDescription.containingChangeSet);
}

function createChangeSet(callback){
	  $.ajax( {
			"dataType": 'jsonp', 
			"contentType": "application/json",
			"type": "POST", 
			"url": "changeset",
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				alert(jsonString);
			},
			"success":callback
	  });
}

function createCodeSystem(csn,about,changeseturi) {
	
			var createFunction = function (json0, text0, jqXHR0) {
				changeSetTable.fnReloadAjax();
				
				var chgseturl = jqXHR0.getResponseHeader('Location');

				  $.ajax( {
						"dataType": 'json', 
						"contentType": "application/json",
						"type": "GET", 
						"url": "./"+chgseturl,
						"error":function (xhr, ajaxOptions, thrownError){
							alert(xhr.status);
							alert(xhr.statusText);
							alert(xhr.responseText);
							alert(jsonString);
						},
						"success": function (json, textStatus, jqXHR) {
							currentChangeSetUri = json.changeSetURI;
							
							doCreate('{"codeSystemName":"'+csn+'","about":"'+about+'"}',currentChangeSetUri);
							
						}
					} );
			};
			
			if(changeseturi == 'NEW'){
				createChangeSet(createFunction);
			} else {
				doCreate('{"codeSystemName":"'+csn+'","about":"'+about+'"}',changeseturi);
			}
}


function doCreate(json,changeSetUri){
	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"type": "POST", 
			"data": json,
			"url": "codesystem?changeseturi="+changeSetUri,
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				alert(jsonString);
			},
			"success": function (json, textStatus, jqXHR) {
				var location = jqXHR.getResponseHeader('Location');
				
				log("POST",
						"codesystem?changeseturi="+currentChangeSetUri,
						"Creating the CodeSystem");
			}
		} );
}

function updateCodingScheme(codeSystemName,about,description,changeSetUri) {

	  var changeSetUri = $("#changeSetDropdown").val();
	  
	  $.ajax( {
			"dataType": 'json', 
			"contentType": "application/json",
			"type": "GET", 
			"url": "codesystem/"+codeSystemName+"?changesetcontext="+changeSetUri,
			"error":function (xhr, ajaxOptions, thrownError){
				alert(xhr.status);
				alert(xhr.statusText);
				alert(xhr.responseText);
				alert(jsonString);
			},
			"success": function (json) {
				log("GET",
						"codesystem/"+codeSystemName+"?changesetcontext="+changeSetUri,
						"Retrieve the CodeSystem prior to updating");
				
				var newjson = json.codeSystemCatalogEntry;
				
				newjson.changeableElementGroup.changeDescription.changeType = "UPDATE";
				
				setResourceSynopsis(newjson,description);
				
				var jsonString = $.toJSON(newjson);

				  $.ajax( {
						"dataType": 'json', 
						"contentType": "application/json",
						"type": "PUT", 
						"url": "codesystem/"+newjson.codeSystemName,
						"data": jsonString,
						"error":function (xhr, ajaxOptions, thrownError){
							alert(xhr.status);
							alert(xhr.statusText);
							alert(xhr.responseText);
							alert(jsonString);
						},
						"success": function (json) {
							log("PUT",
									"codesystem/"+codeSystemName+"?changesetcontext="+changeSetUri,
									"Updating the CodeSystem");
							
							$( "#codeSystemEditForm" ).dialog( "close" );
							
							codeSystemTable.fnReloadAjax("codesystems?format=json&changesetcontext="+changeSetUri);

						}
					} );

			}
		} );

}

function onListAllCodeSystems(){
	var query = "codesystems?format=json";
	var changeSetUri = $("#changeSetDropdown").val();
	if(changeSetUri != 'CURRENT'){
		query += ("&changesetcontext="+changeSetUri);
	}
	
	$('#editAutocomplete').val('');
	
	codeSystemTable.fnReloadAjax(query);
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
