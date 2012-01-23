<html>
<head>
<title>Plugins</title>
<!-- Combo-handled YUI CSS files: -->

<link rel="stylesheet" type="text/css"
	href="http://yui.yahooapis.com/combo?2.9.0/build/assets/skins/sam/skin.css">
<link rel="stylesheet" type="text/css"
	href="http://yui.yahooapis.com/2.9.0/build/container/assets/skins/sam/container.css">
<link rel="stylesheet" type="text/css"
	href="http://yui.yahooapis.com/2.9.0/build/button/assets/skins/sam/button.css" />
<link rel="stylesheet" type="text/css"
	href="http://yui.yahooapis.com/2.9.0/build/fonts/fonts-min.css" />
<link rel="stylesheet" type="text/css"
	href="resources/css/admin/admin.css" />
<!-- Combo-handled YUI JS files: -->

<script type="text/javascript"
	src="http://yui.yahooapis.com/combo?2.9.0/build/utilities/utilities.js&2.9.0/build/container/container-min.js&2.9.0/build/button/button-min.js&2.9.0/build/datasource/datasource-min.js&2.9.0/build/datatable/datatable-min.js&2.9.0/build/json/json-min.js&2.9.0/build/uploader/uploader-min.js"></script>



</head>
<body class="yui-skin-sam">

	<img class="logo" src="resources/images/dflogo-small.png">
	
	<script type="text/javascript">
	YAHOO.widget.Uploader.SWFURL = "http://yui.yahooapis.com/2.9.0/build/uploader/assets/uploader.swf";
	
	YAHOO.util.Event.addListener(window, "load", function() {
		


	        var myColumnDefs = [
	            {key:"pluginName"}, {key:"description"}, {key:"pluginVersion"}, {key:"isActive"}
	        ];

	        var myDataSource = new YAHOO.util.ScriptNodeDataSource(null);
	        myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSONARRAY;
	        myDataSource.responseSchema = {
	            fields: ["pluginName", "description", "pluginVersion", "isActive"]
	        };

	        var myDataTable = new YAHOO.widget.DataTable("table", myColumnDefs,
	                myDataSource, {
	        			initialRequest:"admin/plugins?format=json&", 
	        			selectionMode:"single"});

	        myDataTable.subscribe("rowMouseoverEvent", myDataTable.onEventHighlightRow); 
	        myDataTable.subscribe("rowMouseoutEvent", myDataTable.onEventUnhighlightRow); 
	        myDataTable.subscribe("rowClickEvent", myDataTable.onEventSelectRow);
	        
	        var deleteButton = new YAHOO.widget.Button("deleteButton");
	        deleteButton.on("click", handleDelete);    		 

		    var activateButton = new YAHOO.widget.Button("activateButton");
			
	        activateButton.on('click', handleActivate);
	        
	        function handleActivate(){
	        	var selected = myDataTable.getSelectedRows();
	        	var rset = myDataTable.getRecordSet();
	        	var plugin = rset.getRecord(selected[0]);
	        	var pluginName = plugin.getData('pluginName')
	        	var pluginVersion = plugin.getData('pluginVersion')
	        	
	        	YAHOO.util.Connect.setDefaultPostHeader(false);
				YAHOO.util.Connect.initHeader("Content-Type","application/json; charset=utf-8");
	        	YAHOO.util.Connect.asyncRequest(
	                    'PUT',
	                    'admin/plugins/active',
	                    {
	                        success: function (o) {
	                            var handleOK = function() {
	                                this.hide();
	                            };

	                            var alert = new YAHOO.widget.SimpleDialog("alert", {
	                                visible:false,
	                                width: '20em', 
	                                close: false,
	                                fixedcenter: true,
	                                modal: false,
	                                draggable: true,
	                                constraintoviewport: true, 
	                                icon: YAHOO.widget.SimpleDialog.ICON_INFO,
	                                buttons: [
	                                    { text: 'OK', handler: handleOK, isDefault: true }
	                                    ]
	                            });
	                            alert.setHeader("Plugin Activated");
	                            alert.setBody("Plugin Activation Successful.");
	                            alert.render('alerts')
	                            alert.show();
	                           refreshPluginTable();
	                        },
	                        failure: function (o) {
	                            alert(o.statusText);
	                        },
	                    },
		                "{'pluginName':'"+pluginName+"','pluginVersion':'0.1.0-SNAPSHOT'}"       
	                );
	        }
	        
	        function handleDelete(){
	        	var selected = myDataTable.getSelectedRows();
	        	var rset = myDataTable.getRecordSet();
	        	var plugin = rset.getRecord(selected[0]);
	        	var pluginName = plugin.getData('pluginName');
	        	
	        	
	
		// Instantiate the Dialog
								var areYouSure = new YAHOO.widget.SimpleDialog(
										"areYouSureDialog",
										{
											width : "300px",
											fixedcenter : true,
											visible : false,
											draggable : false,
											close : true,
											text : "Delete "+ pluginName +" plugin?",
											icon : YAHOO.widget.SimpleDialog.ICON_HELP,
											constraintoviewport : true,
											buttons : [ {
												text : "Yes",
												handler : function(){
													doDelete();
													this.hide();
												},
												isDefault : true
											}, {
												text : "No",
												handler : function(){this.hide();}
											} ]
										});
								areYouSure.setHeader("Are you sure?");

								// Render the Dialog
								areYouSure.render("alerts");
								areYouSure.show();

	    
								function doDelete(){
									YAHOO.util.Connect.asyncRequest('DELETE',
											'admin/plugin/' + pluginName, {
												success : function(o) {
													refreshPluginTable();
												},
												failure : function(o) {
													alert(o.statusText);
												},
											});
								}

	        }
							function refreshPluginTable() {
								myDataTable
										.getDataSource()
										.sendRequest(
												"admin/plugins?format=json",
												myDataTable.onDataReturnInitializeTable,
												myDataTable);
							}

							var onUploadButtonClick = function(e) {
								//the second argument of setForm is crucial,  
								//which tells Connection Manager this is a file upload form  
								YAHOO.util.Connect.setForm('uploadForm', true);

								var uploadHandler = {
									upload : function(o) {
										refreshPluginTable();
									}
								};

								YAHOO.util.Connect.asyncRequest('POST',
										'admin/plugins', uploadHandler);
							};

							YAHOO.util.Event.on('uploadButton', 'click',
									onUploadButtonClick);

						});
	</script>

	<div id="alerts"></div>

	<div id="table"></div>

	<div id="buttons">
		<button id="deleteButton" name="delete" type="button">Delete</button>
		<button id="activateButton" name="activate" type="button">Activate</button>
	</div>

	<br>
	<br>

	<div>
		<form method="post" action="admin/plugins" id="uploadForm"
			enctype="multipart/form-data">
			<input type="file" name="file" /><br>
			<br> <input type="button" id="uploadButton" name="upload"
				value="Upload Plugin" /><br>
		</form>
	</div>

</body>
</html>