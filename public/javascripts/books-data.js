var DatatableDataLocalDemo = function () {
	//== Private functions

	// demo initializer
	var demo = function () {

		var dataJSONArray = JSON.parse('[{"BookID":1,"BookTitle":"the little prince","Difficulty":"Easy","Category":"adventure","Language":"English","Date":"04/17/2018","Status":1},{"BookID":2,"BookTitle":"علاء الدين","Difficulty":"Easy","Category":"adventure","Language":"عربي","Date":"04/18/2018","Status":2}]');

		var datatable = $('.m_datatable').mDatatable({
			// datasource definition
			data: {
				type: 'local',
				source: dataJSONArray,
				pageSize: 10
			},

			// layout definition
			layout: {
				theme: 'default', // datatable theme
				class: '', // custom wrapper class
				scroll: false, // enable/disable datatable scroll both horizontal and vertical when needed.
				// height: 450, // datatable's body's fixed height
				footer: false // display/hide footer
			},

			// column sorting
			sortable: true,

			pagination: true,

			search: {
				input: $('#generalSearch')
			},

			// inline and bactch editing(cooming soon)
			// editable: false,

			// columns definition
			columns: [{
				field: "BookID",
				title: "#",
				width: 50,
				sortable: false,
				selector: false,
				textAlign: 'center'
			}, {
				field: "BookTitle",
				title: "Book Title"
			}, {
				field: "Difficulty",
				title: "Difficulty",
				responsive: {
					visible: 'lg'
				}
			}, {
				field: "Category",
				title: "Category",
				width: 100
			}, {
				field: "Language",
				title: "Language",
				responsive: {
					visible: 'lg'
				}
			}, {
				field: "Date",
				title: "Date",
				type: "date",
				format: "MM/DD/YYYY"
			},{
				field: "Status",
				title: "Status",
				// callback function support for column rendering
				template: function (row) {
					var status = {
						1: {
							'title': 'Pending',
							'class': 'm-badge--brand'
						},
						2: {
							'title': 'Success',
							'class': ' m-badge--success'
						}
					};
					return '<span class="m-badge ' + status[row.Status].class + ' m-badge--wide">' + status[row.Status].title + '</span>';
				}
			}, {
				field: "Actions",
				width: 110,
				title: "Actions",
				sortable: false,
				overflow: 'visible',
				template: function (row) {
					var dropup = (row.getDatatable().getPageSize() - row.getIndex()) <= 4 ? 'dropup' : '';

					return '\
						<div class="dropdown ' + dropup + '">\
							<a href="#" class="btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" data-toggle="dropdown">\
                                <i class="la la-ellipsis-h"></i>\
                            </a>\
						  	<div class="dropdown-menu dropdown-menu-right">\
						    	<a class="dropdown-item" href="#"><i class="la la-edit"></i> Edit Details</a>\
						    	<a class="dropdown-item" href="#"><i class="la la-leaf"></i> Update Status</a>\
						    	<a class="dropdown-item" href="#"><i class="la la-print"></i> Create Quizz </a>\
						  	</div>\
						</div>\
						<a href="#" class="m-portlet__nav-link btn m-btn m-btn--hover-accent m-btn--icon m-btn--icon-only m-btn--pill" title="View ">\
                            <i class="la la-edit"></i>\
                        </a>\
					';
				}
			}]
		});

		var query = datatable.getDataSourceQuery();

		$('#m_form_status').on('change', function () {
			datatable.search($(this).val(), 'Status');
		}).val(typeof query.Status !== 'undefined' ? query.Status : '');

		// $('#m_form_type').on('change', function () {
		// 	datatable.search($(this).val(), 'Type');
		// }).val(typeof query.Type !== 'undefined' ? query.Type : '');

		$('#m_form_status, #m_form_type').selectpicker();

	};

	return {
		//== Public functions
		init: function () {
			// init dmeo
			demo();
		}
	};
}();


jQuery(document).ready(function () {
	DatatableDataLocalDemo.init();
});