package com.hdvon.sip.app;

import java.util.Properties;

import javax.sip.SipFactory;
import javax.sip.SipStack;

import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.SipConstants;

public class SipStackManage{
	private static final int logLevel = 32;
	private static SipStack sipStack;
	private static SipFactory sipFactory;
	
	private volatile static SipStackManage instance;
	
	public SipStackManage(SipConfig sipConfig) {

		try {
			String ipAddress = sipConfig.getClientIp();
			
			Properties properties = new Properties();
			properties.setProperty("javax.sip.IP_ADDRESS", ipAddress);
			properties.setProperty("javax.sip.STACK_NAME", "hdvon-register-stack");
			properties.setProperty("gov.nist.javax.sip.MAX_MESSAGE_SIZE", "1048576");
			properties.setProperty("gov.nist.javax.sip.DEBUG_LOG", "sipAuthdebug.txt");
			properties.setProperty("gov.nist.javax.sip.SERVER_LOG", "sipAuthlog.txt");
			properties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", new Integer(logLevel).toString());
			// Drop the client connection after we are done with the transaction.
			properties.setProperty("gov.nist.javax.sip.CACHE_CLIENT_CONNECTIONS", "false");
			
//			properties.setProperty("gov.nist.javax.sip.READ_TIMEOUT", "1000");
			//连接超时时间
			properties.setProperty("gov.nist.javax.sip.CONNECTION_TIMEOUT", "3000");
			properties.setProperty("gov.nist.javax.sip.CONGESTION_CONTROL_TIMEOUT", "3000");
			
	        properties.setProperty("gov.nist.javax.sip.NIO_BLOCKING_MODE", "NONBLOCKING");
	        
			// Create SipStack object
	        this.sipFactory = SipFactory.getInstance();
	        this.sipFactory.setPathName(SipConstants.SIP_DOMAIN);
            this.sipStack = this.sipFactory.createSipStack(properties);
            this.sipStack.start();
            
             
		} catch (Exception e) {
			// could not find gov.nist.jain.protocol.ip.sip.SipStackImpl in the
			// classpath
			throw new SipSystemException(e);
		}
	}
	
	public static SipStackManage getInstance(SipConfig sipConfig){
        if(instance==null){
            synchronized (SipStackManage.class){
                if(instance==null){
                    instance=new SipStackManage(sipConfig);
                }
            }
        }
        return instance;
    }

	public static SipStack getSipStack() {
		return sipStack;
	}

	public static SipFactory getSipFactory() {
		return sipFactory;
	}
	
	
}
