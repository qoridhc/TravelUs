package com.ssafy.soltravel.v2.service.fcm;

import com.ssafy.soltravel.v2.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final SecurityUtil securityUtil;

//    // FireBase 토큰 redis에 저장
//    public ResponseEntity<?> saveToken(String fcmToken, long member_id) {
//        Map<String, Object> resultMap = new HashMap<>();
//        HttpStatus status = null;
//
//        User user = securityUtil.getUserByToken();
//        redisUtil.save(user.getPhone(), fcmTokenDto.getToken());
//        return new ResponseEntity<>(resultMap, status);
//    }


}
