package com.hdvon.nmp.util;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.convert.ConvertException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hdvon
 * @Description bean工具类
 * @date: 2018/05/24 17:30
 */
public class BeanHelper {

    /**
     * 拷贝List实体
     */
    public static <T> List<T> convertList(Class<T> type, List sourceList) throws ConvertException {
        List<T> newList = new ArrayList<>();
        for (Object item : sourceList) {
            T t = Convert.convert(type,item);
            newList.add(t);
        }
        return newList;
    }

}
