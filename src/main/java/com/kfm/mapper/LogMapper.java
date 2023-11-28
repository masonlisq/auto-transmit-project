package com.kfm.mapper;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.kfm.entity.Log;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class LogMapper {
    public List<Log> selectAllLogs()throws Exception{
        Properties properties = new Properties();
        properties.load(new FileReader(new File("F:\\麻小神\\Desktop\\auto-transmit-project\\src\\main\\resources\\druid.properties")));
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("select id ,type,message,createTime,updateTime from operation_log");
        ResultSet rs = ps.executeQuery();
        List<Log> logList = new ArrayList<>();
        while (rs.next()) {
            Long id = rs.getLong("id");
            String type = rs.getString("type");
            String message = rs.getString("message");
            String createTime = rs.getString("createTime");
            String updateTime = rs.getString("updateTime");
            Log log = new Log();
            log.setId(id);
            log.setType(type);
            log.setMessage(message);
            log.setCreateTime(createTime);
            log.setUpdateTime(updateTime);
            logList.add(log);
        }
        return logList;
    }

//    public void insertALog(String type, String message) throws Exception {
//        Connection conn = DBUtils.getConnection();
//        PreparedStatement ps = conn.prepareStatement("insert into operation_log values (null,?,?,null,null)");
//        int i = ps.executeUpdate();
//    }
}
