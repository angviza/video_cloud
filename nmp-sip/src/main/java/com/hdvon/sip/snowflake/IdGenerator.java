package com.hdvon.sip.snowflake;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * snowflake-id 生成器
 */
@Component
public class IdGenerator {

    private static int snowflakeWorkId;

    @Value("${snowflakeWorkId:1}")//机器id，默认值为1
    public void setSnowflakeWorkId(int snowflakeWorkId){//应用启动读取配置
        IdGenerator.snowflakeWorkId = snowflakeWorkId;
    }

    private static IdWorker idWorker = new IdWorker(snowflakeWorkId);

    public static String nextId() {
        if (idWorker == null) {
            idWorker = new IdWorker(snowflakeWorkId);
        }
        return idWorker.nextId()+"";
    }

    public static void main(String[] args) {
        System.out.println(IdGenerator.nextId());
    }
}