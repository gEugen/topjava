const mealsAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl
};

function updateTable() {
    $.ajax({
        type: "GET",
        url: mealsAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(refreshTableView);
}

function filterReset() {
    $("#filter")[0].reset();
    updateTable();
}

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
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
                    "desc"
                ]
            ]
        }),
    );
});