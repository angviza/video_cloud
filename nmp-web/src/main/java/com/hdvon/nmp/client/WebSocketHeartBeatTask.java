package com.hdvon.nmp.client;

import com.hdvon.nmp.common.NotifyBean;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.enums.NotifyTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * websocket定时发送心跳
 * @author zhuxiaojin
 * @Date 2018-10-15
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
@Slf4j
@Component
@Configurable
@EnableScheduling
public class WebSocketHeartBeatTask {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Scheduled(cron = "0/10 * * * * ? ")
    public void cleanAlarm() {
        //log.debug("heartbeat");
        NotifyBean notifyBean = NotifyBean.builder()
                .type(NotifyTypeEnum.HEARTBEAT.getType())
                .message("heartbeat")
                .build();

        notifyBean.setData(System.currentTimeMillis());
        simpMessagingTemplate.convertAndSend(WebConstant.WEB_SC_TOPIC_NOTIFY, notifyBean);

    }
}
