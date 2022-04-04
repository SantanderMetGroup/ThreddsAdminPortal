$(document).ready(function(){

	/*
	 * 
	 * Functions
	 * 
	 * 
	 */
	
	function showDatasetPoliciesDialog(datasetSelected){
		showDialog('Policies of Dataset '+datasetSelected);
		$('#dialog-content').load('rolePoliciesForm?roleName='+datasetSelected);
			
	}
	
	/*
	 * 
	 * EVENT HANDLERS
	 * 
	 * 
	 */
	
	
	$("#acceptancePendingDatasets input:checkbox").on('change',function () {
		var checkbox = $(this);
		$(checkbox).removeAttr("checked");
		var datasetName = $(this).closest('.group-thumbnail').attr('data-dataset-name');
		showAgreementDialog( 'This dataset requires agreement acceptance', 'rolePoliciesForm?roleName='+datasetName, 
				function(){
					$(checkbox).prop('checked', true);
					$(checkbox).parent().find('input[name=policy-declined]:checkbox').prop('checked', false);	
				}, 
				function(){
					$(checkbox).prop('checked', false);
					$(checkbox).parent().find('input[name=policy-declined]:checkbox').prop('checked', true);
				}
		);
	});
	
	
	$('body').on('click', '.btn-show-policies',function(){
		var datasetSelected = $(this).closest('.group-thumbnail').attr('data-dataset-name');
		showDatasetPoliciesDialog(datasetSelected);
	});
    
    
    $("#buttonSave").on('click', function(){
    	var acceptedDatasets = [];
    	var declinedDatasets = [];
    	$("#acceptancePendingDatasets .group-thumbnail").each(function(){
    		var datasetName = $(this).attr('data-dataset-name');
    		if($(this).find('input[name=policy-accepted]:checkbox').is(':checked')){
    			acceptedDatasets.push(datasetName);
    		}else if($(this).find('input[name=policy-declined]:checkbox').is(':checked')){
    			declinedDatasets.push(datasetName);
    		}
    	});
    	
    	var jsonObject = {
    			acceptedDatasets: acceptedDatasets,
        		declinedDatasets: declinedDatasets,
        };
    	postData("datasets?username="+username, jsonObject);
    });
});