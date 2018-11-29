package com.hdvon.sip.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.sip.DialogTerminatedEvent;
import javax.sip.IOExceptionEvent;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.SipFactory;
import javax.sip.SipListener;
import javax.sip.SipProvider;
import javax.sip.SipStack;
import javax.sip.TimeoutEvent;
import javax.sip.TransactionTerminatedEvent;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;

import com.hdvon.sip.config.SipConfig;
import com.hdvon.sip.exception.SipSystemException;
import com.hdvon.sip.utils.SipConstants;

public class SipAdapter implements SipListener {
	
	
	public SipFactory sipFactory;
	
	public SipStack sipStack;
	
	public SipProvider sipProvider;

	public AddressFactory addressFactory;

	public MessageFactory messageFactory;

	public HeaderFactory headerFactory;
	
	public int logLevel = 32;
	
	public AtomicLong cSeqCounter = new AtomicLong(1);
	
	SipConfig sipConfig;
	
	
	AtomicInteger diagTerminatedCounter = new AtomicInteger(0);
	AtomicInteger IOExceptionCounter = new AtomicInteger(0);
	AtomicInteger requestCounter = new AtomicInteger(0);
	List<ResponseEvent> responses = Collections.synchronizedList(new ArrayList<ResponseEvent>());
	AtomicInteger timeoutcounter = new AtomicInteger(0);
	AtomicInteger txTerminatedCounter = new AtomicInteger(0);

	
	public void init() {

		try {
			String ipAddress = sipConfig.getClientIp();
			
			Properties properties = new Properties();
			properties.setProperty("javax.sip.IP_ADDRESS", ipAddress);
			properties.setProperty("javax.sip.STACK_NAME", SipConstants.STACK_NAME);
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
	
	public void resetCounters() {
		diagTerminatedCounter.set(0);
		IOExceptionCounter.set(0);
		requestCounter.set(0);
		responses = Collections.synchronizedList(new ArrayList<ResponseEvent>());
		timeoutcounter.set(0);
		txTerminatedCounter.set(0);
	}

	public void processDialogTerminated(DialogTerminatedEvent arg0) {
		diagTerminatedCounter.incrementAndGet();
	}

	public void processIOException(IOExceptionEvent arg0) {
		IOExceptionCounter.incrementAndGet();
	}

	public void processRequest(RequestEvent arg0) {
		requestCounter.incrementAndGet();
	}

	public void processResponse(ResponseEvent arg0) {
		responses.add(arg0);
	}

	public void processTimeout(TimeoutEvent arg0) {
		timeoutcounter.incrementAndGet();
	}

	public void processTransactionTerminated(TransactionTerminatedEvent arg0) {
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		txTerminatedCounter.incrementAndGet();
	}
}
