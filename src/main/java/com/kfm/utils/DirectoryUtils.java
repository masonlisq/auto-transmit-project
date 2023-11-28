package com.kfm.utils;
import com.jcraft.jsch.*;

import java.util.Vector;

/**
 * 检测服务器是否存在目录并新建的工具类
 */
public class DirectoryUtils {
    public static void createDirectory (String ip,String user, String pwd, String path) {

        Session session = null;
        Channel channel = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, ip, 22);
            session.setPassword(pwd);

            // 避免不安全的权限警告
            session.setConfig("StrictHostKeyChecking", "no");

            session.connect(30000);

            channel = (Channel) session.openChannel("sftp");
            channel.connect(10000);

            ChannelSftp sftp = (ChannelSftp) channel;

            try {
                // 切换至给定目录
                sftp.cd(path);
                System.out.println("[提示]远程目录存在：" + path);
                // 递归删除文件夹下所有文件
                recursiveDelete(sftp, path);
                System.out.println("[提示]远程目录删除成功：" + path);
            } catch (SftpException e) {
                System.out.println("[提示]远程目录不存在：" + path);
                // 创建远程目录
                sftp.mkdir(path);
//            sftp.ls(remoteDirectory);
                System.out.println("[提示]远程目录创建成功：" + path);
                return;
            }
            sftp.disconnect();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }
        createDirectory (ip, user, pwd, path);
    }
    // 递归删除目录及其内容
    private static void recursiveDelete(ChannelSftp channelSftp, String remoteDirectory) throws SftpException {
        @SuppressWarnings("unchecked")
        Vector<ChannelSftp.LsEntry> list = channelSftp.ls(remoteDirectory);
        for (ChannelSftp.LsEntry entry : list) {
            String fileName = entry.getFilename();
            if (!fileName.equals(".") && !fileName.equals("..")) {
                if (entry.getAttrs().isDir()) {
                    // 如果是子目录，则递归删除
                    recursiveDelete(channelSftp, remoteDirectory + "/" + fileName);
                } else {
                    // 如果是文件，则删除文件
                    channelSftp.rm(remoteDirectory + "/" + fileName);
                }
            }
        }
        // 删除目录本身
        channelSftp.rmdir(remoteDirectory);
    }

}