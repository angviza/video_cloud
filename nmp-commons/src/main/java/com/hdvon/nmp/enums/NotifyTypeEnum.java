package com.hdvon.nmp.enums;

/**
 * websocket通知类型
 * @author zhuxiaojin
 * @Date 2018-10-15
 * 版权所有：广州弘度信息科技有限公司 版权所有(C) 2018
 */
@SuppressWarnings("all")
public enum NotifyTypeEnum {


    //通知类型：0:心跳
    HEARTBEAT(0);

    private int type;

    NotifyTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

}
