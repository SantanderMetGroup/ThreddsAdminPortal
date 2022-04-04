$(document).ready(function(){
	
	/*
	 * 
	 * Event handlers
	 * 
	 */
	$('#selectRole').change(function(){
    	var roleSelected = $(this).find(":selected").val();
    	$("#selectRole option:selected").attr('enabled', 'enabled');
    	$.getJSON("../getRoleInfo?rolename="+roleSelected, null, function (data) {
    		$('#selectedRoles').append('<div class="col-xs-6 col-sm-4 col-md-4 group-thumbnail" data-role-name="'+data.roleName+'"><div class="thumbnail"><i class="fa fa-cog fa-3x" style="display:block;text-align:center"></i><div class="caption"><h4>'+data.roleName+'</h4><p>'+data.description+'</p><p><button class="btn btn-primary btn-xs btn-remove"><i class="glyphicon glyphicon-remove"></i> Remove</button></p></div></div></div>');
        });
    });
	
	$('body').on('click', '.btn-remove', function(){
		var roleName =  $(this).closest('.group-thumbnail').data('role-name');
		$("#selectRole option[value=" + roleName + "]").removeAttr('disabled');
		$(this).closest('.group-thumbnail').remove();
    	$('#selectRole').val('');
	});
	
	$('body').on('click', '.btn-remove-auth', function(){
		var thumbnail =  $(this).closest('.group-thumbnail');
    	var roleName = $(thumbnail).data('role-name');
    	$("#selectRole option[value=" + roleName + "]").removeAttr('disabled');
    	$.ajax({
    	    url: 'user/'+username+'/role/'+roleName,
    	    type: 'DELETE',
    	    success: function(result) {
    			$(thumbnail).remove();
    	    }
    	}); 
    });
	
   $("button.btn-save").on('click', function(){
    	
    	var selectedRoles = [];
    	var authorizedRoles = [];
    	var rejectedRoles = [];

    	if($(this).attr('id') === "buttonSaveSelected"){
	    	$('#selectedRoles .group-thumbnail').each(function(){
	        		var roleName =  $(this).data('role-name');
		    		selectedRoles.push(roleName);
	    	});
    	}else if($(this).attr('id') === "buttonSavePending"){
    		$("#authPendingRoles .group-thumbnail").each(function(){
        		var roleName =  $(this).data('role-name');
        		if($(this).find("input[type='radio']:checked").val() === "authorized"){
        			authorizedRoles.push(roleName);
        		}else if($(this).find("input[type='radio']:checked").val() === "rejected"){
        			rejectedRoles.push(roleName);
        		}
        	});
    	}
    	
    	var jsonObject = {
    		selectedRoles: selectedRoles,
    		authorizedRoles: authorizedRoles,
    		rejectedRoles: rejectedRoles
    	};
	    	
    	$.ajax({
    		type: "POST",
    		url: "../admin/user/"+username+"/role",
    		data: JSON.stringify(jsonObject),
    		contentType: 'application/json; charset=utf-8',
    		dataType: 'json',
    		async: false,
    		success: function(msg){
    	    	$("#dialog").dialog( "close" );
    		}
    	});
    });

 	function disableNonEligibleRoles(){
		$("#acceptPendingRoles .group-thumbnail").each(function(){
    		var roleName =  $(this).data('role-name');
			$("#selectRole option[value=" + roleName + "]").attr('disabled', 'disabled');
		});
		$("#authPendingRoles .group-thumbnail").each(function(){
    		var roleName =  $(this).data('role-name');
			$("#selectRole option[value=" + roleName + "]").attr('disabled', 'disabled');
		});
		$("#authorizedRoles .group-thumbnail").each(function(){
    		var roleName =  $(this).data('role-name');
			$("#selectRole option[value=" + roleName + "]").attr('disabled', 'disabled');
		});
		$('#selectRole').val('');
	}
	disableNonEligibleRoles();
	
});