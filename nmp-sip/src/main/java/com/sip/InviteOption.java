package com.sip;

import lombok.Data;

@Data
public class InviteOption {
	
	/**
	 * SIP:本地信令账号@域，例： sip:34020000001320000009@3402000000
	 */
	private String from;
	/**
	 * SIP:目标信令通道@设备所在域，例：sip:34020000001320000010@3402000000
	 */
	private String to;
	/**
	 * SIP：本地信令账号@本地信令IP地址：端口，例：sip:34020000001320000009@192.168.2.112:5060
	 */
	private String contact;
	/**
	 * SIP：信令中心ID@信令中心IP：端口，例：sip:34020000002000000001@192.168.2.169:5060
	 */
	private String route;
	/**
	 *  目标信令通道：标识,信令中心ID：标识，例：34020000001320000010:01,34020000002000000001:02
	 *  媒体流发送方序列号：发送方媒体流序列号为不超过20位的字符串；当请求为实时视频时，首位取值为0，对于相同的实时视频取值唯一；当请求的媒体流为历史视频时，首位取值为1，对于每一路历史视频取值唯一。
	 *  媒体流接收者ID：为符合附录D定义的媒体流接收者的ID编码。
	 *  接收方媒体流序列号：为媒体流接收端的标识序列号，在同一时刻该序列号在媒体流接收者端为不重复的字符串。当接收者为客户端时，可以作为窗口的标识符。
	 *  34020000001320000010是目标信令通道ID，即发送者编码(设备编码)
     *  34020000002000000001是信令中心ID，即符合附录D定义的媒体流接收者的编码
	 */
	private String subject;
	/**
	 * o 分别是媒体流发送者的设备编码，版本号，版本号，发起invite的IP地址，由c++配置提供
	 * s Play代表实时点播；Playback代表历史回放；Download代表文件下载；Talk代表语音对讲
	 * c invite的IP地址
	 * t 回放或下载时， t行值为开始时间和结束时间，用空格分隔，时间格式采用UNIX时间戳，即从1970年1月1日开始的相对时间。
	 *   开始时间和结束时间均为要回放或下载的音视频文件录制时间段中的某个时刻
	 *   
	 * m 描述媒体的媒体类型，端口，传输层协议，负载类型等内容。媒体类型采用video标识传输视频或视音频混合内容，采用audio标识传输音频内容；
	 *   传输方式：采用RTP/AVP标识传输层协议为RTP，基于UDP协议之上；采用TCP/RTP/AVP标识传输层协议为TCP，基于TCP协议之上。
	 *  
	 * u 视音频文件的URI，在录像回放或下载的时候需要用到。该URI取值有两种方式：简捷方式和普通方式。简捷方式：直接采用产生该历史媒体的媒体源（如某个摄像头）的设备ID（应符合6.1.2的规定）以及相关参数，参数用“：”分隔；
	 *   普通方式采用http://存储设备ID[/文件夹]* /文件名，[/文件夹]*为0－N级文件夹。目前可以先定义为：历史媒体的设备（摄像头）编码+":255"，其中255是海康定义的。
	 * 
     *  "v=0\r\n"+
	 *  "o=34020000001320000010 0 0 IN IP4 172.31.108.1\r\n"+
	 *  "s=Play\r\n"+
	 *  "c=IN IP4 172.31.108.1\r\n"+
	 *  "t=0 0\r\n"+
	 *  "m=video 6000 RTP/AVP 96 97 98 99\r\n"+
	 *  "a=recvonly\r\n"+
	 *  "a=rtpmap:96 PS/90000\r\n"+
	 *  "a=rtpmap:97 MPEG4/90000\r\n"+
	 *  "a=rtpmap:98 H264/90000\r\n"+
	 *  "a=rtpmap:99 SVAC/90000\r\n"+
	 *  ""
	 */
	private String body;
	/**
	 * Invite的超时时间
	 */
	private int expire;
	
}
