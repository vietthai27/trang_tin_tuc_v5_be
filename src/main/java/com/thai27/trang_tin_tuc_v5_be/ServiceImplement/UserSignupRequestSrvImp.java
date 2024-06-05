package com.thai27.trang_tin_tuc_v5_be.ServiceImplement;

import com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Exception.UsernameAlreadyExistException;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.UserSignupRequestRepo;
import com.thai27.trang_tin_tuc_v5_be.ServicerInterface.UserSignupRequestService;
import com.thai27.trang_tin_tuc_v5_be.Util.GenerateRandomString;
import com.thai27.trang_tin_tuc_v5_be.Util.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UserSignupRequestSrvImp implements UserSignupRequestService {

    @Autowired
    GenerateRandomString randomString;

    @Autowired
    SendEmail sendEmail;

    @Autowired
    UserSignupRequestRepo userSignupRequestRepo;

    @Autowired
    TrangTinTucUserRepo trangTinTucUserRepo;

    @Override
    public String createSignupRequest(UserSignupRequest userSignupRequest) throws UsernameAlreadyExistException {
        if (!trangTinTucUserRepo.findByUsername(userSignupRequest.getUsername()).isEmpty()) {
            throw new UsernameAlreadyExistException("Tên người dùng " + userSignupRequest.getUsername() + " đã tồn tại !!!");
        } else if (!trangTinTucUserRepo.findByEmail(userSignupRequest.getEmail()).isEmpty()) {
            throw new UsernameAlreadyExistException("Email " + userSignupRequest.getEmail() + " đã tồn tại !!!");
        } else {
            UserSignupRequest createUser = new UserSignupRequest();
            createUser.setUsername(userSignupRequest.getUsername());
            createUser.setPassword(userSignupRequest.getPassword());
            createUser.setEmail(userSignupRequest.getEmail());
            String requestCode = randomString.generateRandomCode(5);
            createUser.setRequestCode(requestCode);
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);
            createUser.setRequestTime(formattedDate);
            userSignupRequestRepo.save(createUser);
            return sendEmail.sendSignupRequest(userSignupRequest.getEmail(), userSignupRequest.getUsername(), requestCode);
        }
    }
}
