var modSub = 0;
$(document).ready(function() {
	initialTable();

	$('#btn_search').click(function(){
		isChanged = false;
		$('#tbl_datatables').DataTable().ajax.reload();
	});

	var table = $('#tbl_datatables').DataTable();
	$('#tbl_datatables tbody').on('dblclick', 'tr', function () {
		if ( $(this).children().hasClass('dataTables_empty') ) {
			return;
		}
		NhapLieuHoSoModel.soLt.value = table.row( this ).data().soLt;
		NhapLieuHoSoModel.submit();
	});
});

function initialTable() {

	// Define Columns
	var columns = [
			{
				data : 'soLt',
				'className' : 'align-middle text-center',
				'width':'12%'
			},{
				data : 'ngayLap',
				'className' : 'align-middle text-center',
				'width':'6%'
			},{
				data : 'ngayDk',
				'className' : 'align-middle text-center',
				'width':'6%'
			},{
				data : 'ngayKt',
				'className' : 'align-middle text-center',
				'width':'6%'
			},{
				data : 'ngayNl',
				'className' : 'align-middle text-center',
				'width':'6%'
			},{
				data : 'soDk',
				'className' : 'align-middle text-center',
				'width':'12%'
			},{
				data : 'thoiHanBaoQuan',
				'className' : 'align-middle text-center',
				'width':'6%'
			},{
				data : 'maLoai',
				'className' : 'align-middle text-center',
				'width':'5%'
			},{
				data : 'trichYeu',
				'className' : 'align-middle'
			},{
				data : 'cbnl',
				'className' : 'align-middle text-center',
				'width':'10%'
			},{
				data : 'maDvNl',
				'className' : 'align-middle text-center',
				'width':'12%'
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
		}
		,{
			'targets' : [4],
			'searchable' : false,
			'orderable': false
		},{
			'targets' : [5],
			'searchable' : false,
			'orderable': false
		},{
			'targets' : [6],
			'searchable' : false,
			'orderable': false
		},{
			'targets' : [7],
			'searchable' : false,
			'orderable': false
		},{
			'targets' : [8],
			'searchable' : false,
			'orderable': false
		},{
			'targets' : [9],
			'searchable' : false,
			'orderable': false
		},{
			'targets' : [10],
			'searchable' : false,
			'orderable': false
		}],
		"order": [[ 1, "ASC" ]],
		'ajax' : {
			
			'url' : baseUrl + 'api/DanhSachHoSo/search',
			"contentType": "application/json",
			"type": "GET",
			'data' : function(d) {

				// set search keyword to parameter
				d.allSearch = $('#allSearch').val();
				d.maDvNl = $('#maDvNl').val();
				d.maLoai = $('#maLoai').val();

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