$(document).ready(function(){
	
	/*
	 * FUNCTIONS
	 * 
	 * 
	 * 
	 */
	
	function disableNonEligibleGroups(){
		$.each($('#authorizedGroups').children(), function(key, value) { 
			$('#selectGroup option[value="'+$(value).attr('data-group-name')+'"]').attr('disabled', 'disabled');
		});
		$.each($('#authPendingGroups').children(), function(key, value) { 
			$('#selectGroup option[value="'+$(value).attr('data-group-name')+'"]').attr('disabled', 'disabled');
		});
	}
	function appendSelectedGroup(groupSelected){
		$.getJSON("../rest/groups/"+groupSelected, null, function (data) {
			var groupName = data.groupName;
			var moderator = data.coordinator;
			var htmlText = '<div class="col-xs-6 col-sm-4 col-md-3 group-thumbnail" data-group-name="'+groupName+'"><div class="thumbnail thumbnail-mosaic"><img src="../resources/img/logos/logo_'+groupName.toLowerCase()+'.png"  alt="Logo '+groupName.toLowerCase()+'"><hr></hr>';
			htmlText += '<div class="caption"><h4>'+groupName.toLowerCase()+' group</h4>';
			htmlText += '<p>'+data.description+'</p>';
			if(moderator != null)
				htmlText += '<p>Moderator: '+data.coordinator+'</p>';
			else
				htmlText += '<p>Moderator: Non-moderate</p>';
			htmlText += '<button class="btn btn-default btn-xs btn-show-datasets"><i class="fa fa-file-code-o"></i> Datasets</button> ';
			htmlText += '<button class="btn btn-default btn-xs btn-show-policies"><i class="fa fa-bookmark-o"></i> Policies</button> ';
			htmlText += '<button class="btn btn-primary btn-xs btn-remove"><i class="fa fa-times"></i> Remove</button>';
			htmlText += '</div></div>';
			var element = $('#selectedGroups').append(htmlText + '</div></div></div>');
			$('#selectGroup option:selected').attr('disabled', 'disabled');
			$('#selectGroup').selectpicker('val','Nothing selected');
			updateSelectedGroupsCounter();
		});
	}
	
	function updateSelectedGroupsCounter(){
		var groupsSelectedCount = $("#selectedGroups").children().length;
		$("#selectedGroups").parent().children("p").effect("highlight", {}, 1500, function(){} );
		$("#selectedGroups").parent().children("p").children().html(groupsSelectedCount);
	}


	function showGroupPoliciesDialog(groupSelected){
		showAgreementDialog( 'This group requires agreement acceptance', 'groupRolePoliciesForm?groupName='+groupSelected, 
				function(){
					appendSelectedGroup(groupSelected, true);	
				}, 
				function(){
					$(checkbox).prop('checked', false);
					$('#roleSelect').val('');
				}
		);
	}
	
	function showGroupPoliciesInfoDialog(groupSelected){
		showDialog('Datasets and Policies of group '+groupSelected);
		$('#dialog-content').load('groupRolePoliciesForm?groupName='+groupSelected);
	}
	
	function showGroupDatasetsInfoDialog(groupSelected){
		showDialog('Datasets of group '+groupSelected);
		$('#dialog-content').load('groupRolesForm?groupName='+groupSelected);
			
	}
	
    function activateContent(hash){
    	hash = hash.substring(1);
    	var children = $("body").children(".container").children();
        if(!hash){
      	  hash = $(children[0]).attr("id");
      	  window.location.hash = "#" + hash;
        }
        $.each(children, function(key, value) {
      	  if($(value).attr("id") == hash){
      		 $(value).show();
      	  }else{
      		 $(value).hide(); 
      	  }
      	  
        });
    }
    
	/*
	 * Event Handlers
	 * 
	 * 
	 * 
	 */
	
	$('#selectDataset').change(function(){
		var roleNamesParameter = "";
		$.each($(this).selectpicker().val(), function( key, value ) {
			roleNamesParameter += "&roleNames=" + value;
		});
		
		//pending and authorized groups
		$('#selectedGroups').children().each(function(key, value){
			$('#selectGroup option[value="'+value+'"]').attr('disabled', 'disabled');
		});
		
		roleNamesParameter = roleNamesParameter.substring(1, roleNamesParameter.length);
		$.getJSON("../rest/groups?"+roleNamesParameter, null, function (data) {
			$('#selectGroup').empty();
			if(data.length > 0){
				$.each(data, function(key, value) {
					$('#selectGroup').append('<option data-icon="fa fa-users" value=' + value.groupName + '>' + value.groupName + '</option>');
				});
				disableNonEligibleGroups();
			}else{
				$('#selectGroup').children().remove().end().append($("<option class="+"'bs-title-option'"+"></option>").val("").text("No matches"));
			}
			$('#selectGroup').selectpicker('refresh');
		});
		$("#groupSelectionHighlight").effect("highlight", {}, 2500, function(){} );
	});
	
	$('#selectGroup').change(function(){
		var groupSelected = $(this).find(":selected").val();
		$.getJSON("../rest/groups/"+groupSelected+"/policies", null, function (data) {
			if(data.length > 0){
				showGroupPoliciesDialog(groupSelected);
			}else{
				appendSelectedGroup(groupSelected);
			}
		});
	});

	
    $('body').on('click', '.btn-remove',function(){
    	var groupName =  $(this).closest('.group-thumbnail').data('group-name');
    	var parentID = $(this).closest('.group-thumbnail').parent().attr('id');
    	$(this).closest('.group-thumbnail').remove();
    	$('#selectGroup option[value=' + groupName + ']').removeAttr('disabled');
    	$('#selectGroup').selectpicker('render');
    	updateSelectedGroupsCounter();
    });
    
    $('body').on('click', '.btn-leave',function(){
    	var groupName =  $(this).closest('.group-thumbnail').data('group-name');
    	var action = function() {
	    	$.ajax({
	    	    url: 'groups?groupName='+groupName,
	    	    type: 'DELETE',
	    	    success: function(result) {
	    			showOKDialog("success", true);
	    	    }
	    	});
    	};   	
    	showActionDialog("Are you sure you want to leave the "+groupName+" group?", action);
    });
	
	
	$('body').on('click', '#buttonSave', function(){
		var selectedGroups = [];
		var selectedGroupElements = $("#selectedGroups").children('.group-thumbnail');
		if(selectedGroupElements.size() > 0){
			selectedGroupElements.each(function(){
				var groupName = $(this).data('group-name');
				selectedGroups.push(groupName);
			});
			var jsonObject = {
					selectedGroups: selectedGroups
			};
			postData("groups?username="+username,jsonObject);
		}else{
			showOKDialog("Select a group at least");
		}
	});

	$('body').on('click', '.btn-show-policies',function(){
		var groupSelected = $(this).closest('.group-thumbnail').data('group-name');
		showGroupPoliciesInfoDialog(groupSelected);
	});
	
	$('body').on('click', '.btn-show-datasets',function(){
		var groupSelected = $(this).closest('.group-thumbnail').data('group-name');
		showGroupDatasetsInfoDialog(groupSelected);
	});
	
    
    /*
     * Initial conditions
     * 
     * 
     * 
     */
    disableNonEligibleGroups();
    
    $('#selectDataset').selectpicker({
        style: 'btn-info',
        title: 'No filters'
    });
    
    $('#selectGroup').selectpicker({
        style: 'btn-info',
        title: 'Nothing selected'
    });
    activateContent(window.location.hash); 
    $('body').on('click', '.scroll',function(event){
    	activateContent($(this).attr("id")); 
    });
    
    var dataset = $.url('?dataset');
    if(dataset != null && dataset != 'ALL'){
    	$('#selectDataset').selectpicker('val', dataset);
    	$('#selectDataset').trigger("change");
    }
});