package com.kfm.service;

import com.kfm.entity.Log;
import com.kfm.mapper.LogMapper;

import java.util.List;

public class LogService {
    static LogMapper logMapper = new LogMapper();
    public List<Log> selectAllLog(){
        List<Log> logs = null;
        try {
            logs = logMapper.selectAllLogs();
        }catch (Exception e){
            e.printStackTrace();
        }
        return logs;
    }
//    public void insertALog(String type, String message){
//        try {
//            logMapper.insertALog(type,message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
