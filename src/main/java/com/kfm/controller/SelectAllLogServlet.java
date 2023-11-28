package com.kfm.controller;

import com.kfm.service.LogService;
import com.kfm.entity.Log;
import com.kfm.utils.JSONUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
@WebServlet("/selectAllLog")
public class SelectAllLogServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LogService logService = new LogService();
        List<Log> logList = logService.selectAllLog();
        // 转换服务器数据为JSON格式
        String jsonResponse = JSONUtils.convertToJSON(logList);

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
