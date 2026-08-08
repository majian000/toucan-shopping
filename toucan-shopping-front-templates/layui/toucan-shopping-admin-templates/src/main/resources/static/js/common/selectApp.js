//
// function initSelectApp()
// {
//     layui.jquery.ajax({
//         url:ctxPath+"common/select/app/list",
//         contentType: "application/json; charset=utf-8",
//         type:'GET',
//         data:null,
//         success:function(data){
//             if(data.code!=1)
//             {
//                 layer.msg("初始化应用列表失败");
//                 return;
//             }
//             if(data.data.length>0)
//             {
//                 layui.jquery("#selectApp").empty();
//                 layui.jquery("#selectApp").append("<option selected='selected'  value='-1'>请选择</option>");
//                 for(var i=0;i<data.data.length;i++)
//                 {
//                     var app = data.data[i];
//                     layui.jquery("#selectApp").append("<option value='"+app.appCode+"'>"+app.name+"("+app.appCode+")</option>");
//                 }
//             }
//
//         },
//         complete:function(data)
//         {
//         }
//     });
// }