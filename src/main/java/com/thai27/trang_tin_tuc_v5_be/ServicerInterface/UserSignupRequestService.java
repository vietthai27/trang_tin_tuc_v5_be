package com.thai27.trang_tin_tuc_v5_be.ServicerInterface;

import com.thai27.trang_tin_tuc_v5_be.Entity.UserSignupRequest;
import com.thai27.trang_tin_tuc_v5_be.Exception.UsernameAlreadyExistException;

public interface UserSignupRequestService {

    String createSignupRequest (UserSignupRequest userSignupRequest) throws UsernameAlreadyExistException;
}
