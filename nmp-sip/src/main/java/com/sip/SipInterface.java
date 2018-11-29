package com.sip;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SipInterface {
	
	//启动
    public static native boolean startUp(String ip, int port);
    
	//注册相关
	public static native String callRegister(RegisterOption opt, RegisterCred cred);
	public static native void callUnRegister(String callID);
	
	//invite相关
	public static native String callInvite(InviteOption option); 
	public static native void callTerminate(String callID); 
	public static native void callACK(String inviteId); 
	
	//回放控制
	public static native String playBackCtrl(String callID, RecordCtrlOption option);
	
	//云台控制
	public static native String callCloudCmd(ControlOption ctrOption);
	
	//预置位控制
	public static native String callPresetCmd(PresetOption presetOption);
	
	//预置位查询
	public static native String callQueryPresetCmd(QueryPreOption queryOption);
	
	// 雨刷控制
	public static native String callWiperCmd(WiperOption wiperOption);
		
	//public static native void callCloudReset(ControlOption ctrOption);
	//public static native String callCloudZoom(ControlOption ctrOption);
    //public static native String callCloudIris(ControlOption ctrOption);
    //public static native String callCloudFocus(ControlOption ctrOption);
	//录像控制
	public static native String callRecordCmd(RecordOption recordOption);
	
	//巡航预案控制
	public static native String callCruiseCmd(CruiseOption cruiseOption);
	
	// 录像文件查询
   public static native String callQueryRecordCmd(QueryRecOption queryRecOption);
	
	public static Map<String, Object> map = new HashMap<>();
	
//	public static void triggerAsncCallback() {
//		//Callback callback = Callback.getInstance();
//		
//		try {
//			startUp("192.168.2.101", 5060);
//			//startUp("172.31.108.128", 5060);
//			
//			int k=1;
//			while(k>0){
//				System.out.println("please input a char\r\n");
//				char i = (char)System.in.read();
//				switch(i){
//				  case 'r':
//					try {
//						RegisterOption opt = new RegisterOption();
//						RegisterCred cred = new RegisterCred();
//						
//						String strRegisterId = callRegister(opt, cred);
//						map.put(strRegisterId, strRegisterId);
//						//RedisTemplateUtils.redisTemplate.opsForValue().set(strRegisterId, strRegisterId);
//						
//						System.out.println("register id "+strRegisterId);
//						
//						ExecutorService executor = Executors.newCachedThreadPool();
//					    final CountDownLatch latch = new CountDownLatch(1);
//					    executor.execute(new Runnable() {
//							public void run() {
//								latch.countDown();
//							}
//					    });
//						    
//					    executor.execute(() -> {
//					        try {
//					          latch.await();
//					        } catch (InterruptedException e) {
//					          e.printStackTrace();
//					        }
//					    });
//					    
//					    executor.shutdown();
//						
//					    //RedisTemplateUtils.redisTemplate.opsForValue().get(strRegisterId);
//					    System.out.println("callback map "+map);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					    
//					break;
//				  case 'i':
//						InviteOption invOption = new InviteOption();
//						String inviteId = callInvite(invOption);
//						System.out.println(inviteId);
//					break;
//				  case 'm':
//					   ControlOption ctrOption = new ControlOption();
//					   String ctrId = callCloudCmd(ctrOption);
//					break;
//				  case 'e':
//					k=0;
//				  default:
//					break;
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args) throws IOException {
		startUp("192.168.2.57",5060);
		int k=1;
		String inviteId = "";
		while(k>0){
			System.out.println("please input a char\r\n");
			char i = (char)System.in.read();
			switch(i){
			  case 'r':
					RegisterOption opt =new RegisterOption();
					RegisterCred cred = new RegisterCred();
					String strRegisterId=callRegister(opt,cred);
				break;
			  case 'i':
					InviteOption invOption=new InviteOption();
					inviteId=callInvite(invOption);
					
				break;
			  case 'b':
					System.out.println(inviteId);
					callTerminate(inviteId);
					
				break;
			  case 'm':
				   ControlOption ctrOption=new ControlOption();
				   String ctrId=callCloudCmd(ctrOption);
				   System.out.println(ctrOption);
				break;
			  case 's':
				   ControlOption ctrstopOption=new ControlOption();
				   //ctrstopOption.iStop = 0;
				   
				   String ctrstopId=callCloudCmd(ctrstopOption);
				   System.out.println(ctrstopOption);
				break;
			  case '1':
					InviteOption invRecordOption=new InviteOption();
					/**
					invRecordOption.body = "v=0\r\n"+
					   "o=38020000003000000020 0 0 IN IP4 192.168.2.184\r\n"+
					   "u=38020000001320000010:255\r\n"+
					   "s=PlayBack\r\n"+
					   "c=IN IP4 192.168.2.184\r\n"+
					   "t=1530875739 1530875749\r\n"+
					   "m=video 6000 RTP/AVP 96 97 98 99\r\n"+
					   "a=recvonly\r\n"+
					   "a=rtpmap:96 PS/90000\r\n"+
					   "a=rtpmap:97 MPEG4/90000\r\n"+
					   "a=rtpmap:98 H264/90000\r\n"+
					   "a=rtpmap:99 SVAC/90000\r\n"+
					   "";
					   **/
					inviteId=callInvite(invRecordOption);	
				break;
			  case '2':
				   RecordCtrlOption recordctrOption=new RecordCtrlOption();
				   String opId=playBackCtrl(inviteId,recordctrOption);
				   System.out.println(opId);
				break;
			  case '3':
				   RecordCtrlOption recordctrOption1=new RecordCtrlOption();
				   //recordctrOption1.cmdType = 1;
				   String opId1=playBackCtrl(inviteId,recordctrOption1);
				   System.out.println(opId1);
				break;
			  case '4':
				   RecordCtrlOption recordctrOption2=new RecordCtrlOption();
				   //recordctrOption2.cmdType = 2;
				   String opId2=playBackCtrl(inviteId,recordctrOption2);
				   System.out.println(opId2);
				break;
			  case '5':
				   RecordCtrlOption recordctrOption3=new RecordCtrlOption();
				   //recordctrOption3.cmdType = 4;
				   String opId3=playBackCtrl(inviteId,recordctrOption3);
				   System.out.println(opId3);
				break;
			  case '6':
					InviteOption invDownloadOption=new InviteOption();
					/**
					invDownloadOption.body = "v=0\r\n"+
					   "o=38020000003000000020 0 0 IN IP4 192.168.2.184\r\n"+
					   "u=38020000001320000010:255\r\n"+
					   "s=Download\r\n"+
					   "c=IN IP4 192.168.2.184\r\n"+
					   "t=1530254172 1530254182\r\n"+
					   "m=video 6000 RTP/AVP 96 97 98 99\r\n"+
					   "a=recvonly\r\n"+
					   "a=rtpmap:96 PS/90000\r\n"+
					   "a=rtpmap:97 MPEG4/90000\r\n"+
					   "a=rtpmap:98 H264/90000\r\n"+
					   "a=rtpmap:99 SVAC/90000\r\n"+
					   "";**/
					inviteId=callInvite(invDownloadOption);	
				break;
			  case '7':
				   RecordOption recordOption=new RecordOption();
				   String ctrId1=callRecordCmd(recordOption);
				   System.out.println(recordOption);
				break;
			  case '8':
				   RecordOption recordOption1=new RecordOption();
				   recordOption1.cmdType = 2;
				   String ctrId2=callRecordCmd(recordOption1);
				   System.out.println(recordOption1);
				break;
			  case 'x':
				   PresetOption presetOption = new PresetOption();
				   String presetId=callPresetCmd(presetOption);
				   System.out.println(presetId);
				break;
			  case 'y':
				   PresetOption presetOption1 = new PresetOption();
				   //presetOption1.presetType = 2;
				   String presetId1 = callPresetCmd(presetOption1);
		
				   System.out.println(presetId1);
				break;
			  case 'z':
				   PresetOption presetOption2 = new PresetOption();
				   //presetOption2.presetType = 3;
				   String presetId2 = callPresetCmd(presetOption2);
		
				   System.out.println(presetId2);
				break;
			  case 'o':
				   WiperOption wiperOption = new WiperOption();
				   String wiperId = callWiperCmd(wiperOption);		
				   System.out.println(wiperId);
				break;
			  case 'p':
				   WiperOption wiperOption1 = new WiperOption();
				   wiperOption1.cmdType = 2;
				   String wiperId1 = callWiperCmd(wiperOption1);
				   System.out.println(wiperId1);
				break;
			  case 'f':
				   CruiseOption cruiseOption = new CruiseOption();
				   String cruiseId = callCruiseCmd(cruiseOption);		
				   System.out.println(cruiseId);
				break;
			  case 'g':
				   CruiseOption cruiseOption1 = new CruiseOption();
				   cruiseOption1.setCmdType(2);
				   String cruiseId1 = callCruiseCmd(cruiseOption1);		
				   System.out.println(cruiseId1);
				break;
			  case 'h':
				   CruiseOption cruiseOption2 = new CruiseOption();
				   cruiseOption2.setCmdType(3);
				   String cruiseId2 = callCruiseCmd(cruiseOption2);		
				   System.out.println(cruiseId2);
				break;
			  case 'j':
				   CruiseOption cruiseOption3 = new CruiseOption();
				   cruiseOption3.setCmdType(4);
				   String cruiseId3 = callCruiseCmd(cruiseOption3);		
				   System.out.println(cruiseId3);
				break;
			  case 'k':
				   CruiseOption cruiseOption4 = new CruiseOption();
				   cruiseOption4.setCmdType(5);
				   String cruiseId4 = callCruiseCmd(cruiseOption4);		
				   System.out.println(cruiseId4);
				break;
			  case 'l':
				   QueryPreOption queryOption = new QueryPreOption();
				   String querypreId = callQueryPresetCmd(queryOption);		
				   System.out.println(querypreId);
				break;
			  case 'e':
				k=0;
			  default:
				break;
			}
		}
	}

	/**
	static {
		//System.loadLibrary("JavaSip");
		//LibLoader.loadLib(libName);
	}
	**/
}


