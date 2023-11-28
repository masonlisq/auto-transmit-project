package com.kfm.utils;

import com.google.gson.Gson;

import java.util.List;

public class JSONUtils {
    public static <E> String convertToJSON(List<E> jsonList) {
        // 使用一个JSON库（如Jackson或Gson）将Java对象转换为JSON字符串
        // 这里使用Gson作为示例
        Gson gson = new Gson();
        return gson.toJson(jsonList);
    }
}
