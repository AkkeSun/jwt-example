package com.sweettracker.authserver.global.util;

public interface JsonUtil {

    <T> T parseJson(String json, Class<T> valueType);

    String toJsonString(Object obj);
}