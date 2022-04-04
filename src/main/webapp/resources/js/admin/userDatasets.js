$(document).ready(function(){
	var deletedRoles = [];
	
	$('#roleSelect').change(function(){
		var roleSelected = $(this).find(":selected").val(); 
		if($.inArray(roleSelected, rolesWithPoliciesList) >= 0){ // Requires acceptance: -1 does not exist.  If exists return the position.
			showRolePoliciesDialog(roleSelected);
		}else{
			appendNewRole(roleSelected, false);
			deletedRoles.splice($.inArray(roleSelected, deletedRoles),1);
		}
	});
	
	$('body').on('click', '.bShowPolicy',function(){
		var button = $(this);
		var roleSelected = $(this).parent().find('.role_name').text();
		$('#dialog').dialog({
			open: function(event, ui){
				$('#dialog-content').load('rolePoliciesForm?rolename='+roleSelected);
			},
		 	height: 600,
        	width: 800,
        	modal: true,
			resizable: true,
			title: 'This role requires agreement acceptance',
			buttons: {
				"Accept" : function(){
					$(this).dialog("close");
					$(button).parent().find('input[name=decision]:radio').first().attr("checked", "checked");
					deletedRoles.splice($.inArray(roleSelected, deletedRoles),1);
					return true;
				},
				"Decline" : function(){
					$(this).dialog("close");
					$(button).parent().find('input[name=decision]:radio').last().attr("checked", "checked");
					return false;
				}
			}
		});
    });
	
	function showRolePoliciesDialog(roleSelected){
		$('#dialog').dialog({
			open: function(event, ui){
				$('#dialog-content').load('rolePoliciesForm?rolename='+roleSelected);
			},
		 	height: 600,
        	width: 800,
        	modal: true,
			resizable: true,
			title: 'This role requires agreement acceptance',
			buttons: {
				"Accept" : function(){
					$(this).dialog("close");
					$("#roleSelect option:selected").attr('disabled', 'disabled');
					appendNewRole(roleSelected, true);
					return true;
				},
				"Decline" : function(){
					$(this).dialog("close");
			    	$('#roleSelect').val('');
					return false;
				}
			}
		});
	}
	
	function appendNewRole(roleSelected, needsAcceptance){
		$.getJSON("../getRoleInfo?rolename="+roleSelected, null, function (data) {
			var htmlText = '<div class="role_info"> <button class="roleDelete blue_button">Delete</button> <div class="role_details"> <span class="bold">Name:</span> <span class="role_name">'+data.roleName+'</span><br/><span class="bold">Description:</span> '+data.description+' <br/>';
			if(data.needAdmin)
				htmlText += '<img src="../resources/icons/lock_16px.png" alt="Needs admin authorization">Needs admin authorization</img>';
			$('#selected_roles').append(htmlText + '</div></div>');
		});
	}

    $("#roleSelect").prepend("<option value='' selected='selected'></option>");
    
    $('body').on('click', '.roleDelete',function(){
    	var roleName = $(this).closest('.role_info').find(".role_name").text();
    	$(this).closest('.role_info').remove();
    	$('#roleSelect option[value=' + roleName + ']').removeAttr('disabled');
    	$('#roleSelect').val('');
    	deletedRoles.push(roleName);
    });
    
    function limitSelect(){
    	$("#pendingOfAuthRoles").children('div').each(function(){
    		var roleName = $(this).find(".role_name").text();
    		$("#roleSelect option[value=" + roleName + "]").attr('disabled', 'disabled');
    	});
    	$("#pendingOfAcceptRoles").children('div').each(function(){
    		var roleName = $(this).find(".role_name").text();
    		$("#roleSelect option[value=" + roleName + "]").attr('disabled', 'disabled');
    	});
    	$("#assignedRoles").children('div').each(function(){
    		var roleName = $(this).find(".role_name").text();
    		$("#roleSelect option[value=" + roleName + "]").attr('disabled', 'disabled');
    	});
    	
    }
    limitSelect();
    
    $("#saveButton").on('click', function(){
    	
    	var selectedRoles = [];
    	$("#selected_roles").children('div').each(function(){
    		var roleName = $(this).find(".role_name").text();
    		selectedRoles.push(roleName);
    	});
    	
    	var acceptedRoles = [];
    	$("#pendingOfAcceptRoles").children('div').each(function(){
    		var roleName = $(this).find(".role_name").text();
    		if($(this).find("input[type='radio']:checked").val() == "accepted"){
    			acceptedRoles.push(roleName);
    		}else if($(this).find("input[type='radio']:checked").val() == "declined"){
    			deletedRoles.push(roleName);/*eliminar rol*/
    		}
    	});
    	
    	
    	var jsonObject = {
    		selectedRoles: selectedRoles,
    		acceptedRoles: acceptedRoles,
    		deletedRoles: deletedRoles,
    	};
    	
    	$.ajax({
    		type: "POST",
    		url: "saveRoles?username="+username,
    		data: JSON.stringify(jsonObject),
    		contentType: 'application/json; charset=utf-8',
    		dataType: 'json',
    		async: false
    	}).done(function(){
    		$.ajax({
    		    url: "",
    		    context: document.body,
    		    success: function(s,x){
    		        $(this).html(s);
    	    		$('h3').after('<span class="success">Successfully saved</span>');
    		    }
    		});

    	}).fail(function(){
    		alert("failed");
    	});
    });
});