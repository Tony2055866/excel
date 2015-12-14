package util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.*;
import java.io.File;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Administrator on 2015/10/7.
 */
public class MailUtil {
    static String YUECHE_SENDER = "yueche@haijia.51itong.net";
    static String STOCK_SENDER = "stock@gaotong.com";


    public static void sendMail(String title, String content, String recver, File file) {
        /*try {
            String cmd = "echo \"" + content + "\" | mail -s \"" + title + "\" " + recver;
            System.out.println("cmd:" + cmd);
            Process pro = Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            Properties props = new Properties();
            Session session = Session.getInstance(props, null);
            // 在属性中设置发送邮件服务器地址与协议
            props.put("mail.host", "127.0.0.1");
            props.put("mail.transport.protocol", "smtp");
            MimeMessage message = new MimeMessage(session);
            // 设置发件人
            message.setFrom(new InternetAddress(YUECHE_SENDER));
            // 设置收件人, Message.RecipientType.CC是设置抄送者
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recver));
            message.setSubject(title); // 邮件主题
            message.setSentDate(new Date()); // 发送时间
            message.setText(content, "utf-8", "html"); //内容   

            /*MimeBodyPart mbpFile = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(file.getName());
            mbpFile.setDataHandler(new DataHandler(fds));
            mbpFile.setFileName(file.getName());*/
            
            Transport.send(message);
        } catch (Exception m) {
            m.printStackTrace();
        }
    }


    public static void doSendHtmlEmail(String subject, String sendHtml, String receiveUser, File attachment) {
        try {
            Properties props = new Properties();
            Session session = Session.getInstance(props, null);
            // 在属性中设置发送邮件服务器地址与协议
            props.put("mail.host", "127.0.0.1");
            props.put("mail.transport.protocol", "smtp");
            MimeMessage message = new MimeMessage(session);
            
            // 发件人
            InternetAddress from = new InternetAddress(STOCK_SENDER);
            message.setFrom(from);

            // 收件人
            InternetAddress to = new InternetAddress(receiveUser);
            message.setRecipient(Message.RecipientType.TO, to);
            InternetAddress gaotong1 = new InternetAddress("gaotong1@xiaomi.com");
            message.setRecipient(Message.RecipientType.CC, gaotong1);

            // 邮件主题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();

            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));
                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }

            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            Transport.send(message);
          /* Transport transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailHost, sender_username, sender_password);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());*/

            System.out.println("send success!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            
        }
    }

   public static void sendMail(String title, String content, String recver) {
         sendMail(title, content, recver, null);
    }

    public static void main(String[] args) {
        sendMail("约车成功1111", "10.23 晚上2222", "gaotong1@xiaomi.com");
    }
}
