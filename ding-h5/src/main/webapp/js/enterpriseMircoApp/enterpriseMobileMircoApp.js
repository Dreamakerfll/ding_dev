/**
 * Created by liqiao on 8/10/15.
 */

/**
 * _config comes from server-side template. see views/index.jade
 */

dd.config({
			agentId : _config.agentid,
			corpId : _config.corpId,
			timeStamp : _config.timeStamp,
			nonceStr : _config.nonceStr,
			signature : _config.signature,
			jsApiList : [ 'runtime.info', 'biz.contact.choose',
					'device.notification.confirm', 'device.notification.alert',
					'device.notification.prompt', 'biz.ding.post',
					'biz.util.openLink','biz.contact.departmentsPicker','biz.contact.complexPicker','biz.contact.createGroup',
					'ui.pullToRefresh.disable','biz.user.get']
		});

var userInfo = {"active":true,"avatar":"","department":[1],"dingId":"$:LWCP_v1:$92PlFl9CqyALbbs0E+6qOA==","isAdmin":true,"isBoss":false,"isHide":false,"isLeaderInDepts":"{1:false}","jobnumber":"","mobile":"","name":"","orderInDepts":"{}","senior":false,"userid":""};

dd.ready(function() {
	
    dd.biz.navigation.setTitle({
        title: '不是不想胖',
        onSuccess: function(data) {
        },
        onFail: function(err) {
            console.log(JSON.stringify(err));
        }
    });
//	 alert('dd.ready rocks!');

	dd.runtime.info({
		onSuccess : function(info) {
			console.log('runtime info: ' + JSON.stringify(info));
		},
		onFail : function(err) {
			console.log('fail: ' + JSON.stringify(err));
		}
	});
	dd.ui.pullToRefresh.enable({
	    onSuccess: function() {
	    },
	    onFail: function() {
	    }
	})

	dd.biz.navigation.setMenu({
		backgroundColor : "#ADD8E6",
		items : [
			{
				id:"此处可以设置帮助",//字符串
			// "iconId":"file",//字符串，图标命名
			  text:"帮助"
			}
			,
			{
				"id":"2",
			"iconId":"photo",
			  "text":"我们"
			}
			,
			{
				"id":"3",
			"iconId":"file",
			  "text":"你们"
			}
			,
			{
				"id":"4",
			"iconId":"time",
			  "text":"他们"
			}
		],
		onSuccess: function(data) {
			alert(JSON.stringify(data));

		},
		onFail: function(err) {
			alert(JSON.stringify(err));
		}
	});
	
    dd.runtime.permission.requestAuthCode({
        corpId : _config.corpId,
        onSuccess : function(info) {
//		alert('authcode: ' + info.code);
            console.log(info.code);
            $.ajax({
                url : 'userinfo?code=' + info.code + '&corpid='
                + _config.corpId,
                type : 'GET',
                success : function(data, status, xhr) {
                    var info = JSON.parse(data);
                    userInfo = info;
                    //禁止下拉刷新
                	dd.ui.pullToRefresh.disable();
//                    this.state.userInfo = info;
//
//                    $('.user_name').html(info.name);
//                    // 图片
//                    if(info.avatar.length != 0){
//                        $('.user_img').css({'background-image':'url('+info.avatar+')'});
//                    }

                },
                error : function(xhr, errorType, error) {
                    console.log("yinyien:" + _config.corpId);
                    alert(errorType + ', ' + error);
                }
            });

        },
        onFail : function(err) {
            alert('fail: ' + JSON.stringify(err));
        }
    });

});

dd.error(function(err) {
	alert('dd error: ' + JSON.stringify(err));
});
