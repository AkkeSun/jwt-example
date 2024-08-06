package com.sweettracker.resourceserver.global.util;

public interface JsonUtil {

    <T> T parseJson(String json, Class<T> valueType);

    String toJsonString(Object obj);
}
