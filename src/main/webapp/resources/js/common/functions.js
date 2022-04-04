/*
 * POST JSON DATA TO THE SERVER
 */
function postData(url, jsonObject){
	showMessageDialog("Saving...");
	$.ajax({
		type: "POST",
		url: url,
		data: JSON.stringify(jsonObject),
		contentType: 'application/json; charset=utf-8',
		dataType: 'json',
		async: false
	}).done(function(){
		$("#dialog").dialog("close");
		showOKDialog("success", true);
		
	}).fail(function(){
		$("#dialog").dialog("close");
		showOKDialog("failed");
	});
}

/*
 * DIALOG FUNCTIONS
 */
function showDialogURLContent(title, URL){
	showDialog(title);
	$('#dialog-content').load(URL);
}

function showDialogURLContentCallback(title, URL, complete){
	showDialog(title);
	$('#dialog-content').load(URL, complete);
}
function showDialog(title){
	$('#dialog').dialog({
		dialogClass: 'custom-dialog', 
	 	height: 650,
    	width: 800,
    	modal: true,
		resizable: true,
		title: title,
		position: { my: "center", at: "center", of: window },
		create: function(){
			var closeButton = $(this).closest(".ui-dialog").find(".ui-dialog-titlebar button");
			closeButton.addClass("btn btn-info");
			closeButton.append('<i class="fa fa-times"></i>');
		},close: function( event, ui ) {
			resetDialog();
		}
	});
}

function showMessageDialog(message){
	$('#dialog-content').empty().append(message);
	$("#dialog").dialog({
		create: function(){
			var closeButton = $(this).closest(".ui-dialog").find(".ui-dialog-titlebar button");
			closeButton.addClass("btn btn-info");
			closeButton.append('<i class="fa fa-times"></i>');
		},
		open: function(event, ui){
			$('#dialog-content').empty().append(message);
		},
		close: function(event, ui){
			resetDialog();
		},
		resizable: false,
		height: 180,
    	width: 300,
    	modal: true,
    	title: 'System message',	
    });
}

function showOKDialog(message){
	showOKDialog(message,false);
}
function showOKDialog(message, reload){
	showMessageDialog(message);
	$("#dialog").dialog({
    	buttons: { 
			Ok: {
			text : 'OK',
			'class' : 'btn btn-default', 
			click: function() {
				if(reload)
					location.reload();
				$( this ).dialog( "close" );
				resetDialog();
				}
			}
		},
    	modal: true
    });
}

function showActionDialog(message, action){
	showMessageDialog(message);
	$("#dialog").dialog({
    	buttons: { 
			'Ok' : {
				text: 'OK',
				'class': 'btn btn-default',
				click: function() {
				     action();
				}
			},
			'Cancel': {
				text: 'Cancel',
				'class': 'btn btn-default',
				click : function(){
					$( this ).dialog( "close" );
					resetDialog();
				}
			}
		}
    });
}

function resetDialog(){
	$('#dialog').remove();
	$('#dialog-wrapper').html('<div id="dialog"><div id="dialog-content"></div></div>');
}

function showAgreementDialog( title, URLContent, functionAccept, functionDeclined){
	showDialogURLContentCallback(title, URLContent, function (){
		if($('#dialog-content').height() > $('#dialog').height()){
			$('#accept-btn').attr("disabled", "disabled");
		 	$('#accept-btn').addClass("btn btn-danger");
		 	$('#accept-btn').removeClass("ui-button ui-widget ui-state-default");
		 	$('#decline-btn').removeClass("ui-button ui-widget");
		 	$('#decline-btn').addClass("btn btn-info");
		}else{
	    	$('#accept-btn').addClass("btn btn-success");
		 	$('#decline-btn').addClass("btn btn-info");
		}
	});
	$('#dialog').dialog({
		buttons: {
			'Accept' : {
				text: 'Accept',
				click: function(){
					$(this).dialog('close');
					functionAccept();
					resetDialog();
					return true;
				},
				id: 'accept-btn'
			},
			'Decline' : {
				text: 'Decline',
				click : function(){
					$(this).dialog('close');
					resetDialog();
			    	functionDeclined();
					return false;
				},
				id: 'decline-btn'
			}
		}
	});
	console.log("Dialog content height: " + $('#dialog-content').height());
	console.log("Dialog height: " + $('#dialog').height());
	 $("#dialog").scroll(function()
	    {
	        var div = $(this);
	        //console.debug("scrollHeight= " + div[0].scrollHeight + " scrollTop= " + div.scrollTop() + " Div height= " + div.height());
	        if (div[0].scrollHeight - div.scrollTop() <= div.height()+20)
	        {
	        	$('#accept-btn').removeAttr("disabled");
	        	$('#accept-btn').removeClass("btn-danger");
	        	$('#accept-btn').addClass("btn-success");
	        }
    });
}

/* USEFUL FUNCTIONS */

/*GET PARAM FROM URL */
function getURLParameter(name) {
    return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
}
