$(document).ready(function(){
	//$("#countrySelect").click(function() {
	    // If the select list is empty:
		if($(this).find("option").size() > 0){
			$("#countrySelect").removeData();
		}
	    if ($(this).find("option").size() == 0) {
	        // Documentation on getJSON: http://api.jquery.com/jQuery.getJSON/
	        $.getJSON("../getCountryNames", null, function (data) {
	            $.each(data.items, function(i,item){
	                $("#countrySelect").append("<option value="+item+">"+item+"</option>");
	            });
	        });
	    }
	//});
});