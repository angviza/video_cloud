package com.hdvon.sip.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hdvon.sip.entity.VideoRecord;
import com.thoughtworks.xstream.XStream;


public class XmlUtil {
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
//            throw new RuntimeException("[XStream]XML转对象出错");
            e.printStackTrace();
        }
		return null;
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
		
		
		VideoRecord record = XmlUtil.toBean(VideoRecord.class,xml);
		System.out.println(JSON.toJSONString(record));// 调用getXmlString（）方法把xml格式重新转换为对象
	}
}
