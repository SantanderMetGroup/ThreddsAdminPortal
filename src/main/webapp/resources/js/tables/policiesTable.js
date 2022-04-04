$(document).ready(function () {
	
    $('#PolicyTableContainer').jtable({
        actions: {
            listAction: '../admin/getPolicies',
            createAction: '../admin/createPolicy',
            updateAction: '../admin/updatePolicy',
            deleteAction: '../admin/deletePolicy'
        },
        title: 'System policies control panel',
        paging: true,
        deleteConfirmation: function(data){
        	data.deleteConfirmMessage = 'Are you sure to delete policy ' + data.record.policyName + '?';
        },
        formCreated: function (event, data){
        	 $('.html-editor').ckeditor({
        	    	width: "695px",
        	    	toolbarGroups: [
        	    	            	{ name: 'document', groups: [ 'mode', 'document'] },
        	    	            	{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
        	    	            	{ name: 'editing', groups: [ 'find', 'selection' ] },
        	    	            	{ name: 'forms' },
        	    	            	{ name: 'links' },
        	    	            	{ name: 'insert' },
        	    	            	'/',
        	    	            	{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
        	    	            	{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align'] },
        	    	      
        	    	            	{ name: 'styles' },
        	    	            	{ name: 'colors' },
        	    	            	{ name: 'tools' }
        	    	            ]
        	    });
        	 $('.html-editor').val(data.record.agreement);
        },
        fields: {
            policyName: {
                title: 'Policy name',
                width: '30%',
                key: true,
                create:true
            },
            description: {
                title: 'Description',
                width: '30%',
                input: function (data){
                	if(data.record)
                		return '<textarea name="description" rows="3" cols="80">'+data.record.description+'</textarea>';	
                	else
                		return '<textarea name="description" rows="3" cols="80"></textarea>';	
                }
            },
            disableRoles: {
            	list: false,
                visibility: 'hidden',
                input: function (data){
                	if(data.formType==="edit"){
                		var $cb = $('<p>Disable roles which contains this policy</p>'+'<input name="disableRoles" type="checkbox" value="true"/>');
                		return $cb;
                	}
                }
            },
            agreement: {
                title: 'Agreement',
                width: '40%',
                list: false,
                input: function(data) {
                    return $('<textarea class="html-editor" rows="3" cols="80" name="agreement"></textarea>');
                	
                }
            }
        }
    });
    $('#PolicyTableContainer').jtable('load');
});