$(document).ready(function() {

	if($('#soLt').val()) {
		refer();
	}
});
function executeBack(){
	DanhSachHoSoModel.submit();
}
function refer() {
	resetData();
	$.ajax({
		contentType : "application/json",
		url : baseUrl + 'api/NhapLieuHoSo/findById',
		data : {primaryKey:$('#soLt').val()},
		type : "GET",
		dataType : 'json',
		timeout : 30000, // ms
		success : function(data) {
			handleMessageResponse(data);
			if (data.statusCode == '200') {
				settingData(data);
			}
		},
		error : function(xhr) {
			showPopupCommon('error', 'Exception', null);
		}
	});
}
function reloadMaDvNl(value){
	var temp = value.split('/');
	$('#maDvNl').val('');
	if (temp.length<2) {
		return;
	}
	$('#maDvNl').val(temp[1]);
}
function changeSoLt(value){
	$('#maLoai').val('');
	var temp = value.split('/');
	if (temp.length<2) {
		return;
	}
	var temp2 = temp[0];
	var maLoai = temp2.replace(/[0-9]/g, '');;
	$('#maLoai').val(maLoai);
}
function settingData(data) {
	$('#soLt').val(data.modelData.soLt);
	$('#ngayKt').val(data.modelData.ngayKt);
	$('#trichYeu').val(data.modelData.trichYeu);
	$('#cbnl').val(data.modelData.cbnl);
	$('#soDk').val(data.modelData.soDk);
	$('#ngayLap').val(data.modelData.ngayLap);
	$('#ngayDk').val(data.modelData.ngayDk);
	$('#ngayNl').val(data.modelData.ngayNl);
	$('#maLoai').val(data.modelData.maLoai);
	$('#maDvNl').val(data.modelData.maDvNl);
	$('#thoiHanBaoQuan').val(data.modelData.thoiHanBaoQuan);
	$('#primaryKey').val(data.modelData.primaryKey);
}
function resetData() {
	$('#ngayKt').val('');
	$('#trichYeu').val('');
	$('#cbnl').val('');
	$('#soDk').val('');
	$('#ngayLap').val('');
	$('#ngayDk').val('');
	$('#ngayNl').val('');
	$('#maLoai').val('');
	$('#maDvNl').val('');
	$('#thoiHanBaoQuan').val('');
	$('#primaryKey').val('');
}

function executeSubmit(method){
	var valueRequest = {
			soLt:				$('#soLt').val(),
			ngayKt:				$('#ngayKt').val(),
			trichYeu:			$('#trichYeu').val(),
			cbnl:				$('#cbnl').val(),
			soDk:				$('#soDk').val(),
			ngayLap:			$('#ngayLap').val(),
			ngayDk:				$('#ngayDk').val(),
			ngayNl:				$('#ngayNl').val(),
			maLoai:				$('#maLoai').val(),
			maDvNl:				$('#maDvNl').val(),
			thoiHanBaoQuan:		$('#thoiHanBaoQuan').val(),
			primaryKey:			$('#primaryKey').val()
	}
	callApi(method, valueRequest);
}

function callApi(method, valueRequest) {
	$.ajax({
		contentType : "application/json",
		url : baseUrl + 'api/NhapLieuHoSo/maintenance',
		data : JSON.stringify(valueRequest),
		type : method,
		dataType : 'json',
		timeout : 30000, // ms
		success : function(data) {
			handleMessageResponse(data);
			if (data.statusCode == 200) {
				$('#tbl_datatables').DataTable().ajax.reload();
				if (method == 'POST' || method == 'PUT') {
					$('#primaryKey').val(data.primaryKey);
					$('#soLt').val(data.primaryKey);
					refer();
				}
			}
			if (method == 'DELETE'){
				resetData();
				$('#soLt').val('');
			}
		},
		error : function(xhr) {
			handleApiError(xhr);
		}
	});
}