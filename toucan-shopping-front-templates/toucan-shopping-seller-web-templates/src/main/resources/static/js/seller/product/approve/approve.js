
$(function () {


    $("#queryBtn").bind( 'click' ,function(){

    });


    $("#resetBtn").bind( 'click' ,function(){
        $("#productApproveForm").resetForm();
    });

    $('#startDate').datetimepicker({
        format:'Y-m-d H:i'
    });//初始化
    $('#endDate').datetimepicker({
        format:'Y-m-d H:i'
    });//初始化

    $.datetimepicker.setLocale('zh');//使用中文
});