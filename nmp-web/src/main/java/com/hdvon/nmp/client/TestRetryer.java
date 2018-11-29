package com.hdvon.nmp.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.hdvon.nmp.vo.sip.InviteOptionVo;

public class TestRetryer {
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS");
	private static String matrixIp = "192.168.2.65";
	private static Integer matrixPort = 8888;
	public static void main(String[] args) {
		Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
				.retryIfExceptionOfType(IOException.class)
				.withWaitStrategy(WaitStrategies.fixedWait(10,TimeUnit.SECONDS))
				.withStopStrategy(StopStrategies.stopAfterAttempt(5))
				.build();
		System.out.println("begin..." + df.format(new Date()));
		 
		try {
			retryer.call(buildTask());
		} catch (Exception e) {
			e.printStackTrace();
		}
	 
		System.out.println("end..." + df.format(new Date()));
	}
	
	private static Callable<Boolean> buildTask() {
		return new Callable<Boolean>() {
	 
			@Override
			public Boolean call() throws Exception {
				InviteOptionVo vo = new InviteOptionVo();
				vo.setChannelId("34020000001310000001");
				vo.setDeviceCode("340200000013300000001");
				boolean b = new WallVedioRpcClient(matrixIp, matrixPort).upOrPollingWallVedio(vo);
				if(!b) {
					System.out.println("IO异常........");
					throw new IOException();
				}else {
					throw new NullPointerException();
				}
			}
		};
	}
}
