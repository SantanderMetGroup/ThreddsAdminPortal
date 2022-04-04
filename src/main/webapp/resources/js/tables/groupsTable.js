$(document).ready(function () {
	
	$('#GroupTableContainer').on('click','.show-role', function(){
		$('#dialog-content').empty();
		var groupName = $(this).data('groupname');
		showDialogURLContent('Policies of role ' + groupName, '../admin/groupRoles?groupName='+groupName);
	});
	
	
    $('#GroupTableContainer').jtable({
        actions: {
            listAction: '../admin/getGroups',
            createAction: '../admin/createGroup',
            updateAction: '../admin/updateGroup',
            deleteAction: '../admin/deleteGroup',
        },
//        toolbar: {
//            items: [{
//                text: 'Message',
//                click: function () {
//                	showDialogURLContent('Message center', '../admin/message');
//                    }
//                }]
//        },
        title: 'System groups control panel',
        paging: true,
        deleteConfirmation: function(data){
        	data.deleteConfirmMessage = 'Are you sure to delete group ' + data.record.groupName + '?';
        },
        fields: {
            groupName: {
                title: 'Group name',
                width: '20%',
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
            isProject: {
                title: 'Is project',
                width: '20%',
                input: function(data) {
                    var $cb = $('<input id="isProject" name="isProject" type="checkbox" value="true" />');
                    if(data.record && data.record.isProject){
                        $cb.attr('checked', 'checked');
                    }  
                    return $cb;
                }
            },
            coordinator:{
            	title: 'Coordinator',
            	width: '30%'
            },
            RoleColumn: {
                display: function (data) {
                    return '<button title="Group roles" class="show-role jtable-command-button jtable-edit-command-button" data-groupname='+data.record.groupName+'><span>Roles</span></button>';
                },
                create:false,
            	edit:false
            }
        }
    });
    $('#GroupTableContainer').jtable('load');
});