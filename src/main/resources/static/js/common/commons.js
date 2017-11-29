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