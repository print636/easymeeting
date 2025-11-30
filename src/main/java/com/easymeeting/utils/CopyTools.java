package com.easymeeting.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class CopyTools {
    public static <T,S> List<T> copyList(List<S> sourceList,Class<T> clazz){

        List<T> list = new ArrayList<T>();
        for(S s : sourceList){
            T t = null;
            try{
                t = clazz.newInstance();
            } catch (Exception e){}
            BeanUtils.copyProperties(s,t);
            list.add(t);
        }
        return list;
    }

    public static <T,S> T copy(S source,Class<T> clazz){

        T t = null;
        try{
            t = clazz.newInstance();
        } catch (Exception e){}
        BeanUtils.copyProperties(source,t);
        return t;
    }
}
