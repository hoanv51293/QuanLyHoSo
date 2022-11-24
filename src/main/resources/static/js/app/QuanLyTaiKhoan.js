$(document).ready(function() {
	initialTable();

	$('#btn_search').click(function(){
		isChanged = false;
		$('#tbl_datatables').DataTable().ajax.reload();
	});

	$('#btn-edit-submit').click(function(){
		executeUpdate();
	});

	$('#btn-create-submit').click(function(){
		executeCreate();
	});

	$('#btn-edit-cancel,#btn-edit-cancel-header').click(function(){
		hideModelDetail();
		clearError();
		$('#tbl_datatables').DataTable().ajax.reload();
	});
	$('#btn-clone').click(function(){
		resetHiddenCommon();
		$('#userName').val('');
		settingCommonBeforeShowModalAdd();
		$('#userName').attr('disabled',false);
	});

});
function showModalAdd() {
	resetData();
	settingCommonBeforeShowModalAdd();
	showModelDetail();
	$('#userName').attr('disabled',false);
}
function showModalUpdate(primaryKey, cmnUpdateDate) {
	settingHiddenCommon(primaryKey, cmnUpdateDate);
	settingCommonBeforeShowModalUpdate();
	$('#userName').attr('disabled',true);
	$.ajax({
		contentType : "application/json",
		url : baseUrl + 'api/QuanLyTaiKhoan/findById',
		// beforeSend : function(xhr) {
		// xhr.setRequestHeader(header, token);
		// },
		data : {primaryKey:primaryKey},
		type : "GET",
		dataType : 'json',
		timeout : 30000, // ms
		success : function(data) {
			if (data.statusCode == '200') {
				settingData(data);
			}
		},
		error : function(xhr) {
			showPopupCommon('error', 'Exception', null);
		}
	});
}

function showModalDelete(primaryKey, cmnUpdateDate) {
	settingHiddenCommon(primaryKey, cmnUpdateDate);
	showModelDelete();
}
function executeCreate(){
	var valueRequest = {
		primaryKey		:$('#primaryKey').val(),
		hoTen			:$('#hoTen').val(),
		userName		:$('#userName').val(),
		quyen			:$('#quyen').val()
	};
	callApi('POST', valueRequest);
}
function executeUpdate(){
	var valueRequest = {
		primaryKey		:$('#primaryKey').val(),
		hoTen			:$('#hoTen').val(),
		userName		:$('#userName').val(),
		quyen			:$('#quyen').val()
	};
	callApi('PUT', valueRequest);
}

function executeDelete(){
	var valueRequest = {
		primaryKey: $('#primaryKey').val()
	};
	callApi('DELETE', valueRequest);
}
function settingData(data) {
	$('#hoTen').val(data.modelData.hoTen);
	$('#userName').val(data.modelData.userName);
	$('#quyen').val(data.modelData.quyen);

	settingLabelCommon(data);
	showModelDetail();
}

function resetData() {
	resetHiddenCommon();
	resetLabelCommon();

	$('#hoTen').val('');
	$('#userName').val('');
	$('#quyen').val('');
}
function callApi(method, valueRequest) {
	$.ajax({
		contentType : "application/json",
		url : baseUrl + 'api/QuanLyTaiKhoan/maintenance',
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
				$('#tbl_datatables').DataTable().ajax.reload();
				if (method == 'POST' || method == 'PUT') {
					hideModelDetail();
				}
			}
			if (method == 'DELETE'){
				hideModelDelete();
			}
		},
		error : function(xhr) {
			handleApiError(xhr);
		}
	});
}

function initialTable() {

	// Define Columns
	var columns = [
			{
				data : 'userName',
				'className' : 'align-middle text-center',
				'width':'8%'
			},{
				data : 'hoTen',
				'className' : 'align-middle',
				'width':'12%'
			},{
				data : 'quyen',
				'className' : 'text-center align-middle',
				'width':'10%'
			},{
				data : 'cmnUpdDel',
				'className' : 'align-middle text-center',
				'width':'7%',
				'render' : function(data, type, row, meta) {
					return renderButtonUpdDel(row.primaryKey,'');
				}
			}];

	//Create DataTable
	createDataTableCommon();
	$('#tbl_datatables').DataTable({
		'columns' : columns,
		'columnDefs' : [{
			'targets' : [0],
			'searchable' : false,
			'orderable': false
		},{
			'targets' : [1],
			'searchable' : false,
			'orderable': false
		}
		,{
			'targets' : [2],
			'searchable' : false,
			'orderable': false
		}
		,{
			'targets' : [3],
			'searchable' : false,
			'orderable': false
		}],
		"order": [[ 1, "ASC" ]],
		'ajax' : {
			
			'url' : baseUrl + 'api/QuanLyTaiKhoan/search',
			"contentType": "application/json",
			"type": "GET",
			'data' : function(d) {

				// set search keyword to parameter
				d.allSearch = $('#allSearch').val();

				// set order to parameter
				var order = $('#tbl_datatables').DataTable().order();
				var columnIndex = order[0][0];
				d.orderColumn = columns[columnIndex].data;
				d.orderDirection = order[0][1];
			},
			complete : function(response) {
				checkDataTable('tbl_datatables', response.responseJSON.recordsTotal);
			},
			error : function(xhr) {
				handleApiError(xhr);
			}
		}
	});
	checkDataTable('tbl_datatables', 0);
}