package com.kfm.service;

import com.kfm.entity.Server;
import com.kfm.mapper.ServerMapper;
import com.kfm.utils.DirectoryUtils;
import com.kfm.utils.EmailUtils;
import com.kfm.utils.FtpUtils;
import com.kfm.utils.ResponseUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerService {
    static ServerMapper serverMapper = new ServerMapper();
    /**
     * 检测正常的服务器对象集合
     */
    static List<Server> transmitList = new ArrayList<>();
    /**
     * 检测失败的服务器对象集合
     */
    static List<Server> failList = new ArrayList<>();
    /**
     * 所有服务器对象集合
     */
    static List<Server> serverList = new ArrayList<>();
    static ThreadPoolExecutor executorService = new ThreadPoolExecutor(5, 10, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    public List<Server> selectAllServer() {
        List<Server> servers = null;
        try {
            servers = serverMapper.selectAllServers();
        } catch (Exception e) {
            e.printStackTrace();
        }
        servers.forEach(System.out::println);
        return servers;
    }

    public void checkAllServer() {

        try {
            serverList = serverMapper.selectAllServers();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 线程计数
        CountDownLatch countDownLatch = new CountDownLatch(serverList.size());

        // 检测方法
        for (Server server : serverList) {
            executorService.execute(() -> {
                int statusCode = ResponseUtils.checkServerStatus(server.getIp());
                try {
                    if (statusCode == HttpURLConnection.HTTP_OK) {
                        // 记录成功的服务器对象
                        transmitList.add(server);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("成功的地址：");
        transmitList.forEach(System.out::println);
        // 打印本次检测信息的方法
        printConnectMessage(serverList, transmitList);
        // 发送邮件
        notTransmitList(transmitList,serverList);
        // 同步文件
        System.out.println("[提示]开始同步！");
        submitFile(transmitList);
        // 记录清除
        transmitList = null;
        failList = null;
        serverList = null;
    }

    /**
     * 检测信息的方法
     *
     * @param listAll  共检测的服务器对象集合
     * @param listPass 通过检测的服务器对象集合
     */
    public static void printConnectMessage(List<Server> listAll, List<Server> listPass) {
        double route = (double) listPass.size() / listAll.size() * 100;
        System.out.println("[提示]本次共检测 " + listAll.size() + " 台服务器。共有 " + listPass.size() + " 台服务器连接正常！成功率：" + route + "%！");
    }

    /**
     * 同步信息的方法
     *
     * @param list 服务器对象集合
     */
    public static void submitFile(List<Server> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("[提示]请先进行检测！再同步文件！");
            return;
        }
        for (Server server : list) {
            executorService.submit(() -> {
                try {
                    File file = new File(server.getFileListPath());
                    String ip = server.getIp().substring(7);
                    // 判断是否为文件，若为文件直接传输
                    if (file.isDirectory()) {
                        // 若为目录则将整个目录传输
                        // 将目录中所有文件记录在File[]数组中
                        File[] files = file.listFiles();
                        // 新建/检查目录
                        DirectoryUtils.createDirectory(ip, server.getUsername(), server.getPassword(), "/root/" + file.getName());
                        for (File file1 : files) {
                            FileInputStream inputStream = new FileInputStream(file1);
                            byte[] bytes = inputStream.readAllBytes();
                            String filename = file1.getName();
                            FtpUtils.sshSftp(bytes, filename, ip, server.getUsername(), server.getPassword(), "/root/" + file.getName());
                            System.out.println("[提示]" + ip + "：上传文件成功！");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
    public static void notTransmitList(List<Server> successServerList, List<Server> allServerList){
        allServerList.removeAll(successServerList);
        // allServerList变为检测失败的集合
        failList = allServerList;
        if (failList.isEmpty()){
            System.out.println("[提示]没有检测异常的服务器");
        }
        for (Server server : failList) {
            EmailUtils.sendExceptionMail(server.getEmailAddress(),server.getIp());
        }
    }
}