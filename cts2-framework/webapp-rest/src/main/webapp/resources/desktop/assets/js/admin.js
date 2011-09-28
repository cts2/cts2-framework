var selectedPlugin;

fnServerObjectToArray = function ( aElements )
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


function removePlugin(pluginName,removeCallback){
	
	$.ajax( {
		"dataType": 'json', 
		"type": "DELETE", 
		"url": "admin/plugin/"+pluginName, 
		"success": removeCallback
	} );
	
}

function activatePlugin(pluginName,activateCallback){
	
	$.ajax( {
		"dataType": 'json', 
		"type": "PUT", 
		"contentType": "application/json",
		"data": "{'pluginName':'"+pluginName+"'}",
		"url": "admin/plugins/active", 
		"success": activateCallback
	} );
	
}

$(document).ready(function() {
	var oTable = $('#pluginTable').dataTable( {
		"bProcessing": false,
		"bPaginate": false,
		"bFilter": false,
		"sDom": '<"top">rt<"bottom"flp><"clear">',
		"sAjaxSource": 'admin/plugins?format=json',
		"fnServerData": fnServerObjectToArray( [ 'pluginName', 'pluginVersion' ] ),
		"fnRowCallback": function( nRow, aData, iDisplayIndex ) {
	
			$(nRow).addClass('row_selected');

			return nRow;
		},
	} );
	
	$('form').ajaxForm({
        beforeSubmit: function(){alert("starting");},
        success:      function(){alert("ending");}
	});
	
	/* Click event handler */
	$('#pluginTable tbody tr').live('click', function () {
		var aData = oTable.fnGetData( this );
		
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
				function(){oTable.fnReloadAjax();});
	} );
	
	/* Add a click handler for the delete row */
	$('#activateButton').click( function() {
		activatePlugin(
				selectedPlugin[0], 
				function(){
					jAlert('Plugin Successfully Activated.', 'Alert Dialog');
					oTable.fnReloadAjax();});
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


