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
	
	$('body').on('click', '.btn-remove', function(){
		var thumbnail =  $(this).closest('.group-thumbnail');
    	var roleName = $(thumbnail).data('role-name');
    	$("#selectRole option[value=" + roleName + "]").removeAttr('disabled');
    	$.ajax({
    	    url: 'group/'+groupName+'/role/'+roleName,
    	    type: 'DELETE',
    	    success: function(result) {
    			$(thumbnail).remove();
    	    }
    	}); 
    });
	
   $("button.btn-save").on('click', function(){
		var selectedRoles = [];
		$('#selectedRoles .group-thumbnail').each(function(){
    		var roleName =  $(this).data('role-name');
    		selectedRoles.push(roleName);
		});
		
		var jsonObject = {
				selectedRoles: selectedRoles
		};

    	$.ajax({
    		type: "POST",
    		url: "groupRoles?groupName="+groupName,
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
		$("#groupRoles .group-thumbnail").each(function(){
    		var roleName =  $(this).data('role-name');
			$("#selectRole option[value=" + roleName + "]").attr('disabled', 'disabled');
		});
		$('#selectRole').val('');
	}
	disableNonEligibleRoles();
});

	


		

		

	
	
	



    

    

    
 
