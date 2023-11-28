package com.kfm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Server {
    /**
     * 服务器地址
     */
    private String ip;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 服务器管理员邮箱地址
     */
    private String emailAddress;
    /**
     * 同步文件目录路径
     */
    private String fileListPath;
}
