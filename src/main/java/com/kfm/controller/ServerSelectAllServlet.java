package com.kfm.controller;

import com.kfm.mapper.ServerMapper;
import com.kfm.service.ServerService;
import com.kfm.entity.Server;
import com.kfm.utils.JSONUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/selectAllServer")
public class ServerSelectAllServlet extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            ServerMapper mapper = new ServerMapper();
            try {
                mapper.selectAllServers();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            ServerService serverService = new ServerService();
            List<Server> serverList = serverService.selectAllServer();
            serverList.forEach(System.out::println);
            // 转换服务器数据为JSON格式
            String jsonResponse = JSONUtils.convertToJSON(serverList);

            resp.setCharacterEncoding("UTF-8");
            // 设置响应的Content-Type为JSON
            resp.setContentType("application/json");

            // 获取响应输出流并将JSON数据写入
            try (PrintWriter out = resp.getWriter()) {
                out.print(jsonResponse);
            }
        }


        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            super.doPost(req, resp);
        }

}
