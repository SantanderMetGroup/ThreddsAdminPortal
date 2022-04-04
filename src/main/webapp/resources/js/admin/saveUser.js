$(document).ready(function(){
	$("#saveButton").live('click', function(){
		$('#errors').empty();
		$('#success').hide();
		$.each($('#userForm input'),function(){
			$(this).removeClass('fieldError');
		});
		var str = $("#userForm").serialize();
		$.ajax({
			type:"post",
			data:str,
			url:"saveUser",
			async:false,
			dataType: "json",
			success: function(response){
				if(response.status == "SUCCESS"){
					$('#success').show();
				}else{
					errorInfo ="";
					$.each(response.result, function(){
						errorInfo += this.code + "<br/ >";
						$('#'+this.field).addClass('fieldError');
					});
					$('#errors').append(errorInfo);
					$('#errors').show('slow');
				}
			}
		});
   });
});