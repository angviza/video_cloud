package com.hdvon.sip.app;

import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.HexData;
import com.hdvon.sip.vo.CloudControlQuery.DirectionEnum;
import com.hdvon.sip.vo.CloudControlQuery.FocusEnum;
import com.hdvon.sip.vo.CloudControlQuery.IrisEnum;
import com.hdvon.sip.vo.CloudControlQuery.ZoomEnum;
import com.hdvon.sip.vo.CruiseQuery.CruiseTypeEnum;
import com.hdvon.sip.vo.PresetQuery.PresetTypeEnum;
import com.hdvon.sip.vo.VideoControlType.ScaleEnum;

/**
 * 媒体报文帮助类
 * 
 * @author wanshaojian
 *
 */
public class MediaMsgUtil {

	private static final String BIT_1 = "A5", BIT_2 = "0F",BIT_3 = "00";
	/**
	 * 视频点播报文
	 * 
	 * @param registerCode
	 *            注册用户编码
	 * @param receiveIp
	 *            媒体流接受端ip
	 * @param receivePort
	 *            媒体流接受端端口
	 * @return
	 */
	public static String videoPlayMsg(final String registerCode, final String receiveIp,final Integer receivePort) {
		/**
		 * 点播消息内容
		 */
		String sdpData = "v=0\r\n" + "o=" + registerCode + " 0 0 IN IP4 " + receiveIp + "\r\n" + "s=Play\r\n"
				+ "c=IN IP4 " + receiveIp + "\r\n" + "t=0 0\r\n" + "m=video "+receivePort+" RTP/AVP 96 98 97\r\n"
				+ "a=recvonly\r\n" + "a=rtpmap:96 PS/90000\r\n" + "a=rtpmap:98 H264/90000\r\n"
				+ "a=rtpmap:97 MPEG4/90000";
		return sdpData;
	}

	/**
	 * 视频回看报文
	 * 
	 * @param registerCode
	 *            注册用户
	 * @param receiveIp
	 *            媒体流接受端ip
	 * @param receivePort
	 * 			 媒体流 接受端口
	 * @param URI
	 *            视音频文件的URI。该URI取值有两种方式：简捷方式和普通方式。简捷方式直接采用产生该历史媒体的媒体源（如某个摄像头）的设备ID（应符合6.1.2的规定）以及相关参数，参数用“：”分隔；普通方式采用http://存储设备ID[/文件夹]*
	 *            /文件名，[/文件夹]*为0－N级文件夹。
	 * @param startTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static String videoBackMsg(final String registerCode, final String receiveIp,final Integer receivePort, String URI,
			final Long startTime, final Long endTime) {
		String sdpData = "v=0\r\n" + "o=" + registerCode + " 1 0 IN IP4 " + receiveIp + "\r\n" + "s=Playback\r\n" + "u="
				+ URI + "\r\n" + "c=IN IP4 " + receiveIp + "\r\n" + "t=" + startTime + " " + endTime + "\r\n"
				+ "m=video "+receivePort+" RTP/AVP 96 98 97\r\n" + "a=recvonly\r\n" + "a=rtpmap:96 PS/90000\r\n"
				+ "a=rtpmap:98 H264/90000\r\n" + "a=rtpmap:97 MPEG4/90000";
		return sdpData;
	}

	/**
	 * 视频下载报文
	 * 
	 * @param registerCode
	 *            注册用户
	 * @param receiveIp
	 *            媒体流接受端ip
	 * @param receivePort
	 *            媒体流接受端端口
	 * @param URI
	 *            视音频文件的URI。该URI取值有两种方式：简捷方式和普通方式。简捷方式直接采用产生该历史媒体的媒体源（如某个摄像头）的设备ID（应符合6.1.2的规定）以及相关参数，参数用“：”分隔；普通方式采用http://存储设备ID[/文件夹]*
	 *            /文件名，[/文件夹]*为0－N级文件夹。
	 * @param startTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 * @return
	 */
	public static String downloadMsg(final String registerCode, final String receiveIp,final Integer receivePort, final String URI,
			final Long startTime, final Long endTime) {
		String sdpDate = "v=0\r\n" + "o=" + registerCode + " 0 0 IN IP4 " + receiveIp + "\r\n" + "s=Download\r\n" + "u="
				+ URI + "\r\n" + "c=IN IP4 " + receiveIp + "\r\n" + "t=" + startTime + " " + endTime + "\r\n"
				+ "m=video "+receivePort+" RTP/AVP 96 98 97\r\n" + "a=recvonly\r\n" + "a=rtpmap:96 PS/90000\r\n"
				+ "a=rtpmap:98 H264/90000\r\n" + "a=rtpmap:97 MPEG4/90000\r\n" + "a=downloadspeed:4";
		return sdpDate;
	}

	/**
	 * 录像快进或者慢进报文
	 * 
	 * @param invco
	 *            cseq编号
	 * @param em
	 *            快进和慢退的播放速率倍数枚举类
	 * @return
	 */
	public static String playbackFastForwardMsg(final Long invco, final ScaleEnum em) {
		String msg = "PLAY RTSP/1.0\r\n" + "CSeq: " + invco + "\r\n" + "Scale: " + em.getValue();
		return msg;
	}

	/**
	 * 录像暂停报文
	 * 
	 * @param invco
	 *            cseq编号
	 * @return
	 */
	public static String playbackPauseMsg(final Long invco) {
		String msg = "PAUSE RTSP/1.0\r\n" + "CSeq: " + invco + "\r\n" + "PauseTime: now";
		return msg;
	}

	/**
	 * 录像播放报文
	 * 
	 * @param invco
	 *            cseq编号
	 * @return
	 */
	public static String playbackBroadcastMsg(final Long invco) {
		String msg = "PLAY RTSP/1.0\r\n" + "CSeq: " + invco + "\r\n" + "Range: npt=now-";
		return msg;
	}

	/**
	 * 录像随机播放报文
	 * 
	 * @param invco
	 *            cseq编号
	 * @param randomTime
	 *            随机播放时长 (单位：秒)
	 * @return
	 */
	public static String playbackRandomBroadcastMsg(final Long invco, final long randomTime) {
		String msg = "PLAY RTSP/1.0\r\n" + "CSeq: " + invco + "\r\n" + "Range: npt=" + randomTime + "-";
		return msg;
	}

	/**
	 * 开始录像报文
	 * 
	 * @param invco
	 *            cseq编号
	 * @param deviceID
	 *            设备id
	 * @return
	 */
	public static String startVideotapeMsg(final Long invco, final String deviceID) {
		String contents = "<?xml version=\"1.0\"?>\n" + "<Control>\r\n" + "<CmdType>DeviceControl</CmdType>\r\n"
				+ "<SN>" + invco + "</SN>\r\n" + "<DeviceID>" + deviceID + "</DeviceID>\r\n"
				+ "<RecordCmd>Record</RecordCmd>\r\n" + "</Control>";
		return contents;
	}

	/**
	 * 停止录像报文
	 * 
	 * @param invco
	 *            cseq编号
	 * @param deviceID
	 *            设备id
	 * @return
	 */
	public static String stopVideotapeMsg(final Long invco, final String deviceID) {
		String contents = "<?xml version=\"1.0\"?>\n" + "<Control>\r\n" + "<CmdType>DeviceControl</CmdType>\r\n"
				+ "<SN>" + invco + "</SN>\r\n" + "<DeviceID>" + deviceID + "</DeviceID>\r\n"
				+ "<RecordCmd>StopRecord </RecordCmd>\r\n" + "</Control>";

		return contents;
	}

	/**
	 * 云台控制报文
	 * 
	 * @param invco
	 *            cseq编号
	 * @param ptzCmd
	 *            PTZ指令举例
	 * @param deviceID
	 *            设备id
	 * @return
	 */
	public static String cloudControlMsg(final Long invco, final String ptzCmd, final String deviceID) {
		String contents = "<?xml version=\"1.0\"?>\n" + "<Control>\r\n" + "<CmdType>DeviceControl</CmdType>\r\n"
				+ "<SN>" + invco + "</SN>\r\n" + "<DeviceID>" + deviceID + "</DeviceID>\r\n" + "<PTZCmd>" + ptzCmd
				+ "</PTZCmd>\r\n" + "<Info>\r\n" + "<ControlPriority>5</ControlPriority>\r\n" + "</Info>\r\n"
				+ "</Control>\r\n";

		return contents;
	}
	
	/**
	 * 获取xml参数
	 * @param invco
	 *            cseq编号
	 * @param deviceID
	 *            设备id
	 * @return
	 */
	public static String searchPresetMsg(final Long invco,final String deviceID) {
		String contents = "<?xml version=\"1.0\"?>\r\n"+ 
				"<Notify>\r\n" + 
				"<CmdType>PresetQuery</CmdType>\r\n" + 
				"<SN>"+invco+"</SN>\r\n" + 
				"<DeviceID>"+deviceID+"</DeviceID>\r\n" + 
				"</Notify>";
		return contents;
	}
	/**
	 * 生成PTZCmd值
	 * 
	 * @param em
	 * @param speed
	 *            步长
	 * @return
	 */
	public static String genZoomPTZCmd(ZoomEnum em, int speed) {
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append(BIT_3);
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
	 * @param em
	 * @param speed
	 *            步长
	 * @return
	 */
	public static String genIrisPTZCmd(IrisEnum em, int speed) {
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append(BIT_3);
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
	 * @param em
	 * @param speed
	 *            步长
	 * @return
	 */
	public static String genFocusPTZCmd(FocusEnum em, int speed) {
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append(BIT_3);
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
	 * @param em
	 * @param speed
	 *            步长
	 * @return
	 */
	public static String genDirectionPTZCmd(DirectionEnum em, int speed) {
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append(BIT_3);
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
	 * 预置位生成PTZCmd值
	 * 
	 * @param em
	 * @param speed
	 *            presetNum 预置位编号 0-255
	 * @return
	 */
	public static String genPresetPTZCmd(PresetTypeEnum em, int presetNum) {
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append(BIT_3);
		String bit4 = em.getValue(), bit5 = "00", bit6 = "00", bit7 = "00";
		
		bit6 = HexData.numToHex8(presetNum);
		ptzCmd.append(bit4).append(bit5).append(bit6).append(bit7);
		ptzCmd.append(genPtzCmdBit8(bit4, bit5, bit6, bit7));
		return ptzCmd.toString();
	}
	/**
	 * 巡航指令生成PTZCmd值
	 * 
	 * @param em
	 * @param groupNum 巡航组号
	 * @param presetNum 预置位编号 0-255
	 * @return
	 */
	public static String genCruisePTZCmd(CruiseTypeEnum em, Integer groupNum,Integer presetNum,Long stayTime,Integer speed) throws SipSystemException{
		StringBuffer ptzCmd = new StringBuffer();
		ptzCmd.append(BIT_1).append(BIT_2).append(BIT_3);
		String bit4 = em.getValue(),
				bit5 = HexData.numToHex8(groupNum), bit6 = "00", bit7 = "00";
		//字节5表示巡航组号，字节6表示预置位号
		if(CruiseTypeEnum.ADD.equals(em) || CruiseTypeEnum.DEL.equals(em)) {
			bit6 = HexData.numToHex8(presetNum);
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
		}else {
			bit5 = "00";
			bit6 = "00";
		}
		
		bit4 = em.getValue();
		bit6 = HexData.numToHex8(presetNum);
		ptzCmd.append(genPtzCmdBit8(bit4, bit5, bit6, bit7));
		
		return ptzCmd.toString();
	}
	
	public static void main(String[] args) {
		String ptzCmd = MediaMsgUtil.genDirectionPTZCmd(DirectionEnum.UPPER_LEFT, 3);
		System.out.println(ptzCmd);

		System.out.println(HexData.numToHex8(29));
	}
}
