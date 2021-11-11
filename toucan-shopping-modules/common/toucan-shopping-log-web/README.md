# 犀鸟电商平台

    犀鸟电商平台致力于快速搭建属于自己的电商网站

## 模块介绍
    可动态刷新日志模块相关配置
    
## 接口介绍
    
     http://集成日志的服务IP:端口/toucan/log/config/email/flush
     请求头
     Content-Type:application/json;charset=UTF-8
     请求内容
    {
    	"email": {
    		"enabled": true,
    		"protocol": "smtp",
    		"receiverList": [{
    			"email": "695391446@qq.com",
    			"name": "majian"
    		}],
    		"ignoreExceptionList":[
    		    "com.toucan.shopping.modules.exception.user.UserHeaderException",
    		    "com.toucan.shopping.modules.exception.user.NotFountUserMainIdException",
    		    "com.toucan.shopping.modules.exception.user.NotFoundUserTokenException"
    		  ],
    		"sender": "mmdrss@163.com",
    		"senderAuthenticationCode": "IJHWJFZINFUPQIUC",
    		"smtp": {
    			"auth": "true",
    			"host": "smtp.163.com",
    			"port": "465",
    			"socketFactoryClass": "javax.net.ssl.SSLSocketFactory",
    			"socketFactoryFallback": "false"
    		},
    		"title": "——商城PC端——异常邮件"
    	}
    }