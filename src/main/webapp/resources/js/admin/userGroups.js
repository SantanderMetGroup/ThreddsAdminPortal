$(document).ready(function(){

	/*
	 * 
	 * Event handlers
	 * 
	 */
	$('#selectGroup').change(function(){
    	var groupSelected = $(this).find(":selected").val();
    	$("#selectGroup option:selected").attr('disabled', 'disabled');
    	$('#selectGroup').val('');
    	$.getJSON("../rest/groups/"+groupSelected, null, function (data) {
    		$('#selectedGroups').append('<div class="col-xs-6 col-sm-4 col-md-4 group-thumbnail" data-group-name="'+data.groupName+'"><div class="thumbnail"><i class="fa fa-users fa-3x" style="display:block;text-align:center"></i><div class="caption"><h4>'+data.groupName+'</h4><p>'+data.description+'</p><p><button class="btn btn-primary btn-xs btn-remove"><i class="glyphicon glyphicon-remove"></i> Remove</button></p></div></div></div>');
        });
    });
	
	$('body').on('click', '.btn-remove', function(){
		var groupName =  $(this).closest('.group-thumbnail').data('group-name');
		$("#selectGroup option[value=" + groupName + "]").removeAttr('disabled');
		$(this).closest('.group-thumbnail').remove();
    	$('#selectGroup').val('');
	});

	
	$('body').on('click', '.btn-remove-auth', function(){
		var thumbnail =  $(this).closest('.group-thumbnail');
    	var groupName = $(thumbnail).data('group-name');
    	$("#selectGroup option[value=" + groupName + "]").removeAttr('disabled');
    	$.ajax({
    	    url: 'user/'+username+'/group/'+groupName,
    	    type: 'DELETE',
    	    success: function(result) {
    			$(thumbnail).remove();
    	    }
    	}); 
    });

	
   $("button.btn-save").on('click', function(){
	   var selectedGroups = [];
	   var authorizedGroups = [];
	   var rejectedGroups = [];
	   
	   if($(this).attr('id') === "buttonSaveSelected"){
	    	$('#selectedGroups .group-thumbnail').each(function(){
	        		var groupName =  $(this).data('group-name');
	        		selectedGroups.push(groupName);
	    	});
	   }else if($(this).attr('id') === "buttonSavePending"){
		   $("#authPendingGroups .group-thumbnail").each(function(){
			   var groupName =  $(this).data('group-name');
			   if($(this).find("input[type='radio']:checked").val() === "authorized"){
				   authorizedGroups.push(groupName);
			   }else if($(this).find("input[type='radio']:checked").val() === "rejected"){
				   rejectedGroups.push(groupName);
			   }
		   });
	   }
	 
		var jsonObject = {
				selectedGroups: selectedGroups,
				authorizedGroups: authorizedGroups,
				rejectedGroups: rejectedGroups
		};
		$.ajax({
    		type: "POST",
    		url: "userGroups?username="+username,
    		data: JSON.stringify(jsonObject),
    		contentType: 'application/json; charset=utf-8',
    		dataType: 'json',
    		async: false,
    		success: function(msg){
    	    	$("#dialog").dialog( "close" );
    		}
    	});
	});

	function disableNonEligibleGroups(){
		$("#authPendingGroups .group-thumbnail").each(function(){
    		var groupName =  $(this).data('group-name');
			$("#selectGroup option[value=" + groupName + "]").attr('disabled', 'disabled');
		});
		$("#authorizedGroups .group-thumbnail").each(function(){
    		var groupName =  $(this).data('group-name');
			$("#selectGroup option[value=" + groupName + "]").attr('disabled', 'disabled');
		});
		$('#selectGroup').val('');
	}
	disableNonEligibleGroups();
});