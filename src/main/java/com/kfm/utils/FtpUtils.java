package com.kfm.utils;


import com.jcraft.jsch.*;
import com.jcraft.jsch.Channel;

import java.io.OutputStream;

/**
 * 上传文件至服务器的工具类
 */
public class FtpUtils {

    /**
     * 利用JSch包实现SFTP上传文件
     *
     * @param bytes    文件字节流
     * @param fileName 文件名
     * @throws Exception 抛出异常
     */
    public static void sshSftp(byte[] bytes, String fileName, String IP, String username, String passWord, String filePath) throws Exception {

        //服务器端口 默认22
        int port = 22;
        //上传文件到指定服务器的指定目录 自行修改

        Session session = null;
        Channel channel = null;

        JSch jsch = new JSch();

        if (port <= 0) {
            //连接服务器，采用默认端口
            session = jsch.getSession(username, IP);
        } else {
            //采用指定的端口连接服务器
            session = jsch.getSession(username, IP, port);
        }

        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置登陆主机的密码
        session.setPassword(passWord);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);

        OutputStream outstream = null;
        try {
            //创建sftp通信通道
            channel = (Channel) session.openChannel("sftp");
            channel.connect(1000);
            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.cd(filePath);
            System.out.println("[提示]远程文件存在：" + filePath);

            outstream = sftp.put(fileName);
            outstream.write(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关流操作
            if (outstream != null) {
                outstream.flush();
                outstream.close();
            }
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

}