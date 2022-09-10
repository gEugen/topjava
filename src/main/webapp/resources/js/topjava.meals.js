const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return data.replace("T", " ");
                        }
                        return data;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                if (data.excess) {
                    $(row).attr("data-meal-excess", true);
                } else {
                    $(row).attr("data-meal-excess", false);
                }
            }
        })
    );
});

$(function(){
    $.datetimepicker.setLocale(navigator.language.substring(0, 2));
    $("#dateTime").datetimepicker({
        format:'Y-m-d H:i'
    });
});

$(function(){
    $("#startDate").datetimepicker({
        timepicker:false,
        format:'Y-m-d'
    });
});

$(function(){
    $("#endDate").datetimepicker({
        timepicker:false,
        format:'Y-m-d'
    });
});

$(function(){
    $("#startTime").datetimepicker({
        datepicker:false,
        format:'H:i'
    });
});

$(function(){
    $("#endTime").datetimepicker({
        datepicker:false,
        format:'H:i'
    });
});