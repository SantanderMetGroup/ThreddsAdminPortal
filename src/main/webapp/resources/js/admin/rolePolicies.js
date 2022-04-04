$(document).ready(function(){
	
	/*
	 * 
	 * Event handlers
	 * 
	 */
	$('#selectPolicy').change(function(){
    	var policySelected = $(this).find(":selected").val();
    	$("#selectPolicy option:selected").attr('enabled', 'enabled');
    	$.getJSON("../admin/getPolicyInfo?policyName="+policySelected, null, function (data) {
    		$('#selectedPolicies').append('<div class="col-xs-6 col-sm-4 col-md-4 group-thumbnail" data-policy-name="'+data.policyName+'"><div class="thumbnail"><i class="fa fa-cog fa-3x" style="display:block;text-align:center"></i><div class="caption"><h4>'+data.policyName+'</h4><p>'+data.description+'</p><p><button class="btn btn-primary btn-xs btn-remove"><i class="glyphicon glyphicon-remove"></i> Remove</button></p></div></div></div>');
        });
    });
	
	$('#selectedPolicies').on('click', '.btn-remove', function(){
		var policyName =  $(this).closest('.group-thumbnail').data('policy-name');
		$("#selectPolicy option[value=" + policyName + "]").removeAttr('disabled');
		$(this).closest('.group-thumbnail').remove();
    	$('#selectPolicy').val('');
	});
	
	$('#rolePolicies').on('click', '.btn-remove', function(){
		var thumbnail =  $(this).closest('.group-thumbnail');
    	var policyName = $(thumbnail).data('policy-name');
    	$("#selectPolicy option[value=" + policyName + "]").removeAttr('disabled');
    	$.ajax({
    	    url: 'role/'+roleName+'/policy/'+policyName,
    	    type: 'DELETE',
    	    success: function(result) {
    			$(thumbnail).remove();
    	    }
    	}); 
    });
	
   $("button.btn-save").on('click', function(){
		var selectedPolicies = [];
		$('#selectedPolicies .group-thumbnail').each(function(){
    		var policyName =  $(this).data('policy-name');
    		selectedPolicies.push(policyName);
		});
		
		var jsonObject = {
				selectedPolicies: selectedPolicies
		};

    	$.ajax({
    		type: "POST",
    		url: "rolePolicies?roleName="+roleName,
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