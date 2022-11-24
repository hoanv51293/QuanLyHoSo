$(document).ready(function() {

	var flag = localStorage.getItem("common.flagAccepRemind");
	if (flag && flag === "true") {
		$('#flagAccepRemind').bootstrapToggle('on');
		changeFlagAccessRemind(true);
	} else {
		$('#flagAccepRemind').bootstrapToggle('off');
		$('#numberOfRemind').text(localStorage.getItem("common.numberOfRemind"));
		$('.toast-remind').toast('hide');
	}
	$('#flagAccepRemind').change(function(){
		if(!$(this).prop('checked')) {
			$(".remind_position_left").each(function(){
				$(this).remove();
			});
	
			$(".remind_position_right").each(function(){
				$(this).remove();
			});
		} else {
			loadReminder();
		}
	});
});

var remind;
var listRemindId = null;
var flagAccepRemind;
function changeFlagAccessRemind(check) {
	flagAccepRemind = check;
	localStorage.setItem("common.flagAccepRemind", flagAccepRemind);
	if (flagAccepRemind == true) {
		loadReminder();
		$('.toast-remind').toast('show');
		if (!remind) {
			loadReminder();
			remind = setInterval(loadReminder, 1000*60*60);
		}
	} else {
		$('.toast-remind').toast('hide');
	}
	
}

// create AJAX xhr
var $xhr = null;
function loadReminder() {

	if (flagAccepRemind == false && remind) {
		clearInterval(remind);
		remind = null;
		return;
	}

	// kill the request
	if ($xhr != null) {
		$xhr.abort();
	}

	//var paramRequest = "";
	//for (var i = 0; i <listRemindId.length; i ++) {
	//	paramRequest += listRemindId[i] + "_";
	//}
	$xhr = $.ajax({
		contentType : "application/json",
		url : baseUrl + 'api/remind/search',
		// beforeSend : function(xhr) {
		// xhr.setRequestHeader(header, token);
		// },
		//data : {
		//	'remindId':paramRequest
		//},
		type : "GET",
		dataType : 'json',
		timeout : 30000, // ms
		success : function(data) {
			settingToast(data.data, data.arrId)
			$('#numberOfRemind').text(data.count);
			localStorage.setItem("common.numberOfRemind", data.count);
		},
		error : function(xhr) {
			//alert('Exception');
		}
	});
}
function settingToast(listRemindModel, arrId) {

	$(".remind_position_left").each(function(){
		if (!arrId.includes(this.id)) {
			$(this).remove();
		}
	});

	$(".remind_position_right").each(function(){
		if (!arrId.includes(this.id)) {
			$(this).remove();
		}
	});

	for (var i = 0; i<listRemindModel.length; i++) {
		if ($('#' + listRemindModel[i].id).length) {
			continue;
		}
		if ($('.remind_position_left').length < 5) {
			var str = '<div class="toast toast-remind remind_position_left" id="'+listRemindModel[i].id+'" data-autohide="false">' +
			'	<div class="toast-header">' +
			'		<strong class="mr-auto text-primary">' + listRemindModel[i].header + '</strong>' +
			'		<small class="text-muted font-weight-bold">Status: '+listRemindModel[i].statusTask+'</small>' +
			'	</div>' +
			'	<div class="toast-body">' +
			'		<p>' + listRemindModel[i].content +
			'		</p>' +
			'	</div>' +
			'	<hr style = "margin-top:0;margin-bottom:0">' +
			'	<div class="toast-footer">' +
			'		<a href = "#" style = "text-decoration: none !important;" onclick="referScreen(\''+listRemindModel[i].referScreen+'\')">' +
			'			<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'			<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'			<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'			<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'			<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'			<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'		</a>' +
			'		<small class="text-muted float-right mr-3 font-weight-bold"><a href="#" class="text-decoration-none" onclick="changeStatus(\''+listRemindModel[i].id+'\')">Close Remind</a></small>' +
			'	</div>' +
			'</div>';
			$('#remind_position_left').prepend(str);
		} else {
			var str = '<div class="toast  toast-remind remind_position_right" id="'+listRemindModel[i].id+'" data-autohide="false">' +
			'	<div class="toast-header">' +
			'		<strong class="mr-auto text-primary">' + listRemindModel[i].header + '</strong>' +
			'		<small class="text-muted font-weight-bold">Status: '+listRemindModel[i].statusTask+'</small>' +
			'	</div>' +
			'	<div class="toast-body">' +
			'		<p>' + listRemindModel[i].content +
			'		</p>' +
			'	</div>' +
			'	<hr style = "margin-top:0;margin-bottom:0">' +
			'	<div class="toast-footer">' +
			'		<a href = "#" style = "text-decoration: none !important;" onclick="referScreen(\''+listRemindModel[i].referScreen+'\')">' +
			'				<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'				<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'				<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'				<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'				<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'				<i class="fa fa-arrow-circle-right" aria-hidden="true"></i>' +
			'			</a>' +
			'		<small class="text-muted float-right mr-3 font-weight-bold"><a href="#" class="text-decoration-none" onclick="changeStatus(\''+listRemindModel[i].id+'\')">Close Remind</a></small>' +
			'	</div>' +
			'</div>';
			$('#remind_position_right').prepend(str);
		}
	}
	$('.toast-remind').toast('show');
}
function referScreen(referScreen){
	window.location.href = baseUrl +referScreen;
}
function changeStatus(remindId){
	showPopupCommon('confirm', 'Close?', 'changeStatusY('+remindId+')');
}
function changeStatusY(remindId){
	var valueRequest = {
		id: remindId
	};
	$xhr = $.ajax({
		contentType : "application/json",
		url : baseUrl + 'api/remind/maintenance',
		// beforeSend : function(xhr) {
		// xhr.setRequestHeader(header, token);
		// },
		data : JSON.stringify(valueRequest),
		type : "PUT",
		dataType : 'json',
		timeout : 30000, // ms
		success : function(data) {
			loadReminder();
		},
		error : function(xhr) {
			//alert('Exception');
		}
	});
}