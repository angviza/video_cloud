package com.hdvon.nmp.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.hdvon.nmp.exception.ServiceException;

public class ClientUtil {
	/**
	 * 获取客户端真实ip
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if (ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		
		if("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
			//根据网卡获取本机配置的IP地址
			InetAddress inetAddress = null;
			try {
				inetAddress = InetAddress.getLocalHost();
				//inetAddress = getLocalHostLANAddress();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ip = inetAddress.getHostAddress();
		}
		
		return ip;
	}
	
	
	public static String getLocalClientIp(){
		String ip = null;
		try {
			//InetAddress inetAddress = getLocalHostLANAddress();
			InetAddress inetAddress = InetAddress.getLocalHost();
			//获取当前主机的IP地址
			ip = inetAddress.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ip;
	}
	
	
	public static InetAddress getLocalHostLANAddress() throws Exception {
	    try {
	        InetAddress candidateAddress = null;
	        // 遍历所有的网络接口
	        for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
	            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
	            // 在所有的接口下再遍历IP
	            for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
	                if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
	                    if (inetAddr.isSiteLocalAddress()) {
	                        // 如果是site-local地址，就是它了
	                        return inetAddr;
	                    } else if (candidateAddress == null) {
	                        // site-local类型的地址未被发现，先记录候选地址
	                        candidateAddress = inetAddr;
	                    }
	                }
	            }
	        }
	        if (candidateAddress != null) {
	            return candidateAddress;
	        }
	        // 如果没有发现 non-loopback地址.只能用最次选的方案
	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
	        return jdkSuppliedAddress;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	//获取本机的空闲端口，默认使用5060
	public static int getClientProt(String clientIp) {
		int port=0; 
		Socket client = null;
		try{
		  client = new Socket(clientIp, 5060);
		  client.close();
		  ServerSocket serverSocket =  new ServerSocket(0); //读取空闲的可用端口
		  port = serverSocket.getLocalPort();
		  serverSocket.close();
		}catch(Exception e){
			port=5060;
		}
		return port;
	}
	
	
	/**
	 * 获取本机系统的ip和空闲端口
	 * @param clientIp
	 * @param number
	 * @return
	 */
	public static List<String> getClientIpAndProt(String clientIp,int number) {
		List<String> list=new ArrayList<String>();
		try{
			for(int i =0;i<number;i++) {
				 ServerSocket serverSocket = new ServerSocket(0); //读取空闲的可用端口,并且是偶数端口
				 int port = serverSocket.getLocalPort();
				 if(port%2 == 0 && port > 10000) {
					 list.add(Integer.toString(port));
				 } else {
					 i--;
				 }
				 serverSocket.close();//释放系统占用端口
			}
		}catch(Exception e){
			throw new ServiceException("生成本地端口错误！");
		}
		return list;
	}
	
	/**
	 * 随机分配空闲端口
	 * @param clientIp  本地ip地址
	 * @param number 需要生成端口数量
	 * @return
	 */
	public static List<Integer> creatProts(String clientIp, int number) {
		List<Integer> list=new ArrayList<Integer>();
		try{
			for(int i =0;i<number;i++) {
				 ServerSocket serverSocket = new ServerSocket(0); //读取空闲的可用端口,并且是偶数端口
				 int port = serverSocket.getLocalPort();
				 if(port%2 == 0 && port > 10000) {
					 list.add(port);
				 } else {
					 i--;
				 }
				 serverSocket.close();//释放系统占用端口
			}
		}catch(Exception e){
			throw new ServiceException("生成本地端口错误！");
		}
		return list;
	}
	
	/**
	 * 获取本机的mac地址
	 * @return
	 */
	public static String getClientMac() {
		String macId=null;
		try {
			//获取本地IP对象
			InetAddress address = InetAddress.getLocalHost(); 
			 //获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。    
	        byte[] mac = NetworkInterface.getByInetAddress(address).getHardwareAddress();    
	        //下面代码是把mac地址拼装成String    
	        StringBuffer sb = new StringBuffer();    
	        for(int i=0;i<mac.length;i++){    
	            if(i!=0){    
	                sb.append("-");    
	            }    
	            //mac[i] & 0xFF 是为了把byte转化为正整数    
	            String s = Integer.toHexString(mac[i] & 0xFF);    
	            sb.append(s.length()==1?0+s:s);    
	        }    
	        //把字符串所有小写字母改为大写成为正规的mac地址并返回    
	        macId= sb.toString().toUpperCase();  
		}	
		catch (Exception e) {
			throw new ServiceException("获取本地mac地址出错！");
		}
		return macId;
	}
	
	
	public static void main(String[] args) { 
		/*String ip="127.0.0.1";
		getClientIpAndProt(ip,9);*/
		
		getClientMac();
	}

	
	
}
