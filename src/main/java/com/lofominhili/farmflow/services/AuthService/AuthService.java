package com.lofominhili.farmflow.services.AuthService;

import com.lofominhili.farmflow.dto.EntityDTO.UserDTO;
import com.lofominhili.farmflow.dto.RequestDTO.SignInRequest;
import com.lofominhili.farmflow.exceptions.AuthenticationFailedException;

public interface AuthService {
    void registerUser(UserDTO request) throws AuthenticationFailedException;

    String signIn(SignInRequest credentials) throws AuthenticationFailedException;
}
