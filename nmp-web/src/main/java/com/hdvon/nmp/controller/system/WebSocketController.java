package com.hdvon.nmp.controller.system;

import com.hdvon.nmp.common.NotifyBean;
import com.hdvon.nmp.common.WebConstant;
import com.hdvon.nmp.controller.BaseController;
import com.hdvon.nmp.util.RedisHelper;
import com.hdvon.nmp.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @ClassName
 * @Description WebSocket控制器
 * @author zhuxiaojin
 * @Date 2018-04-12
 */
@Api(tags="WebSocket控制器",description="WebSocket控制器")
@Controller
@RequestMapping(value = "/webSocket")
public class WebSocketController extends BaseController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private RedisHelper redisHelper;


    @ApiOperation(value = "测试主动发送消息", notes = "测试主动发送消息", httpMethod = "GET")
    @RequestMapping(value = "/sendMsg")
    @ResponseBody
    public void sendMsg(){
        System.out.println("测试主动发送消息");
        NotifyBean notifyBean = NotifyBean.builder().message("服务器给你发消息啦！").build();
        simpMessagingTemplate.convertAndSend(WebConstant.WEB_SC_TOPIC_NOTIFY,notifyBean);
    }


    @MessageMapping("/test") //当浏览器向服务端发送请求时,通过@MessageMapping映射/welcome这个地址,类似于@ResponseMapping
    @SendTo(WebConstant.WEB_SC_TOPIC_NOTIFY)//当服务器有消息时,会对订阅了@SendTo中的路径的浏览器发送消息
    public NotifyBean test(UserVo userVo) {
        try {
            //睡眠1秒
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NotifyBean notifyBean = NotifyBean.builder().message("welcome！"+ userVo.getName()).build();
        return notifyBean;
    }

    /**
     * 点对点发送消息demo
     * 根据用户key发送消息
     * @param userVo
     * @return
     * @throws Exception
     */
    @MessageMapping("/test/toOne")
    public void toOne(UserVo userVo) throws Exception {
        String sessionId=(String)redisHelper.redisTemplate.opsForValue().get("websocket:"+userVo.getId());
        NotifyBean notifyBean = NotifyBean.builder().message("welcome！"+ userVo.getName()).build();
        //convertAndSendToUser该方法会在订阅路径前拼接"/user"，所以前端订阅的路径全路径是"/user/topic/notify"
        simpMessagingTemplate.convertAndSendToUser(sessionId, WebConstant.WEB_SC_TOPIC_NOTIFY,notifyBean,createHeaders(sessionId));
    }



    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }



}
