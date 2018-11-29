package com.hdvon.quartz.util;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

/**
 * 自定义mybatis扩展类
  * @param <T>
 */
@RegisterMapper
public interface MySqlMapper<T> {

    @InsertProvider(
            type = SpecialProvider.class,
            method = "dynamicSQL"
    )
    int insertList(List<T> var1);
}
