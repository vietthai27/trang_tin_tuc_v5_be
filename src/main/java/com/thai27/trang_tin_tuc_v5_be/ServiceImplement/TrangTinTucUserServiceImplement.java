package com.thai27.trang_tin_tuc_v5_be.ServiceImplement;


import com.thai27.trang_tin_tuc_v5_be.DTO.UserListDto;
import com.thai27.trang_tin_tuc_v5_be.Entity.TrangTinTucUser;
import com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Exception.ResourceNotFoundException;
import com.thai27.trang_tin_tuc_v5_be.Exception.TokenExpiredException;
import com.thai27.trang_tin_tuc_v5_be.Repository.RoleRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.TrangTinTucUserRepo;
import com.thai27.trang_tin_tuc_v5_be.Repository.UserSignupRequestRepo;
import com.thai27.trang_tin_tuc_v5_be.Request.UserChangePasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.Request.UserResetPasswordRequest;
import com.thai27.trang_tin_tuc_v5_be.Request.UserValidateSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Response.UserListResponse;
import com.thai27.trang_tin_tuc_v5_be.Security.JWTAuthenProvider;
import com.thai27.trang_tin_tuc_v5_be.Security.JWTUltil;
import com.thai27.trang_tin_tuc_v5_be.ServicerInterface.TrangTinTucUserServiceInterface;
import com.thai27.trang_tin_tuc_v5_be.Util.GenerateRandomString;
import com.thai27.trang_tin_tuc_v5_be.Util.SendEmail;
import io.jsonwebtoken.Claims;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrangTinTucUserServiceImplement implements TrangTinTucUserServiceInterface {

    @Autowired
    JWTUltil jwtUtil;

    @Autowired
    JWTAuthenProvider jwtAuth;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    TrangTinTucUserRepo trangTinTucUserRepo;

    @Autowired
    UserSignupRequestRepo userSignupRequestRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    GenerateRandomString randomString;

    @Autowired
    SendEmail sendEmail;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public String login(TrangTinTucUser userData) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        userData.getUsername(),
                        userData.getPassword()
                );
        Authentication authen = jwtAuth.authenticate(token);
        String jwtToken = jwtUtil.generate(authen);
        return jwtToken;
    }

    @Override
    public String userSignup(UserValidateSignupRequest request) throws ResourceNotFoundException {
        UserSignupRequest userSignupRequest =
                userSignupRequestRepo.findByRequestCode(request.getValidateCode()).orElseThrow(() -> new ResourceNotFoundException("Mã xác thực " + request.getValidateCode() + " không tồn tại trong hệ thống"));
        if (!userSignupRequestRepo.getCodeByEmail(request.getEmail()).equals(request.getValidateCode())) {
            throw new RuntimeException("Mã xác thực đã hết hạn vui lòng kiểm tra lại email");
        } else {
            TrangTinTucUser userData = new TrangTinTucUser();
            userData.setUsername(userSignupRequest.getUsername());
            userData.setPassword(encoder.encode(userSignupRequest.getPassword()));
            userData.setEmail(userSignupRequest.getEmail());
            userData.setRoles(roleRepo.findByRolename("USER"));
            trangTinTucUserRepo.save(userData);
            userSignupRequestRepo.deleteAllByEmail(userSignupRequest.getEmail());
            return sendEmail.sendSignupSuccess(userSignupRequest.getEmail(), userSignupRequest.getUsername());
        }
    }

    @Override
    public Claims getClaimsFromToken(String token) throws TokenExpiredException {
        try {
            return jwtUtil.getClaims(token);
        } catch (Exception e) {
            throw new TokenExpiredException("Phiên đăng nhập hết hạn vui lòng đăng nhập lại");
        }
    }

    @Override
    public UserListResponse getAllUser(Integer pageNum, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<TrangTinTucUser> userListEntity = trangTinTucUserRepo.findAll(pageRequest);
        List<UserListDto> userListDto = userListEntity
                .stream().map(userList -> modelMapper.map(userList, UserListDto.class))
                .collect(Collectors.toList());
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setContent(userListDto);
        userListResponse.setPageNo(userListEntity.getNumber());
        userListResponse.setPageSize(userListEntity.getSize());
        userListResponse.setTotalElements(userListEntity.getTotalElements());
        userListResponse.setTotalPages(userListEntity.getTotalPages());
        userListResponse.setLast(userListEntity.isLast());
        return userListResponse;
    }

    @Override
    public TrangTinTucUser getUserById(Long id) throws ResourceNotFoundException {
        return trangTinTucUserRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Không có người dùng với id:" + id));
    }

    @Override
    public String setUserModerRole(Long userId) throws ResourceNotFoundException {
        if (!trangTinTucUserRepo.existsById(userId)) {
            throw new ResourceNotFoundException("Không có người dùng với id:" + userId);
        } else roleRepo.setUserModerRole(userId);
        return "Thêm quyền Moder thành công";
    }

    @Override
    public String unsetUserModerRoles(Long userId) throws ResourceNotFoundException {
        if (!trangTinTucUserRepo.existsById(userId)) {
            throw new ResourceNotFoundException("Không có người dùng với id:" + userId);
        } else trangTinTucUserRepo.unsetUserModerRole(userId);
        return "Hủy quyền Moder thành công";
    }

    @Override
    public UserListResponse findAllByUsername(String username, Integer pageNum, Integer pageSize) {
        String likeUsername = "%" + username + "%";
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<TrangTinTucUser> userListEntity = trangTinTucUserRepo.
                findAllByUsernameLikeIgnoreCase(likeUsername, pageRequest);
        List<UserListDto> userListSearchDto = userListEntity
                .stream().map(userList -> modelMapper.map(userList, UserListDto.class))
                .collect(Collectors.toList());
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.setContent(userListSearchDto);
        userListResponse.setPageNo(userListEntity.getNumber());
        userListResponse.setPageSize(userListEntity.getSize());
        userListResponse.setTotalElements(userListEntity.getTotalElements());
        userListResponse.setTotalPages(userListEntity.getTotalPages());
        userListResponse.setLast(userListEntity.isLast());
        return userListResponse;
    }

    @Override
    public String deleteUserById(Long userId) {
        trangTinTucUserRepo.deleteAllUserRoles(userId);
        trangTinTucUserRepo.deleteUser(userId);
        return "Xóa người dùng thành công";
    }

    @Override
    public String resetPassword(UserResetPasswordRequest userRequest) throws ResourceNotFoundException {
        TrangTinTucUser resetUser = trangTinTucUserRepo.findByUsername(userRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException("Tên người dùng không tồn tại trong hệ thống: " + userRequest.getUsername()));
        if (userRequest.getEmail().equals(resetUser.getEmail())) {
            String newPassword = randomString.generateRandomCode(8);
            String newEncodedPassword = encoder.encode(newPassword);
            resetUser.setPassword(newEncodedPassword);
            trangTinTucUserRepo.save(resetUser);
            sendEmail.sendNewPassword(resetUser.getEmail(), resetUser.getUsername(), newPassword);
            return "Đổi mật khẩu thành công, vui lòng check emal để biết mật khẩu mới";
        } else throw new RuntimeException("Email người dùng nhập không khớp với email đã đăng ký");
    }

    @Override
    public String changePassword(UserChangePasswordRequest userRequest) throws ResourceNotFoundException {
        String username = jwtUtil.getUsername(userRequest.getToken());
        TrangTinTucUser changePassUser = trangTinTucUserRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Tên người dùng không tồn tại trong hệ thống: " + username));
        if (encoder.matches(userRequest.getOldPassword(), changePassUser.getPassword())) {
            changePassUser.setPassword(encoder.encode(userRequest.getNewPassword()));
            trangTinTucUserRepo.save(changePassUser);
            return "Đổi mật khẩu thành công vui lòng đăng nhập lại";
        } else throw new RuntimeException("Mật khẩu người dùng nhập không trùng với mật khẩu trên hệ thống");
    }

}
