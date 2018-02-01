$.Alert = function(options, fn){
	alert(options)
//		Messenger().post(options);
//		Messenger().post({messenger: "mmmm", type: "error"});
// if( !$.isEmptyObject(options) ) if(!$.isEmptyObject(options["message"])){
// 	options.title = "";
// 	options.text = options.message;
// }else{
// 	var text = options;
// 	options = {};
// 	options.title = "";
// 	options.text = text;
// }
// swal(options, fn);
}

$.reloadCurrent = function(){
	location.reload();
}
$.nullToEmpy = function(text){
	if(!text){
		return "";
	}
	return text.trim();
}
$.trimToVisible = function(text){
	if(!text){
		return "";
	}
	if(text.length > 20) {
		return text.substr(0,20) + '...';
	}
	return text.trim();
}
$.handlerSampleInput = function(e) {
	var target = $(e.target);
	var name = target.data("name");
	var selector = target.data("selector");
	if(name && selector) {
		$.ajax({
            url : '/v1/sample/get?name='+name,
            type : 'GET',
            contentType : 'application/json; charset=utf-8',
            dataType : 'json',
            cache : false
        }).done(function(data, textStatus, jqXHR) {
            if (data && data.code == 200) {
                $(selector).val(data.result);
            } else {
                $.Alert({
                    message : "Edit CheckConfig FAIL - " + data.message,
                    type : "error"
                });
            }
        }).fail(function(jqXHR, textStatus, errorThrown) {
            $.Alert({
                message : "Update CheckConfig FAIL",
                type : "error"
            });
        });
	} else {
		$.Alert("Sample not defined!");
	}
}
$.bindSampleInput = function() {
	$(".sampleInput").on('click', function (e){
		$.handlerSampleInput(e);
	})
}

Date.prototype.format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}  