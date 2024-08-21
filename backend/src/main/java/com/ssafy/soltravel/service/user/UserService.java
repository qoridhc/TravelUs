package com.ssafy.soltravel.service.user;


import com.ssafy.soltravel.dto.user.UserLoginRequestDto;
import com.ssafy.soltravel.dto.user.UserLoginResponseDto;
import com.ssafy.soltravel.dto.user.UserType;
import com.ssafy.soltravel.service.user.social.SocialUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final SocialUserService socialUserService;

    //로그인
    public UserLoginResponseDto loginUser(UserLoginRequestDto request) {
        if(request.getUserType() != UserType.NORMAL) {
            log.info("userLogin(UserController): 소셜 로그인 요청");
            log.info("email: {}", request.getEmail());
            return socialUserService.socialLoginUser(request);
        }
        else{
            //TODO: 소셜 로그인 구현
            return null;
        }
    }
}
