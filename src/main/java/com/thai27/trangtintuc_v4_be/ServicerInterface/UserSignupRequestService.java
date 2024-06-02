package com.thai27.trangtintuc_v4_be.ServicerInterface;

import com.thai27.trangtintuc_v4_be.Entity.UserSignupRequest;
import com.thai27.trangtintuc_v4_be.Exception.UsernameAlreadyExistException;

public interface UserSignupRequestService {

    String createSignupRequest (UserSignupRequest userSignupRequest) throws UsernameAlreadyExistException;
}
