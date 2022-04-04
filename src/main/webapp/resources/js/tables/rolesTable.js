$(document).ready(function () {
	$('#RoleTableContainer').on('click','.show-policies', function(){
		var roleName = $(this).data('rolename');
		showDialogURLContent('Policies of role ' + roleName, '../admin/rolePolicies?roleName='+roleName);
	});
	
	
    $('#RoleTableContainer').jtable({
        actions: {
            listAction: '../admin/getRoles',
            createAction: '../admin/createRole',
            updateAction: '../admin/updateRole',
            deleteAction: '../admin/deleteRole'
        },
        title: 'System roles control panel',
        paging: true,
        deleteConfirmation: function(data){
        	data.deleteConfirmMessage = 'Are you sure to delete role ' + data.record.roleName + '?';
        },
        fields: {
            roleName: {
                title: 'Role name',
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
            isRestricted: {
                title: 'Restricted',
                width: '2%',
                input: function(data) {
                    var $cb = $('<input name="isRestricted" type="checkbox" value="true"/>');
                    if(data.record && data.record.isRestricted){
                        $cb.attr('checked', 'checked');
                    }  
                    return $cb;
                }
            },
            isDataset: {
                title: 'Dataset',
                width: '2%',
                input: function(data) {
                    var $cb = $('<input name="isDataset" type="checkbox" value="true"/>');
                    if(data.record && data.record.isDataset){
                        $cb.attr('checked', 'checked');
                    }  
                    return $cb;
                }
            },
            metadata: {
                title: 'Metadata',
                list: false,
                input: function (data){
                	if(data.record)
                		return '<textarea name="metadata" rows="3" cols="80">'+data.record.metadata+'</textarea>';	
                	else
                		return '<textarea name="metadata" rows="3" cols="80"></textarea>';	
                }
            },
            PolicyColumn: {
            	width: '2%',
            	display: function (data) {
                    return '<button title="Policy assignment" class="show-policies jtable-command-button jtable-edit-command-button" data-rolename='+data.record.roleName+'><span>Policies</span></button>';
                },
                create:false,
            	edit:false
            }
        }
    });
    $('#RoleTableContainer').jtable('load');
});