package com.kfm.mapper;

import com.kfm.entity.Server;
import com.kfm.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServerMapper {
    public List<Server> selectAllServers() throws Exception {
        Connection conn = DBUtils.getConnection();
        PreparedStatement ps = conn.prepareStatement("select ipAddress,username,password,email,directoryPath from server");
        ResultSet rs = ps.executeQuery();
        List<Server> ipAddressList = new ArrayList<>();
        while (rs.next()) {
            String ipAddress = rs.getString("ipAddress");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String email = rs.getString("email");
            String directory = rs.getString("directoryPath");
            Server server = new Server();
            server.setIp(ipAddress);
            server.setUsername(username);
            server.setPassword(password);
            server.setEmailAddress(email);
            server.setFileListPath(directory);
            ipAddressList.add(server);
        }
        return ipAddressList;
    }
}
