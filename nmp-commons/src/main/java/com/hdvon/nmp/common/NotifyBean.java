package com.hdvon.nmp.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * websocket通知前端页面的消息类
 * @author zhuxiaojin
 * @Date 2018-10-15
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int type;
    private String message;
    private T data;//也可以是Map

}
