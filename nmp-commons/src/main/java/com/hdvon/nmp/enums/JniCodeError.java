package com.hdvon.nmp.enums;

/**
 * 调用信令错误返回信息
 *
 */
public enum JniCodeError {
	
	JniCode_Error_100(100,"","说明caller正在呼叫，但还没联系上callee"),
	JniCode_Error_180(180,"","说明callee已经被联系上,callee的铃正在响.收到这个信息后，等待200 OK"),
	JniCode_Error_181(181,"","说明call被重新路由到另外一个目的地"),
	JniCode_Error_182(182,"请稍后重试 ","说明callee当前是不可获得的，但是对方不想直接拒绝呼叫，而是选择放在呼叫队列"),
	JniCode_Error_183(183,"","用来警告caller频段(inband)错误。当从PSTN收到一个ISDN消息，SIP gateway"),
	JniCode_Error_300(300,"","说明呼叫的地址被解析成多个地址，所有的地址都被提供出来，用户或用户代理可以从中选择联系哪个"),
	JniCode_Error_301(301,"","说明指定地址的用户已经永远不可用，在头中已经用另外一个地址替换了"),
	JniCode_Error_302(302,"","说明指定地址的用户临时不可用，在头中已经用另外一个地址代替了"),
	JniCode_Error_305(305,"","说明caller必须用一个proxy来联系callee"),
	JniCode_Error_380(380,"","说明call不成功，但是可选择其他的服务"),
	JniCode_Error_400(400,"非法的请求格式","说明由于非法格式，请求不能被理解"),
	JniCode_Error_401(401,"用户认证未通过","说明请求需要用户认证"),
	JniCode_Error_402(402,"会话需要付费","说明完成会话需要付费"),
	JniCode_Error_403(403,"拒接提供服务","说明server已经收到并能理解请求但不提供服务"),
	JniCode_Error_404(404,"请求资源不存在","说明server有明确的信息在指定的域中用户不存在"),
	JniCode_Error_405(405,"权限不足","说明请求中指定的方法是不被允许的。将返回一个允许的方法列表"),
	JniCode_Error_406(406,"请求资源受限","说明被请求的资源只对某些特殊的请求作出响应，对目前头(header)中指定的请求不接受"),
	JniCode_Error_407(407,"用户认证未通过","和401 Unauthorized response相似.但是，它说明client必须首先在proxy上认证"),
	JniCode_Error_408(408,"服务请求超时","说明在timeout时间过期前，server不能产生响应"),
	JniCode_Error_409(409,"请稍后重试","说明由于和当前资源状态产生冲突，请求不能被处理"),
	JniCode_Error_410(410,"服务不可用","说明请求资源在server上永远不可用，也没有转发的地址"),
	JniCode_Error_411(411,"参数错误","说明用户拒绝接受没有定义content长度的请求"),
	JniCode_Error_413(413,"请稍后重试","说明server拒绝处理请求，因为它太大，超过了server能处理的大小。"),
	JniCode_Error_414(414,"参数错误","说明server拒绝处理请求，因为请求的URI太长，server不能解释它"),
	JniCode_Error_415(415,"参数错误","说明server拒绝处理请求，因为body格式不被目的终端支持"),
	JniCode_Error_420(420,"参数错误","说明server不能理解在header中指出的扩展协议"),
	JniCode_Error_480(480,"请稍后重试","说明server正在忽略请求，由于它是一个没有匹配legID的BYE或者是一个没有匹配事务的CANCEL"),
	JniCode_Error_481(481,"参数错误","服务正在忽略请求，由于它是一个没有匹配legID的BYE或者是一个没有匹配事务的CANCEL"),
	JniCode_Error_482(482,"参数错误","说明server收到了一个包含它自己路径的请求"),
	JniCode_Error_483(483,"参数错误","说明server收到了一个请求，它需要的hop数超过了在header中允许的最大hop数"),
	JniCode_Error_484(484,"参数错误","说明server收到一个地址不完整的请求"),
	JniCode_Error_485(485,"参数错误","说明server收到一个请求，其中callee的地址是不明确的，也没有可能备用的地址供选择。"),
	JniCode_Error_486(486,"请稍后重试","说明callee已经被联系上，但是它们的系统不能承受额外的call"),
	JniCode_Error_488(488,"请稍后重试","暂时不能进行"),
	JniCode_Error_500(500,"信令服务内部错误","说明server或gateway发生意外错误从而不能处理请求"),
	JniCode_Error_501(501,"信令服务内部错误","说明server或gateway不支持完成请求所需的功能"),
	JniCode_Error_502(502,"信令服务内部错误","说明server或gateway从下游server收到一个非法响应"),
	JniCode_Error_503(503,"请稍后重试","说明由于超负载或维护问题，server或gateway不能处理请求"),
	JniCode_Error_504(504,"信令服务内部错误","说明server或gateway没有从另外一个server(如location server)收到一个及时的"),
	JniCode_Error_505(505,"信令服务内部错误","说明server或gateway不支持在请求中用到的SIP版本"),
	JniCode_Error_600(600,"服务器正忙","说明callee已经被联系上，但是处于忙状态中，在这个时间不能接受call"),
	JniCode_Error_603(603,"","说明callee已经被联系上，但是不能或不想加入call"),
	JniCode_Error_604(604,"","说明server有正式的信息说明callee不存在于网络中"),
	JniCode_Error_606(606,"服务器停止响应",""),
	JniCode_Error_1000(1000,"服务启动失败,请联系系统管理员","服务启动失败,请检查本地IP地址和端口号设置"),
	JniCode_Error_1001(1001,"请求连接超时,请保持网络畅通后重试",""),
	JniCode_Error_1002(1002,"服务启动异常,请稍后重试",""),
	JniCode_Error_1003(1003,"请求超时,请稍后重试",""),
	JniCode_Error_1004(1004,"请求失败,请稍后重试",""),
	JniCode_Error_1005(1005,"播放失败,请稍后重试","无效的IP地址/端口号/播放接口调用异常"),
	JniCode_Error_1006(1006,"账号注册失败",""),
	JniCode_Error_1007(1007,"账号注销失败",""),
	JniCode_Error_1008(1008,"视频停止播放失败",""),
	JniCode_Error_1009(1009,"视频播放响应失败",""),
	JniCode_Error_1010(1010,"摄像头控制失败",""),
	JniCode_Error_1011(1011,"服务异常,请稍后重试",""),
	JniCode_Error_1012(1012,"信令服务器异常",""),
	JniCode_Error_1013(1013,"该时间段没有录像",""),
	JniCode_Error_1100(1100,"","未知错误类型");
   
   private int code; //编码
   private String msg; //前端打印信息 
   private String dec;  //调用信令错误描述
     
  
   private JniCodeError(int code,String msg,String dec){  
       this.code=code;  
       this.msg=msg;  
       this.dec=dec;
   }  
   
   private JniCodeError(int code,String msg){  
       this.code=code;  
       this.msg=msg;  
   }
     
   public String getMsg(){  
       return this.msg;  
   } 
   
   public int getCode() {  
     return this.code;  
  } 
   
   
   public String getDec(){  
       return this.dec;  
   } 
   
   
   /**
    * 根据编码匹配对应错误类型，否则返回未知错误
    * @param code
    * @return
    */
   public static JniCodeError getEnumByCode(int code){  
	   for(JniCodeError errorEnum : JniCodeError.values()){  
		   if(code ==errorEnum.getCode()) {
			   return errorEnum;
		   }
	   }  
	   return JniCode_Error_1100;  
	 } 

}
