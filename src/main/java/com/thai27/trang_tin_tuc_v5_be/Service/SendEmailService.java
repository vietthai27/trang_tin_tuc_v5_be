package com.thai27.trang_tin_tuc_v5_be.Service;

import com.thai27.trang_tin_tuc_v5_be.Util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailService {

    private final JavaMailSender mailSender;

    public String sendNewPassword(String userMail, String username, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Constant.EMAIL_SENDER);
        message.setTo(userMail);
        message.setSubject("Tài khoản " + username + " đã reset mật khẩu thành công");
        message.setText("Mật khẩu mới là: " + password);
        mailSender.send(message);
        return "Reset mật khẩu thành công vui lòng check mail !";
    }

    public String sendSignupSuccess(String userMail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Constant.EMAIL_SENDER);
        message.setTo(userMail);
        message.setSubject("WELCOME");
        message.setText("Tài khoản với tên người dùng: " + username + " đã đăng ký thành công");
        try {
            mailSender.send(message);
        } catch (Exception e) {
            return "Email không hợp lệ vui lòng thử email khác";
        }
        return "Đăng ký tài khoản " + username + " thành công !!!";
    }

    public String sendSignupRequest(String userMail, String username, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(Constant.EMAIL_SENDER);
        message.setTo(userMail);
        message.setSubject("Đăng ký tạo tài thành công");
        message.setText("Bạn vừa đăng ký tạo tài khoản với tên người dùng: " + username + ". " +
                "Vui lòng nhập mã xác thực: " + code + " để tạo tài khoản trên hệ thống");
        try {
            mailSender.send(message);
        } catch (Exception e) {
            return "Email không hợp lệ vui lòng thử email khác";
        }
        return "Đăng ký tạo tài khoản thành công vui lòng kiểm tra email !!!";
    }

}
