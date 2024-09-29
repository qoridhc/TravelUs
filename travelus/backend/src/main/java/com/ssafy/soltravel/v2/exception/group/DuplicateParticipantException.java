package com.ssafy.soltravel.v2.exception.group;

import com.ssafy.soltravel.v2.exception.CustomException;

public class DuplicateParticipantException extends CustomException {

    private static final String DEFAULT_MESSAGE = "해당 유저는 이미 이 그룹에 참여하고 있습니다.";
    private static final String DEFAULT_CODE = "DUPLICATE_PARTICIPANT";
    private static final int DEFAULT_STATUS = 400; // Bad Request

    public DuplicateParticipantException(Long groupId, Long userId) {
        super(
            DEFAULT_MESSAGE,
            DEFAULT_CODE,
            DEFAULT_STATUS,
            String.format("%s: 그룹 ID %d, 유저 ID %d", DEFAULT_MESSAGE, groupId, userId)
        );
    }
}
