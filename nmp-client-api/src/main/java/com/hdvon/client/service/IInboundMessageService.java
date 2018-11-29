package com.hdvon.client.service;

import com.hdvon.client.vo.InboundMessageVo;

/**
 * <br>
 * <b>功能：</b> 服务类<br>
 * <b>作者：</b>huanhongliang<br>
 * <b>日期：</b>2018<br>
 * <b>版权所有：<b>广州弘度信息科技有限公司 版权所有(C) 2018<br>
 */
public interface IInboundMessageService {
	
	public void saveMessageLog(InboundMessageVo vo, String msgId);
	
	public InboundMessageVo getMessage(String id);
	
}
