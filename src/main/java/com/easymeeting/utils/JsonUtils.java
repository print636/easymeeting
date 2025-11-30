package com.easymeeting.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.easymeeting.entity.enums.ResponseCodeEnum;
import com.easymeeting.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public static SerializerFeature[] features = new SerializerFeature[]{SerializerFeature.WriteMapNullValue};

    public static String convertObj2Json(Object obj) { return JSON.toJSONString(obj, features);}

    public static<T> T convertJson2Obj(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            logger.error("convertJson2Obj error: {}", json);
            throw new BusinessException(ResponseCodeEnum.CODE_603);
        }
    }

    public static<T> List<T> convertJsonArray2List(String json, Class<T> classz) {
        try {
            return JSONArray.parseArray(json, classz);
        } catch ( Exception e ){
            logger.error("convertJsonArray2List error: {}", json, e);
            throw new BusinessException(ResponseCodeEnum.CODE_603);
        }
    }

}
