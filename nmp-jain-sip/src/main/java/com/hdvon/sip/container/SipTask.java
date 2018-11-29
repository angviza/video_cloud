package com.hdvon.sip.container;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hdvon.sip.service.SipService;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能：sip定时任务
 * 作者：wanshaojian
 * 日期：2018/10/23
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2017
 */
@Slf4j
@Component
public class SipTask {

	@Autowired
	SipService sipService;

    /**
     * sip 用户每间隔2小时注册一次
     */
    @Scheduled(cron = "0 0 0/2 * * ? ")
    public void register() {
        log.debug("sip user register");
        sipService.register();
    }

//    /**
//     * sip 注册用户发送心跳
//     */
//    @Scheduled(cron = "0 0/2 * * * ? ")
//    public void heartBeat() {
//    	log.debug("sip registered user send heratbeat");
//    	sipService.sendHeartbeat();
//    }

}
