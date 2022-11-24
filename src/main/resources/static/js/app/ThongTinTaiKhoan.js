$(document).ready(function() {

	$('#update_password').click(function(){
		executeUpdate();
	});

});

function executeUpdate(){
	var valueRequest = {
		oldPassword		:$('#oldPassword').val(),
		newPassword		:$('#newPassword').val(),
		rePassword		:$('#rePassword').val(),
		hoTen			:$('#hoTen').val()
	}
	callApi('PUT', valueRequest);
}

function callApi(method, valueRequest) {
	$.ajax({
		contentType : "application/json",
		url : baseUrl + 'api/ThongTinTaiKhoan/maintenance',
		// beforeSend : function(xhr) {
		// xhr.setRequestHeader(header, token);
		// },
		data : JSON.stringify(valueRequest),
		type : method,
		dataType : 'json',
		timeout : 30000, // ms
		success : function(data) {
			handleMessageResponse(data);
			if (data.statusCode == 200) {
				setTimeout(logout, 1000);
			}
		},
		error : function(xhr) {
			handleApiError(xhr);
		}
	});
}
function logout(){
	window.location.href = baseUrl + 'login';
}