package com.hdvon.nmp.client;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

import lombok.Data;

public class ScreenStatusTimerTask extends TimerTask{
	
	private String matrixIp;

	private int matrixPort;
	
	private List<String> channelIdList;

	public ScreenStatusTimerTask(String matrixIp, int matrixPort, List<String> channelIdList) {
		this.matrixIp = matrixIp;
		this.matrixPort = matrixPort;
		this.channelIdList = channelIdList;
	}
	
	@Override
	public void run() {
		
		  WallVedioRpcClient statusWvc = new WallVedioRpcClient(matrixIp, matrixPort);
	      try {
			statusWvc.getScreenStatus("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getMatrixIp() {
		return matrixIp;
	}

	public void setMatrixIp(String matrixIp) {
		this.matrixIp = matrixIp;
	}

	public int getMatrixPort() {
		return matrixPort;
	}

	public void setMatrixPort(int matrixPort) {
		this.matrixPort = matrixPort;
	}

	public List<String> getChannelIdList() {
		return channelIdList;
	}

	public void setChannelIdList(List<String> channelIdList) {
		this.channelIdList = channelIdList;
	}
}
