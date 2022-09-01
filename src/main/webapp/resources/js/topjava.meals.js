const mealsAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl
};

function dataFilter() {
    $.ajax({
        type: "GET",
        url: mealsAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByFilter);
}
function updateTableByFilter(data) {
    $("#datatable").dataTable().api().clear().rows.add(data).draw();
}

function filterReset() {
    $("#filter")[0].reset();
    $.get(mealsAjaxUrl, dataFilter);
}

// $(document).ready(function () {
$(function () {
    makeEditable({
        ajaxUrl: mealsAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "dec"
                ]
            ]
        }),
    });
});