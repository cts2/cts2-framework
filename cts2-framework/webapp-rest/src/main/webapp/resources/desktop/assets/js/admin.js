var selectedPlugin;

fnContextConfigPropsObjectToArray = function ( )
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
		
				var i=0;
				for (var key in json) {
				  if (json.hasOwnProperty(key)) {
					  newJson.aaData[i] = new Array();
					  newJson.aaData[i][0] = key;
					  newJson.aaData[i][1] = json[key];
					  i++;
				  }
				}

				fnCallback(newJson);
			}
		} );
	};
};


fnPluginDescriptionObjectToArray = function ( )
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
				
				for(i in json){
					newJson.aaData[i] = new Array();
					newJson.aaData[i][0] = json[i].pluginName;
					newJson.aaData[i][1] = json[i].pluginVersion;
					newJson.aaData[i][2] = json[i].isActive;
				}
				
				
				fnCallback(newJson);
			}
		} );
	};
};


function removePlugin(pluginName,pluginVersion,removeCallback){
	
	$.ajax( {
		"dataType": 'json', 
		"type": "DELETE", 
		"url": "admin/plugin/"+pluginName+"/version/"+pluginVersion, 
		"success": removeCallback
	} );
	
}

function saveCurrentPluginConfig(saveCallback){
	
	$.ajax( {
		"dataType": 'json', 
		"type": "PUT", 
		"contentType": "application/json",
		"data": getPropertiesFromTable(),
		"url": "admin/config/currentplugin", 
		"success": saveCallback
	} );
	
}

function getPropertiesFromTable(){
	var contextConfigTable = $('#contextConfigTable').dataTable();
	
	var aaData = contextConfigTable.fnGetData();
	
	var configProps = new Object();
	
	for(i in aaData){
		var row = aaData[i];
		for(j in row){
			configProps[row[0]] = row[1];
		}
	}
	
	return $.toJSON(configProps);
}

function activatePlugin(pluginName, pluginVersion, activateCallback){
	
	$.ajax( {
		"dataType": 'json', 
		"type": "PUT", 
		"contentType": "application/json",
		"data": "{'pluginName':'"+pluginName+"','pluginVersion':'"+pluginVersion+"'}",
		"url": "admin/plugins/active", 
		"success": activateCallback
	} );
	
}

$(document).ready(function() {
	var pluginsTable = $('#pluginTable').dataTable( {
		"bProcessing": false,
		"bPaginate": false,
		"bFilter": false,
		"sDom": '<"top">rt<"bottom"flp><"clear">',
		"sAjaxSource": 'admin/plugins?format=json',
		"fnServerData": fnPluginDescriptionObjectToArray(),
		"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
	
			$(nRow).addClass('row_selected');

			return nRow;
		},
	} );
	
	var contextConfigTable = $('#contextConfigTable').dataTable( {
		"bProcessing": false,
		"bPaginate": false,
		"bFilter": false,
		"aoColumnDefs":[
		                {"aTargets":[0],"sClass":"readonly"},
		                	],
		"sDom": '<"top">rt<"bottom"flp><"clear">',
		"sAjaxSource": 'admin/config/currentplugin?format=json',
		"fnServerData": fnContextConfigPropsObjectToArray(),
		"fnDrawCallback": function(){
			var t = $('#contextConfigTable').dataTable();
			$('td[class!="readonly sorting_1"]', t.fnGetNodes()).editable(
					function(value, settings){
						handleConfigTableChange();

						var aPos = t.fnGetPosition(this);
						t.fnUpdate( value, aPos[0], aPos[1] );
						
						return value;
					}
			);
		}
	});

	function handleConfigTableChange(){
		//
	}
	
	$('form').ajaxForm({
        success:      function(){
        	pluginsTable.fnReloadAjax();
        	jAlert('Plugin Successfully Uploaded.', 'Alert Dialog');
        }
	});
	
	/* Click event handler */
	$('#pluginTable tbody tr').live('click', function () {
		var aData = pluginsTable.fnGetData( this );
		
		selectedPlugin = aData;
		
		$(this).toggleClass('row_selected');
		
		if(aData[2]){
			$('#activateButton').attr('disabled','disabled');
		} else {
			$('#activateButton').removeAttr('disabled');
		}
		
		$('#removeButton').removeAttr('disabled');
	} );
	
	/* Add a click handler for the delete row */
	$('#removeButton').click( function() {
		removePlugin(
				selectedPlugin[0], 
				function(){pluginsTable.fnReloadAjax();});
	} );
	
	/* Add a click handler for the delete row */
	$('#activateButton').click( function() {
		activatePlugin(
				selectedPlugin[0], 
				selectedPlugin[1], 
				function(){
					jAlert('Plugin Successfully Activated.', 'Alert Dialog');
					pluginsTable.fnReloadAjax();});
	} );
	
	/* Add a click handler for the delete row */
	$('#saveConfigButton').click( function() {
		saveCurrentPluginConfig(
				getPropertiesFromTable(),
				function(){
					jAlert('ConfigurationSaved.', 'Alert Dialog');
					contextConfigTable.fnReloadAjax();
				});
	} );
	
	/* Add a click handler for the delete row */
	$('#resetConfigButton').click( function() {
			contextConfigTable.fnReloadAjax();
	} );

	
	$('#removeButton').attr('disabled','disabled');
	$('#activateButton').attr('disabled','disabled');
	
} );

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


