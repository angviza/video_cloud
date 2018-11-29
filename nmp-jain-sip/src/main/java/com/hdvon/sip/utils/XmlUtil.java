package com.hdvon.sip.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.hdvon.sip.entity.DeviceStatusBean;
import com.hdvon.sip.entity.MediaStatusBean;
import com.hdvon.sip.entity.NotifyBean;
import com.hdvon.sip.entity.PresetQueryBean;
import com.hdvon.sip.entity.StatusBean;
import com.hdvon.sip.enums.MessageTypeEnum;
import com.thoughtworks.xstream.XStream;

/**
 * xml直接转化为对象
 * @author wanshaojian
 * @since 2018-10-15
 *
 */
public class XmlUtil {
	private static final String URI_CHAR = ":";
	private static final String KEY_CHAR = "@";
	
	private static final String CMD_FIELD ="CmdType";
	public static Logger logger = LoggerFactory.getLogger(XmlUtil.class);
	
	public static <T> T toBean(Class<T> clazz, String xml) {
        try {
            XStream xstream = new XStream();
            xstream.processAnnotations(clazz);
            xstream.autodetectAnnotations(true);
            xstream.setClassLoader(clazz.getClassLoader());
            return (T) xstream.fromXML(xml);
        } catch (Exception e) {
            logger.error("[XStream]XML转对象出错:{}", e.getCause());
            throw new RuntimeException("[XStream]XML转对象出错");
        }
    }
	/**
	 * 生成message会话的关联key
	 * @param deviceCode 设备编码
	 * @param sn sn编号
	 * @return
	 */
	public static String genRelationKey(String deviceCode,Long sn) {
        String reqKey = deviceCode + KEY_CHAR + sn;
        return reqKey;
	}

	/**
	 * 获取message的cmdType类型
	 * @param xml
	 * @return
	 */
	public static MessageTypeEnum getMessageType(String xml){
		
		int beginIndex = xml.indexOf("<CmdType>");
		int endIndex = xml.indexOf("</CmdType>");
		String value= xml.substring(beginIndex, endIndex).replace("<CmdType>", "");
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>type:"+value);
		return MessageTypeEnum.getObjectByKey(value);
	}
	/**
	 * 将xml转化为DeviceStatus对象
	 * @param xml
	 * @return
	 */
	public static DeviceStatusBean getDeviceStatusBean(String xml){
		DeviceStatusBean data = XmlUtil.toBean(DeviceStatusBean.class, xml);
		
		return data;
	}
	
	/**
	 * 将xml转化为DeviceStatus对象
	 * @param xml
	 * @return
	 */
	public static StatusBean getStatusBean(String xml){
		StatusBean data = new StatusBean();
		int index = xml.indexOf("<Notify>");
		if(index != -1) {
			NotifyBean mediaData = XmlUtil.toBean(NotifyBean.class, xml);
			BeanUtils.copyProperties(mediaData, data);
		}else {
			MediaStatusBean mediaData = XmlUtil.toBean(MediaStatusBean.class, xml);
			BeanUtils.copyProperties(mediaData, data);
		}
		return data;
	}
	/**
	 * 将xml转化为PresentQuery对象
	 * @param xml
	 * @return
	 */
	public static PresetQueryBean getPresetQueryBean(String xml){
		PresetQueryBean data = XmlUtil.toBean(PresetQueryBean.class, xml);
		
		return data;
	}

	public static void main(String[] args) {
		String xml="<?xml version=\"1.0\" encoding=\"gb2312\"?><Response>\r\n" + 
				"<CmdType>RecordInfo</CmdType>\r\n" + 
				"<SN>59063</SN>\r\n" + 
				"<DeviceID>38020000001320000010</DeviceID>\r\n" + 
				"<Name>IPdome</Name>\r\n" + 
				"<SumNum>2</SumNum>\r\n" + 
				"<RecordList Num=\"2\">\r\n" + 
				"<Item>\r\n" + 
				"<DeviceID>38020000001320000010</DeviceID>\r\n" + 
				"<Name>IPdome</Name>\r\n" + 
				"<FilePath>1535348158_1535349571</FilePath>\r\n" + 
				"<Address>Address 1</Address>\r\n" + 
				"<StartTime>2018-08-27T13:35:58</StartTime>\r\n" + 
				"<EndTime>2018-08-27T13:59:31</EndTime>\r\n" + 
				"<Secrecy>0</Secrecy>\r\n" + 
				"<Type>time</Type>\r\n" + 
				"</Item>\r\n" + 
				"<Item>\r\n" + 
				"<DeviceID>38020000001320000010</DeviceID>\r\n" + 
				"<Name>IPdome</Name>\r\n" + 
				"<FilePath>1535349571_1535350982</FilePath>\r\n" + 
				"<Address>Address 1</Address>\r\n" + 
				"<StartTime>2018-08-27T13:59:31</StartTime>\r\n" + 
				"<EndTime>2018-08-27T14:23:02</EndTime>\r\n" + 
				"<Secrecy>0</Secrecy>\r\n" + 
				"<Type>time</Type>\r\n" + 
				"</Item>\r\n" + 
				"</RecordList>\r\n" + 
				"</Response>";
		XmlUtil.getMessageType(xml);
		
//		RecordBean record = XmlUtil.toBean(RecordBean.class,xml);
//		System.out.println(JSON.toJSONString(record));// 调用getXmlString（）方法把xml格式重新转换为对象
	}
}
