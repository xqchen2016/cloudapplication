$(function(){
	var table = $("#filelist");

	var loadList = function(){
		table.find('tr.file').remove();
		$.get('rest/index', function(r){
        		if(r.s==0 && r.d){
        			for(var i=0; i<r.d.length; ++i){
        				var item = r.d[i];
						table.append('<tr class="file"><td><a href="FileGetServlet?path='+item.name+'">'+item.name+'</a></td></tr>');
        			}
        		}
        });
	};

	loadList();
})


