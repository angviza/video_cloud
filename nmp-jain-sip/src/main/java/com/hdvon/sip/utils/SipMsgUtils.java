package com.hdvon.sip.utils;

import org.springframework.util.StringUtils;

import com.hdvon.nmp.common.SipConstants;
import com.hdvon.nmp.enums.CruiseTypeEnum;
import com.hdvon.nmp.enums.PresetTypeEnum;
import com.hdvon.sip.entity.param.DragZoomParam;
import com.hdvon.sip.entity.param.DragZoomParam.DragZoomType;
import com.hdvon.sip.enums.DirectionEnum;
import com.hdvon.sip.enums.FocusEnum;
import com.hdvon.sip.enums.IrisEnum;
import com.hdvon.sip.enums.ZoomEnum;

/**
 * sip报文帮助类
 * 
 * @author wanshaojian
 *
 */
public class SipMsgUtils {

	private static final String BIT_1 = "A5", BIT_2 = "0F",BIT_3 = "00";
	
	/**
	 * 设备心跳报文
	 * @param sn sn编号
	 * @param checkUserCode
	 * @return
	 */
	public static String keepaliveMsg(final Long sn,final String checkUserCode) {
		String contents = "<?xml version=\"1.0\"?>\r\n"+ 
				"<Notify>\r\n" + 
				"<CmdType>Keepalive</CmdType>\r\n" + 
				"<SN>"+sn+"</SN>\r\n" + 
				"<DeviceID>"+checkUserCode+"</DeviceID>\r\n" + 
				"<Status>OK</Status>\r\n" + 
				"</Notify>";
		return contents;
	}
	
	/**
	 * 设备状态查询
	 * @param sn sn编号
	 * @param deviceCode
	 * @return
	 */
	public static String deviceStatusMsg(Long sn,String deviceCode) {
		String contents = "<?xml version=\"1.0\"?>\r\n"+ 
				"<Query>\r\n" + 
				"<CmdType>DeviceStatus</CmdType>\r\n" + 
				"<SN>"+sn+"</SN>\r\n" + 
				"<DeviceID>"+deviceCode+"</DeviceID>\r\n" + 
				"</Query>";
		return contents;
	}
	
	/**
	 * 设备预置位查询
	 * @param sn sn编号
	 * @param deviceCode
	 * @return
	 */
	public static String presetQueryMsg(Long sn,String deviceCode) {
		String contents = "<?xml version=\"1.0\"?>\r\n"+ 
				"<Query>\r\n" + 
				"<CmdType>PresetQuery</CmdType>\r\n" + 
				"<SN>"+sn+"</SN>\r\n" + 
				"<DeviceID>"+deviceCode+"</DeviceID>\r\n" + 
				"</Query>";
		return contents;
	}
	/**
	 * 视频点播报文
	 * 
	 * @param registerCode
	 *            注册用户编码
	 * @param host
	 *            媒体流接受端ip
	 * @param port
	 *            媒体流接受端端口
	 * @param transport
	 * 			      协议类型           
	 * @return
	 */
	public static String videoPlayMsg(final String registerCode, final String host,final Integer port,String transport) {
		/**
		 * 点播消息内容
		 */
		String sdpData = "v=0\r\n" + "o=" + registerCode + " 0 0 IN IP4 " + host + "\r\n" + "s=Play\r\n"
				+ "c=IN IP4 " + host + "\r\n" + "t=0 0\r\n";
		if(SipConstants.TRANSPORT_TCP.equals(transport.toLowerCase())) {
			sdpData += "m=video "+port+" TCP/RTP/AVP 96 98 97 99\r\n";
		}else {
			sdpData += "m=video "+port+" RTP/AVP 96 98 97 99\r\n";
		}				
		sdpData += "a=recvonly\r\n" + "a=rtpmap:96 PS/90000\r\n" + "a=rtpmap:98 H264/90000\r\n"
				+ "a=rtpmap:97 MPEG4/90000\r\n" + "a=rtpmap:99 SVAC/90000\r\n";
		if(SipConstants.TRANSPORT_TCP.equals(transport.toLowerCase())) {
			sdpData += "a=setup:active\r\n" + "a=connection:new";
		}
		return sdpData;
	}

	/**
	 * 视频回看报文
	 * 
	 * @param registerCode
	 *            注册用户
	 * @param host
	 *            媒体流接受端ip
	 * @param port
	 * 			 媒体流 接受端口
	 * @param URI
	 *            视音频文件的URI。该URI取值有两种方式：简捷方式和普通方式。简捷方式直接采用产生该历史媒体的媒体源（如某个摄像头）的设备ID（应符合6.1.2的规定）以及相关参数，参数用“：”分隔；普通方式采用http://存储设备ID[/文件夹]*
	 *            /文件名，[/文件夹]*为0－N级文件夹。
	 * @param startTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 * @param transport
	 * 			      协议类型          
	 * @return
	 */
	public static String videoBackMsg(final String registerCode, final String host,final Integer port, String URI,
			final String startTime, final String endTime,String transport) {
		/**
		 * 视频回看
		 */
		long start = CommonUtil.parseTimeSecondByFormat(startTime.replace("T", " "),CommonUtil.sdf1);
		long end = CommonUtil.parseTimeSecondByFormat(endTime.replace("T", " "),CommonUtil.sdf1);
		
		String sdpData = "v=0\r\n" + "o=" + registerCode + " 1 0 IN IP4 " + host + "\r\n" + "s=Playback\r\n" + "u="
				+ URI + "\r\n" + "c=IN IP4 " + host + "\r\n" + "t=" + start + " " + end + "\r\n";
				
		if(SipConstants.TRANSPORT_TCP.equals(transport.toLowerCase())) {
			sdpData += "m=video "+port+" TCP/RTP/AVP 96 98 97 99\r\n";
		}else {
			sdpData += "m=video "+port+" RTP/AVP 96 98 97 99\r\n";
		}				
		sdpData += "a=recvonly\r\n" + "a=rtpmap:96 PS/90000\r\n"
				+ "a=rtpmap:98 H264/90000\r\n" + "a=rtpmap:97 MPEG4/90000\r\n"+ "a=rtpmap:99 SVAC/90000\r\n";
		if(SipConstants.TRANSPORT_TCP.equals(transport.toLowerCase())) {
			sdpData += "a=setup:active\r\n" + "a=connection:new";
		}
		return sdpData;
	}

	/**
	 * 视频下载报文
	 * 
	 * @param registerCode
	 *            注册用户
	 * @param host
	 *            媒体流接受端ip
	 * @param port
	 *            媒体流接受端端口
	 * @param URI
	 *            视音频文件的URI。该URI取值有两种方式：简捷方式和普通方式。简捷方式直接采用产生该历史媒体的媒体源（如某个摄像头）的设备ID（应符合6.1.2的规定）以及相关参数，参数用“：”分隔；普通方式采用http://存储设备ID[/文件夹]*
	 *            /文件名，[/文件夹]*为0－N级文件夹。
	 * @param startTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 * @param scale
	 * 			  下载倍数           
	 * @return
	 */
	public static String downloadMsg(final String registerCode, final String host,final Integer port, final String URI,
			final String startTime, final String endTime, final int scale,String transport) {
		long start = CommonUtil.parseTimeSecondByFormat(startTime.replace("T", " "),CommonUtil.sdf1);
		long end = CommonUtil.parseTimeSecondByFormat(endTime.replace("T", " "),CommonUtil.sdf1);
		
		String sdpData = "v=0\r\n" + "o=" + registerCode + " 0 0 IN IP4 " + host + "\r\n" + "s=Download\r\n" + "u="
				+ URI + "\r\n" + "c=IN IP4 " + host + "\r\n" + "t=" + start + " " + end + "\r\n";
		if(SipConstants.TRANSPORT_TCP.equals(transport.toLowerCase())) {
			sdpData += "m=video "+port+" TCP/RTP/AVP 96 98 97 99\r\n";
		}else {
			sdpData += "m=video "+port+" RTP/AVP 96 98 97 99\r\n";
		}
		sdpData += "a=recvonly\r\n" + "a=rtpmap:96 PS/90000\r\n"
				+ "a=rtpmap:98 H264/90000\r\n" + "a=rtpmap:97 MPEG4/90000\r\n" + "a=rtpmap:99 SVAC/90000\r\n" + "a=downloadspeed:"+scale+"\r\n";
		if(SipConstants.TRANSPORT_TCP.equals(transport.toLowerCase())) {
			sdpData += "a=setup:active\r\n" + "a=connection:new";
		}
		return sdpData;
	}

	/**
	 * 录像快进或者慢进报文
	 * @param cseq cseq编号
	 * @param scaleType 快进和慢退的播放速率倍数
	 * @return
	 */
	public static String playbackFastForwardMsg(final Long cseq, final Double scaleType) {
		String msg = "PLAY RTSP/1.0\r\n" + "CSeq: " + cseq + "\r\n" + "Scale: " + scaleType;
		return msg;
	}

	/**
	 * 录像暂停报文
	 * @param cseq cseq编号
	 * @return
	 */
	public static String playbackPauseMsg(final Long cseq) {
		String msg = "PAUSE RTSP/1.0\r\n" + "CSeq: " + cseq + "\r\n" + "PauseTime: now";
		return msg;
	}

	/**
	 * 录像播放报文
	 * @param cseq cseq编号
	 * @return
	 */
	public static String playbackBroadcastMsg(final Long cseq) {
		String msg = "PLAY RTSP/1.0\r\n" + "CSeq: " + cseq + "\r\n" + "Range: npt=now-";
		return msg;
	}

	/**
	 * 录像随机播放报文
	 * @param cseq cseq编号
	 * @param randomTime 随机播放时长 (单位：秒)
	 * @return
	 */
	public static String playbackRandomBroadcastMsg(final Long cseq, final long randomTime) {
		String msg = "PLAY RTSP/1.0\r\n" + "CSeq: " + cseq + "\r\n" + "Range: npt=" + randomTime + "-";
		return msg;
	}

	/**
	 * 开始录像报文
	 * @param sn  sn编号
	 * @param deviceID 设备id
	 * @return
	 */
	public static String startVideotapeMsg(final Long sn, final String deviceID) {
		String contents = "<?xml version=\"1.0\"?>\n" + "<Control>\r\n" + "<CmdType>DeviceControl</CmdType>\r\n"
				+ "<SN>" + sn + "</SN>\r\n" + "<DeviceID>" + deviceID + "</DeviceID>\r\n"
				+ "<RecordCmd>Record</RecordCmd>\r\n" + "</Control>";
		return contents;
	}

	/**
	 * 停止录像报文
	 * @param sn sn编号
	 * @param deviceID 设备id
	 * @return
	 */
	public static String stopVideotapeMsg(final Long sn, final String deviceID) {
		String contents = "<?xml version=\"1.0\"?>\n" + "<Control>\r\n" + "<CmdType>DeviceControl</CmdType>\r\n"
				+ "<SN>" + sn + "</SN>\r\n" + "<DeviceID>" + deviceID + "</DeviceID>\r\n"
				+ "<RecordCmd>StopRecord </RecordCmd>\r\n" + "</Control>";

		return contents;
	}

	/**
	 * 云台控制报文
	 * @param sn sn编号
	 * @param ptzCmd PTZ指令举例
	 * @param deviceID 设备id
	 * @return
	 */
	public static String cloudControlMsg(final Long sn, final String ptzCmd, final String deviceID) {
		String contents = "<?xml version=\"1.0\"?>\n" + "<Control>\r\n" + "<CmdType>DeviceControl</CmdType>\r\n"
				+ "<SN>" + sn + "</SN>\r\n" + "<DeviceID>" + deviceID + "</DeviceID>\r\n" + "<PTZCmd>" + ptzCmd
				+ "</PTZCmd>\r\n" + "<Info>\r\n" + "<ControlPriority>5</ControlPriority>\r\n" + "</Info>\r\n"
				+ "</Control>\r\n";

		return contents;
	}
	/**
	 * 看守位控制
	 * @param deviceCode 设备编码
	 * @param enabled 看守位使能1：开启，0：关闭
	 * @param resetTime 自动归位时间间隔
	 * @param presetNum 预置位编号
	 * @param sn sn编码
	 * @return
	 */
	public static String homePositionMsg(final String deviceCode,final int enabled,final Integer resetTime,final Integer presetNum,final Long sn) {
		StringBuffer contents = new StringBuffer(); 
		contents.append("<?xml version=\"1.0\"?>\n"); 
		contents.append("<Control>\r\n");
		contents.append("<CmdType>DeviceControl</CmdType>\r\n");
		contents.append("<SN>"+sn+"</SN>\r\n");
		contents.append("<DeviceID>"+deviceCode+"</DeviceID>\r\n");
		contents.append("<HomePosition>\r\n");
		contents.append("<Enabled>"+enabled+"</Enabled>\r\n");
		if(resetTime!=null) {
			contents.append("<ResetTime>"+resetTime+"</ResetTime>\r\n");
		}
		if(presetNum!=null) {
			contents.append("<PresetIndex>"+presetNum+"</PresetIndex>\r\n");
		}
		contents.append("</HomePosition>\r\n");
		contents.append("</Control>");
		
		return contents.toString();
	}
	/**
	 * 3D拖放
	 * @param param
	 * @param sn
	 * @return
	 */
	public static String dragZoomMsg(DragZoomParam param,final Long sn) {
		StringBuffer contents = new StringBuffer(); 
		contents.append("<?xml version=\"1.0\"?>\n"); 
		contents.append("<Control>\r\n");
		contents.append("<CmdType>DeviceControl</CmdType>\r\n");
		contents.append("<SN>"+sn+"</SN>\r\n");
		contents.append("<DeviceID>"+param.getDeviceID()+"</DeviceID>\r\n");
		
		String s ="<Length>"+param.getLength()+"</Length>\r\n" + 
				"<Width>"+param.getWidth()+"</Width>\r\n" + 
				"<MidPointX>"+param.getMidPointX()+"</MidPointX>\r\n" + 
				"<MidPointY>"+param.getMidPointY()+"</MidPointY>\r\n" + 
				"<LengthX>"+param.getLengthX()+"</LengthX>\r\n" + 
				"<LengthY>"+param.getLengthY()+"</LengthY>\r\n";
		if(DragZoomType.DRAGZOOMIN.getKey().equals(param.getType())) {
			contents.append("<DragZoomIn>\r\n");
			contents.append(s);
			contents.append("</DragZoomIn>\r\n");
		}else {
			contents.append("<DragZoomOut>\r\n");
			contents.append(s);
			contents.append("</DragZoomOut>\r\n");
		}
		contents.append("</Control>");

		return contents.toString();
	}
	/**
	 * 查询录像报文
	 * @param deviceCode 设备编码
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @param sn sn编码
	 * @return
	 */
	public static String searchMediaMsg(final String deviceCode,final String startTime,final String endTime,final Long sn) {
		String contents = "<?xml version=\"1.0\"?>\r\n"+ 
				"<Query>\r\n" + 
				"<CmdType>RecordInfo</CmdType>\r\n" + 
				"<SN>"+sn+"</SN>\r\n" + 
				"<DeviceID>"+deviceCode+"</DeviceID>\r\n" ;
		if(!StringUtils.isEmpty(startTime)) {
			contents += "<StartTime>"+startTime.replace(" ", "T")+"</StartTime>\r\n"; 
		}
		if(!StringUtils.isEmpty(endTime)) {
			contents += "<EndTime>"+endTime.replace(" ", "T")+"</EndTime>\r\n";
		}				
		contents +="<Type>all</Type>\r\n"  
				 +"<RecordPos>2</RecordPos>\r\n" 
				 + "</Query>";
		
		return contents;
	}
	

	/**
	 * 查询日志位
	 * @param sn sn编号
	 * @param deviceID 设备id
	 * @return
	 */
	public static String searchPresetMsg(final Long sn,final String deviceID) {
		String contents = "<?xml version=\"1.0\"?>\r\n"+ 
				"<Notify>\r\n" + 
				"<CmdType>PresetQuery</CmdType>\r\n" + 
				"<SN>"+sn+"</SN>\r\n" + 
				"<DeviceID>"+deviceID+"</DeviceID>\r\n" + 
				"</Notify>";
		return contents;
	}
	/**
	 * 生成PTZCmd值
	 * 
	 * @param zoomType
	 * @param speed
	 *            步长
	 * @return
	 */
	public static String genZoomPTZCmd(final int zoomType,final  int speed) {
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append(BIT_3);
				
		ZoomEnum em = ZoomEnum.getObjectByKey(zoomType);
		String  bit4 = em.getValue(), bit5 = "00", bit6 = "00", bit7 = "00";

		if(!ZoomEnum.STOP.equals(em)){
			byte b1 = (byte) speed;
			int height = (b1 & 0xf0)>>4;
			bit7 = HexData.numToHex8(height);
		}
		ptzCmd.append(bit4).append(bit5).append(bit6).append(bit7);
		ptzCmd.append(genPtzCmdBit8(bit4, bit5, bit6, bit7));
		return ptzCmd.toString();
	}
	
	/**
	 * 生成PTZCmd值
	 * 
	 * @param irisType
	 * @param speed
	 *            步长
	 * @return
	 */
	public static String genIrisPTZCmd(final int irisType,final  int speed) {
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append(BIT_3);
		
		IrisEnum em = IrisEnum.getObjectByKey(irisType);
		String bit4 = em.getValue(), bit5 = "00", bit6 = "00", bit7 = "00";

		if(!IrisEnum.STOP.equals(speed)){
			//镜头以字节6的数值放大光圈
			bit6 = HexData.numToHex8(speed);
		} 
		ptzCmd.append(bit4).append(bit5).append(bit6).append(bit7);
		ptzCmd.append(genPtzCmdBit8(bit4, bit5, bit6, bit7));
		return ptzCmd.toString();
	}
	
	/**
	 * 生成PTZCmd值
	 * 
	 * @param focusType
	 * @param speed
	 *            步长
	 * @return
	 */
	public static String genFocusPTZCmd(final int focusType,final  int speed) {
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append(BIT_3);
		FocusEnum em = FocusEnum.getObjectByKey(focusType);
		String bit4 = em.getValue(), bit5 = "00", bit6 = "00", bit7 = "00";
		if(!FocusEnum.STOP.equals(em)) {
			//镜头以字节5的数值聚焦远
			bit5 = HexData.numToHex8(speed);
		}
		ptzCmd.append(bit4).append(bit5).append(bit6).append(bit7);
		ptzCmd.append(genPtzCmdBit8(bit4, bit5, bit6, bit7));
		return ptzCmd.toString();
	}
	/**
	 * 生成PTZCmd值
	 * 
	 * @param directionType
	 * @param speed
	 *            步长
	 * @return
	 */
	public static String genDirectionPTZCmd(final String directionType,final int speed) {
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append(BIT_3);
		
		DirectionEnum em = DirectionEnum.getObjectByKey(directionType);
		String bit4 = em.getValue(), bit5 = "00", bit6 = "00", bit7 = "00";

		if (DirectionEnum.UP.equals(em) || DirectionEnum.DOWN.equals(em)) {
			bit6 = HexData.numToHex8(speed);
		}  else if (DirectionEnum.LEFT.equals(em) || DirectionEnum.RIGHT.equals(em)) {
			bit5 = HexData.numToHex8(speed);
		}  else if(DirectionEnum.STOP.equals(em)){
		} else{
			bit5 = HexData.numToHex8(speed);
			bit6 = HexData.numToHex8(speed);
		}
		ptzCmd.append(bit4).append(bit5).append(bit6).append(bit7);
		ptzCmd.append(genPtzCmdBit8(bit4, bit5, bit6, bit7));
		
		return ptzCmd.toString();
	}
	/**
	 * 生成指定字节8
	 * @param bit4
	 * @param bit5
	 * @param bit6
	 * @param bit7
	 * @return
	 */
	private static String genPtzCmdBit8(String bit4,String bit5,String bit6,String bit7) {
		int checkCode = Integer.parseInt(BIT_1, 16) + Integer.parseInt(BIT_2, 16) + Integer.parseInt(BIT_3, 16)
		+ Integer.parseInt(bit4, 16) + Integer.parseInt(bit5, 16) + Integer.parseInt(bit6, 16)
		+ Integer.parseInt(bit7, 16);

		checkCode = checkCode % 256;
		
		String bit8 = HexData.numToHex8(checkCode);
		return bit8;
	}
	
	/**
	 * 生成指定字节8
	 * @param bit4
	 * @param bit5
	 * @param bit6
	 * @param bit7
	 * @return
	 */
	private static String genCruisePtzCmdBit8(String bit4,String bit5,String bit6,String bit7) {
		int checkCode = Integer.parseInt(BIT_1, 16) + Integer.parseInt(BIT_2, 16) + Integer.parseInt("01", 16)
		+ Integer.parseInt(bit4, 16) + Integer.parseInt(bit5, 16) + Integer.parseInt(bit6, 16)
		+ Integer.parseInt(bit7, 16);

		checkCode = checkCode % 256;
		
		String bit8 = HexData.numToHex8(checkCode);
		return bit8;
	}
	/**
	 * 预置位生成PTZCmd值
	 * 
	 * @param presetType
	 * @param presetNum 预置位编号 0-255
	 * @return
	 */
	public static String genPresetPTZCmd(final String presetType,final int presetNum) {
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append(BIT_3);
		
		PresetTypeEnum em = PresetTypeEnum.getObjectByKey(presetType);
		String bit4 = em.getValue(), bit5 = "00", bit6 = "00", bit7 = "00";
		
		bit6 = HexData.numToHex8(presetNum);
		ptzCmd.append(bit4).append(bit5).append(bit6).append(bit7);
		ptzCmd.append(genPtzCmdBit8(bit4, bit5, bit6, bit7));
		return ptzCmd.toString();
	}
	/**
	 * 巡航指令生成PTZCmd值
	 * 
	 * @param cruiseType
	 * @param groupNum 巡航组号
	 * @param presetNum 预置位编号 0-255
	 * @return
	 */
	public static String genCruisePTZCmd(final String cruiseType, final Integer groupNum,final Integer presetNum,final Long stayTime,final Integer  speed) {
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append("01");
		
		CruiseTypeEnum em = CruiseTypeEnum.getObjectByKey(cruiseType);
		String bit4 = em.getValue(),
		bit5 = HexData.numToHex8(groupNum),
		bit6 = "00", bit7 = "00";
		//字节5表示巡航组号，字节6表示预置位号
		if(CruiseTypeEnum.ADD.equals(em)) {
			bit6 = HexData.numToHex8(presetNum);
		}else if(CruiseTypeEnum.DEL.equals(em)) {
			bit6 = "00";
		}else if(CruiseTypeEnum.SET_SPEED.equals(em)) {			
			/*
			 * 设置巡航速度
			 * 字节6表示数据的低8位  字节7的高4位表示数据的高4位。
			 */
			byte b1 = (byte) speed.intValue();
			int height = (b1 & 0xf0)>>4;
			int lower = b1 & 0xf0;
			bit6 = HexData.numToHex8(lower);
			bit7 = HexData.numToHex8(height);
		}else if(CruiseTypeEnum.SET_STOPOVER.equals(em)) {
			/* 
			 * 设置巡航停留时间
			 * 字节6表示数据的低8位  字节7的高4位表示数据的高4位。
			 */
			byte b1 = (byte) stayTime.intValue();
			int height = (b1 & 0xf0)>>4;
			int lower = b1 & 0xf0;
			bit6 = HexData.numToHex8(lower);
			bit7 = HexData.numToHex8(height);
		}else if(CruiseTypeEnum.START.equals(em)) {
			bit6 = "00";
			bit7 = "00";
		}else if(CruiseTypeEnum.STOP.equals(em)){
			//停止巡航
			bit4 = "00";
		}
		ptzCmd.append(bit4).append(bit5).append(bit6).append(bit7);
		ptzCmd.append(genCruisePtzCmdBit8(bit4, bit5, bit6, bit7));
		
		return ptzCmd.toString();
	}
	
	public static void main(String[] args) {
		String ptzCmd = SipMsgUtils.genDirectionPTZCmd(DirectionEnum.UPPER_LEFT.getKey(), 3);
		System.out.println(ptzCmd);

		System.out.println(HexData.numToHex8(29));
	}
}
