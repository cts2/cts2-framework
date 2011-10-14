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
		
				for(var i in json){
					var option = json[i];
					for (var key in option) {
						  newJson.aaData[i] = new Array();
						  newJson.aaData[i][0] = option.optionName;
						  newJson.aaData[i][1] = option.optionValue;
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
		"type": "POST", 
		"contentType": "application/json",
		"data": getPropertiesFromTable(),
		"url": "admin/plugins/currentplugin/properties", 
		"success": saveCallback
	} );
	
}

function saveServiceContextConfig(saveCallback){
	
	$.ajax( {
		"dataType": 'json', 
		"type": "POST", 
		"contentType": "application/json",
		"data": getPropertiesFromTable(),
		"url": "admin/servicecontext/properties", 
		"success": saveCallback
	} );
	
}

function getPropertiesFromTable(){
	var pluginAdminTable = $('#pluginAdminTable').dataTable();
	
	var aaData = pluginAdminTable.fnGetData();
	
	var options = new Array();
	
	for(i in aaData){
		var row = aaData[i];

		var option = new Option();
		option.optionName = row[0];
		option.optionValue = row[1];
			
		options[i] = option;

	}
	
	return $.toJSON(options);
}


function populateServerContextFields(){
	$.ajax( {
		"dataType": 'json', 
		"type": "GET", 
		"contentType": "application/json",
		"url": "admin/servicecontext/properties", 
		"success": function(json){
			for(i in json){
				var option = json[i];
				
				if(option.optionName == 'server.root'){
					 $('input[name="server_url"]').val(option.optionValue);
				}
				if(option.optionName == 'app.name'){
					$('input[name="webapp_name"]').val(option.optionValue);
				}
			}
		}
	} );
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
		"sDom": '<"top">rt<"bottom"flp><"clear">',
		"bProcessing": false,
		"bPaginate": false,
		"bFilter": false,
		"sAjaxSource": 'admin/plugins?format=json',
		"fnServerData": fnPluginDescriptionObjectToArray(),
	} );
	
	
	
	var pluginAdminTable = $('#pluginAdminTable').dataTable( {
		//"sDom": 'R<"H"lfr>t<"F"ip<',
		"sDom": '<"top">rt<"bottom"flp><"clear">',
		//"bJQueryUI": true,
		"bProcessing": false,
		"bPaginate": false,
		"bFilter": false,
		"aoColumnDefs":[
		                {"aTargets":[0],"sClass":"readonly"},
		                	],
		"sAjaxSource": 'admin/plugins/currentplugin/properties?format=json',
		"fnServerData": fnContextConfigPropsObjectToArray(),
		"fnDrawCallback": function(nRow, aData, iDisplayIndex ){
			$(nRow).addClass('row_selected');

			var t = $('#pluginAdminTable').dataTable();
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

	/* Click event handler */
	$('#pluginTable tbody tr').live('click', function () {
		var aData = pluginsTable.fnGetData( this );
		
		selectedPlugin = aData;

		$(pluginsTable.fnSettings().aoData).each(function (){
			$(this.nTr).removeClass('row_selected');
		});
		
		$(this).toggleClass('row_selected');
		
		if(aData[2]){
			$('#activateButton').button('disable')
		} else {
			$('#activateButton').button('enable')
		}
		
		$('#removeButton').button('enable')
	} );
	
	/* Add a click handler for the delete row */
	$('#removeButton').click( function() {
		removePlugin(
				selectedPlugin[0], 
				selectedPlugin[1], 
				function(){
					pluginsTable.fnReloadAjax();
					pluginAdminTable.fnReloadAjax()
				});
	} );
	
	/* Add a click handler for the delete row */
	$('#activateButton').click( function() {
		activatePlugin(
				selectedPlugin[0], 
				selectedPlugin[1], 
				function(){
					jAlert('Plugin Successfully Activated.', 'Alert Dialog');
					pluginsTable.fnReloadAjax();
					pluginAdminTable.fnReloadAjax()
				});
	} );
	
	/* Add a click handler for the delete row */
	$('#saveConfigButton').click( function() {
		saveCurrentPluginConfig(
				getPropertiesFromTable(),
				function(){
					jAlert('ConfigurationSaved.', 'Alert Dialog');
					pluginAdminTable.fnReloadAjax();
				});
	} );
	
	/* Add a click handler for the delete row */
	$('#resetConfigButton').click( function() {
			pluginAdminTable.fnReloadAjax();
	} );
	
	$( "#cancel_web-admin-change_password_button" )
		.button()
			.click(function() {
				$("#changeAdminPasswordForm").validate().resetForm();
	});
	
	$( "#change-web-admin-password-button" )
		.button()
			.click(function() {
				var isValid = $("#changeAdminPasswordForm").valid();
				if(!isValid){
					jAlert('Password cannot be changed. Please fix the indicated errors.', 'Error');
				} else {
					var newPassword = $("#new_password").val();
					$.ajax( {
							"dataType": 'json', 
							"type": "POST", 
							"contentType": "application/json",
							"data": "[{'optionName':'admin.password', 'optionValue':'"+newPassword+"'}]",
							"url": "admin/servicecontext/properties", 
							"success": function(){
								jAlert('Password changed.', 'Success', function(){
									$( "#change-web-admin-password-form" ).dialog( "close" );
								});
							}
						} );
				}	
	});
	
	$.validator.setDefaults({
		submitHandler: function() { alert("submitted!"); },
		highlight: function(input) {
			$(input).addClass("ui-state-highlight");
		},
		unhighlight: function(input) {
			$(input).removeClass("ui-state-highlight");
		}
	});
	
	// validate signup form on keyup and submit
	$("#changeAdminPasswordForm").validate({
		rules: {
			old_password: {
				required: true,
				remote: {
					type: "GET",
					dataType: "json",
					url: "admin/servicecontext/properties",
					dataFilter: function (data,type) {
				
						var oldPassword = $("#old_password").val();
						var json = $.parseJSON(data);
						for(i in json){
							var option = json[i];
							if(option.optionName == 'admin.password' &&
									option.optionValue == oldPassword){
								return "true";
							}
						}
						
						return "false";
					},
					data: null,
				}
			},
			new_password: {
				required: true,
				minlength: 4
			},
			confirm_password: {
				equalTo: "#new_password"
			},
		},

		messages: {
			old_password: {
				remote: "Password Mismatch"
			},
			new_password: {
				required: "Please provide a password",
				minlength: "Your password must be at least 4 characters long"
			},
			confirm_password: {
				equalTo: "Passwords don't match"
			},
		
		}
	});
	
	populateServerContextFields();
	
	$('#changeServerUrlForm').submit(function() { 
	    
	    var value = $("#server_url").val();
	 
		    $.ajax( {
				"dataType": 'json', 
				"type": "POST", 
				"contentType": "application/json",
				"data": "[{'optionName':'server.root', 'optionValue':'"+value+"'}]",
				"url": "admin/servicecontext/properties", 
				"success": function(){
					jAlert('Server URL changed.', 'Success');
					populateServerContextFields();
				}
			} );
	    return false; 
	});
	
	$('#changeWebappNameForm').submit(function() { 
	    
	    var value = $("#webapp_name").val();
	 
		    $.ajax( {
				"dataType": 'json', 
				"type": "POST", 
				"contentType": "application/json",
				"data": "[{'optionName':'app.name', 'optionValue':'"+value+"'}]",
				"url": "admin/servicecontext/properties", 
				"success": function(){
					jAlert('WebApp Name changed.', 'Success');
					populateServerContextFields();
				}
			} );
	    return false; 
	});
	
	$('#tabs').tabs();
	
	$("button, input:submit, input:button").not('#fileUploadButton').button();

	$('#removeButton').button('disable');
	$('#activateButton').button('disable');
	
	$('#uploadForm').submit(function() {
	    $(this).ajaxSubmit(function(){
	    	pluginsTable.fnReloadAjax();
	    	jAlert('Plugin Successfully Uploaded.', 'Alert Dialog');
	    });
	    return false;
	});

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


