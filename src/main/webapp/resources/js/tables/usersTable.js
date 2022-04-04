 
$(document).ready(function () {
	
	var pendingOfAuthUsers = []; 
		
	$.getJSON('getPendingOfAuthUsers', null, function (data) {
        $.each(data, function(i,item){
        	pendingOfAuthUsers[i] = item.username;
        });
    });
	
	$("#searchContainer").on("click", ".btn-search", function(){
       // e.preventDefault();
        $('#personTableContainer').jtable('load', {
        	text: $('#inputSearch').val(),
        	column: window.location.hash.substring(1,window.location.hash.length)
        });
	});

	$('#personTableContainer').on('click','.show-role', function(){
		var username = $(this).data('username');
		showDialogURLContent("Roles of " + username, "../admin/userRoles?username=" + username);
		return false;
	});
	
	$('#personTableContainer').on('click','.show-group', function(){
		var username = $(this).data('username');
		showDialogURLContent("Groups of " + username, "../admin/userGroups?username=" + username);
		return false;
	});
	
	
	$('#personTableContainer').on('click','.confirm-email', function(){
		var username = $(this).data('username');
		$.get( "resendConfirmationEmail?username="+username, function( data ) {
			  //$( ".result" ).html( data );
			  alert( "Email sent" );
			});
		return false;
	});

    $('#personTableContainer').jtable({
        actions: {
            listAction: '../admin/getUsers',
            createAction: '../admin/createUser',
            updateAction: '../admin/updateUser',
            deleteAction: '../admin/deleteUser'
        },
        paging: true,
        sorting: true, //Enable sorting
        defaultSorting: 'Name ASC', //Set default sorting
        selecting: true, //Enable selecting
        multiselect: true, //Allow multiple selecting
        deleteConfirmation: function(data){
        	data.deleteConfirmMessage = 'Are you sure to delete person ' + data.record.username + '?';
        },
        rowInserted: function(event, data){
        	$.each(pendingOfAuthUsers, function(index, value){
        		if(data.record.username == value){
        			$('#PersonTableContainer').jtable('selectRows', data.row);
        		}
        	});        	
        },
        fields: {
            username: {
                title: 'Username',
                width: '10%',
                key: true,
                create:true
            },
            password: {
                title: 'Password',
                list:false,
                input: function (data){
                	if(data.record)
                		return '<input type="password" name="password" value="'+ data.record.password + '"/>';
                	else
                		return '<input type="password" name="password" value=""/>';	
                }
            },
            confirmPassowrd: {
                title: 'Confirm password',
                list:false,
                input: function (data){
                	if(data.record)
                		return '<input type="password" name="confirmPassword" value="'+ data.record.password + '"/>';
                	else
                		return '<input type="password" name="confirmPassword" value=""/>';	
                }
            },
            firstName: {
                title: 'First name',
                width: '15%'
            },
            lastName: {
                title: 'Last name',
                width: '15%',
            },
            email: {
                title: 'Email',
                width: '20%',
                sorting: false
            },
            active: {
                title: 'Active',
                width: '3%',
                create: false,
                edit: false,
                sorting: false
            },
            regdate: {
            	title: 'Reg date',
            	width: '5%'
            },
            institution: {
                title: 'Institution',
                width: '30%',
            },
            isoCode: {
            	title: 'Country',
            	width: '25%',
            	options: '../admin/getCountryNames',
            	optionsSorting: 'text',
            	sorting: false
            },
            motivation: {
                title: 'Motivation',
                visibility: 'hidden',
                width: '30%',
                sorting: false,
                input: function (data){
                	if(data.record)
                		return '<textarea name="motivation" rows="3" cols="80">'+data.record.motivation+'</textarea>';	
                	else
                		return '<textarea name="motivation" rows="3" cols="80"></textarea>';	
                }
            },
            RoleColumn: {
                display: function (data) {
                    return '<button title="User roles" class="show-role jtable-command-button jtable-edit-command-button" data-username='+data.record.username+'><span>Roles</span></button>';
                },
                sorting: false,
                create:false,
            	edit:false
            },
            GroupColumn: {
                display: function (data) {
                    return '<button title="User groups" class="show-group jtable-command-button jtable-edit-command-button" data-username='+data.record.username+'><span>Groups</span></button>';
                },
                sorting: false,
                create:false,
            	edit:false
            },
//            ConfirmationColumn: {
//            	display: function (data) {
//            		if(!data.record.active)
//            			return '<button title="Resend confirmation email" class="confirm-email jtable-command-button jtable-edit-command-button" data-username='+data.record.username+'><span>Confirmation email</span></button>';
//                },
//                create:false,
//            	edit:false
//            }
        }
    });
    $('#personTableContainer').jtable('load');
    
    //Re-load records when user click 'load records' button.
    $('#content').on('click','#button-load-records',function (e) {
        e.preventDefault();
        $('#personTableContainer').jtable('load', {
        	text: $('#text').val(),
        	column: $('#column').val()
        });
    });
    

});