package com.kfm.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;

public class EmailUtils {
    public static void sendExceptionMail(String userEmail,String ipAddress) {
        // 创建邮件账户
        MailAccount account = new MailAccount();
        account.setHost("smtp.163.com"); // 设置SMTP服务器
        account.setPort(25); // 设置SMTP端口
        account.setAuth(true); // 启用身份验证
        account.setFrom("*********@163.com"); // 发件人邮箱地址
        account.setUser("**********@163.com"); // 发件人用户名
        account.setPass("************"); // 发件人邮箱密码
        // 发送邮件
        String subject = "[告警！]服务器状态异常提醒";
        String content = "你好！尊敬的服务器管理员！您的服务器"+ipAddress+"出现异常，请及时维护！请及时联系平台管理员！";
        MailUtil.send(account, CollUtil.newArrayList(userEmail),
                subject,content,false);
    }
}
