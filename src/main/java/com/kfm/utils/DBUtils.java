package com.kfm.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.util.Properties;

public class DBUtils {
    public static Connection getConnection() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileReader(new File("F:\\麻小神\\Desktop\\auto-transmit-project\\src\\main\\resources\\druid.properties")));
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        return dataSource.getConnection();
    }
}
