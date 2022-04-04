$(document).ready(function () {
	var groupSelect = $('#select-groups');	
	$.each(availableGroups, function(item) {
		groupSelect.append($("<option />").val(this.toString()).text(this.toString()));
	});

    $('.html-editor').ckeditor({
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
    $(".multiple-select").select2({
    	placeholder: "Select the groups to send the message",
    	width:"100%"
    });
	
});