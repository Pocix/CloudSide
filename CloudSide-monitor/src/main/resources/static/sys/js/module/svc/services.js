/**
 * http://usejsdoc.org/
 */

layui.use(['table','layer','laytpl','form'], function() {
    var table = layui.table,
        $ = layui.jquery,
        form = layui.form,
        laytpl = layui.laytpl;
    

	// 监听工具条
	table.on('tool(tables)', function(obj) { // 注：tool是工具条事件名，test是table原始容器的属性
												// lay-filter="对应的值"
		var data = obj.data // 获得当前行数据
		, layEvent = obj.event; // 获得 lay-event 对应的值
		if (layEvent === 'reg') {
			layer.open({
				type : 1,
				title : '注册主机',
				maxmin : true,
				offset : '100px',
				area : '500px',
				content : '<div id="view"></div>', // 这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
				success : function(layero, index) {
					var view = document.getElementById('view');
					laytpl(regHost.innerHTML).render(data,
							function(html) {
								view.innerHTML = html;
							});
					form.render(); // 用模板加入的无法刷新样式 ,须加此行代码.

					$('#view').html();
				},
				cancel : function(index, layero) {
					layer.close(index);
					return false;
				}
			});
		}
	});
  
	table.render({
	    elem: '#regTable'
	    ,url:'/svc/host/query'
	    ,cols: [[
	      {field:'id', width:80,title: 'ID'}
	      ,{field:'hostIp', width:120,title: 'IP',sort: true}
	      ,{field:'hostPort', width:80,title: '端口', sort: true}
	      ,{field:'hostName', width:160, title: '名称'}
	      ,{field:'serverAreaId',width:160, title: '服务域'} //minWidth：局部定义当前单元格的最小宽度，layui 2.2.1 新增
	      ,{field:'createDate', width:160,title: '创建时间', sort: true}
	      ,{field:'updateDate', width:160,title: '更新时间', sort: true}
	      ,{field:'remarks', width:160,title: '备注', sort: true}
	      ,{fixed: 'right', width:100, title: '操作',align:'center', toolbar: '#relation'}
	    ]]
  		,page: true
	  });
  	
	//监听工具条
      table.on('tool(regtables)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
        var data = obj.data //获得当前行数据
        ,layEvent = obj.event; //获得 lay-event 对应的值
        if(layEvent === 'relation'){
          layer.msg('查看操作');
        }
      });
	

	//监听提交
	form.on('submit(formDemo)', function(data) {
		$.ajax({
			type : 'POST',
			url : '/svc/host/save',
			dataType : 'json',
			data : data.field,
			async : true,
			success : function(result) {
				if (result.errcode == 0) {
					layer.msg('success:保存成功', {
						icon : 1,
						time : 800
					});
					setTimeout(function() {
						top.layer.closeAll('iframe');
						//刷新父亲的页面
						parent.refreshIframe();
					}, 1000);
				} else {
					console.alert(result.errcode);
					layer.msg('eles:保存失败！' + result.errmsg, {
						icon : 2,
						time : 1000
					});
				}
			},
			error : function(result, type) {
				layer.msg('error:保存失败！', {
					icon : 2,
					time : 1000
				});
				window.parent.location.reload(); //刷新父页面
				var index = parent.layer
						.getFrameIndex(window.name); //获取窗口索引
				parent.layer.close(index);
			}
		});
		console.log(data.field)
		return false;
	});
});