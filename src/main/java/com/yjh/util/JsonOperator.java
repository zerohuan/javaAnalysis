package com.yjh.util;

import org.codehaus.jackson.map.ObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JsonOperator {
	private ObjectMapper objectMapper = null;
    
    @SuppressWarnings("deprecation")
	public JsonOperator() {
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	objectMapper = new ObjectMapper();
    	//设置date转换是的输出格式
    	objectMapper.getSerializationConfig().setDateFormat(format);
    }
    public ObjectMapper getJsonMapper() {
    	return this.objectMapper;
    }
}