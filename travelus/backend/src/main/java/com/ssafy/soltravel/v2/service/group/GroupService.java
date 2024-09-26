package com.ssafy.soltravel.v2.service.group;

import com.ssafy.soltravel.v2.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.domain.Participant;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.ResponseDto;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.v2.dto.group.request.GroupCodeGenerateRequestDto;
import com.ssafy.soltravel.v2.dto.group.response.GroupCodeGenerateResponseDto;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateGroupRequestDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateParticipantRequestDto;
import com.ssafy.soltravel.v2.dto.group.response.GroupSummaryDto;
import com.ssafy.soltravel.v2.exception.account.InvalidPersonalAccountException;
import com.ssafy.soltravel.v2.exception.group.InvalidGroupIdException;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import com.ssafy.soltravel.v2.mapper.GroupMapper;
import com.ssafy.soltravel.v2.repository.GroupRepository;
import com.ssafy.soltravel.v2.repository.ParticipantRepository;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.service.user.UserService;
import com.ssafy.soltravel.v2.util.LogUtil;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {

    private final Map<String, String> apiKeys;
    private final RedisTemplate<String, String> redisTemplate;

    private final UserService userService;
    private final AccountService accountService;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;

    private final GroupMapper groupMapper;

    private final SecurityUtil securityUtil;

    private final String BASE_URL = "/accounts/";

    // 신규 모임 개설
    public GroupDto createNewGroup(CreateGroupRequestDto requestDto) {

        // 1. 토큰 기반 유저 아이디 추출
        User user = securityUtil.getUserByToken();

        // 2. 모임 계좌 생성
        CreateAccountRequestDto createGroupRequestDto = CreateAccountRequestDto.createDto(
            user.getUserId(),
            AccountType.G,
            requestDto.getGroupAccountPassword(),
            1
        );

        AccountDto newGroupAccount = accountService.createGeneralAccount(createGroupRequestDto);

        // 3. 그룹 생성
        TravelGroup group = TravelGroup.createGroupEntity(newGroupAccount.getAccountNo(), requestDto);
        groupRepository.save(group);

        GroupDto groupDto = groupMapper.toDto(group);

        // 개인 참여자 생성

        // 1. 모임 정산에 사용할 개인 계좌 유효성 검증
        AccountDto personalAccount = accountService.getByAccountNo(requestDto.getPersonalAccountNo());

        if (personalAccount.getAccountType() != AccountType.I) {
            throw new InvalidPersonalAccountException();
        }

        // 2. 참여자 생성
        CreateParticipantRequestDto createParticipantRequestDto = CreateParticipantRequestDto.createDto(
            group.getGroupId(),
            requestDto.getPersonalAccountNo()
        );

        // 3. dto 변환
        ParticipantDto newParticipantDto = createNewParticipant(createParticipantRequestDto, true);

        List<ParticipantDto> participantDtoList = new ArrayList<>();
        participantDtoList.add(newParticipantDto);

        groupDto.setParticipants(participantDtoList);

        return groupDto;
    }

    // 모임 조회
    public GroupDto getGroupInfo(Long groupId) {

        TravelGroup travelGroup = groupRepository.findById(groupId).orElseThrow(InvalidGroupIdException::new);

        return groupMapper.toDto(travelGroup);

    }

    // 신규 참여자 생성
    public ParticipantDto createNewParticipant(CreateParticipantRequestDto requestDto, boolean isMaster) {

        // 1. 토큰 기반 유저 아이디 추출
        User user = securityUtil.getUserByToken();

        // 2. 그룹 조회
        TravelGroup group = groupRepository.findById(requestDto.getGroupId()).orElseThrow(InvalidGroupIdException::new);

        AccountDto accountDto = accountService.getByAccountNo(requestDto.getPersonalAccountNo());

        // 3. 참여자 생성

        Participant participant = Participant.createParticipant(
            user,
            group,
            isMaster,
            requestDto.getPersonalAccountNo()
        );

        // 4. 참여자 -> 모임 추가
        participantRepository.save(participant);

        ParticipantDto participantDto = groupMapper.toParticipantDto(participant);

        return participantDto;
    }

    // 특정 유저가 가입한(생성 x) 모임 전체 조회
    public List<GroupSummaryDto> getAllJoinedGroup(boolean isMaster) {

        // 1. 토큰 기반 유저 아이디 추출
        User user = securityUtil.getUserByToken();

        List<TravelGroup> groupList = participantRepository.findAllGroupsByUserId(user.getUserId(), isMaster);

        return groupList.stream()
            .map((group) -> {
                AccountDto accountDto = accountService.getByAccountNo(group.getGroupAccountNo());

                return GroupSummaryDto.createFromAccountDto(group, accountDto.getMoneyBoxDtos());
            })
            .toList();
    }

    
    /*
    * 모임 코드 생성
    */
    public GroupCodeGenerateResponseDto generateGroupCode(GroupCodeGenerateRequestDto request) {
        User user = userRepository.findGroupMasterByGroupIdAndUserId(
            request.getGroupId(),
            securityUtil.getCurrentUserId()
        ).orElseThrow(
            ()-> new UserNotFoundException(securityUtil.getCurrentUserId())
        );

        String code = generateGroupCode(request.getGroupId());
        redisTemplate.opsForValue().set(code, String.valueOf(request.getGroupId()), 30, TimeUnit.SECONDS);

        return GroupCodeGenerateResponseDto.builder()
            .groupCode(code)
            .build();
    }

    public GroupDto findGroupByCode(String code) {
        String groupId = redisTemplate.opsForValue().get(code);
        if(groupId == null) {
            throw new InvalidGroupIdException("유효하지 않은 초대 코드입니다.");
        }
        return getGroupInfo(Long.valueOf(groupId));
    }

    public ResponseDto validGroupCode(String code) {
        if(redisTemplate.hasKey(code)) {
            Long expireTime = redisTemplate.getExpire(code);
            return new ResponseDto(String.valueOf(expireTime));
        }

        LogUtil.info("키 존재하지 않음", code);
        return new ResponseDto("유효하지 않은 키입니다.");
    }


    private String generateGroupCode(Long groupId){
        UUID uuid = UUID.randomUUID();

        byte[] uuidBytes = ByteBuffer.wrap(new byte[16])
            .putLong(uuid.getMostSignificantBits())
            .putLong(uuid.getLeastSignificantBits())
            .array();

        String urlSafeUuid = Base64.getUrlEncoder().withoutPadding().encodeToString(uuidBytes);
        return urlSafeUuid;
    }
}
