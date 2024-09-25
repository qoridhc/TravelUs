package com.ssafy.soltravel.v2.service.group;

import com.ssafy.soltravel.v1.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.domain.Participant;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.account.request.CreateAccountRequestDto;
import com.ssafy.soltravel.v2.dto.account.request.InquireAccountRequestDto;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateGroupRequestDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateParticipantRequestDto;
import com.ssafy.soltravel.v2.exception.user.UserNotFoundException;
import com.ssafy.soltravel.v2.exception.account.InvalidPersonalAccountException;
import com.ssafy.soltravel.v2.exception.group.InvalidGroupIdException;
import com.ssafy.soltravel.v2.mapper.GroupMapper;
import com.ssafy.soltravel.v2.repository.GroupRepository;
import com.ssafy.soltravel.v2.repository.ParticipantRepository;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.util.SecurityUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {

    private final Map<String, String> apiKeys;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    private final AccountService accountService;

    private final GroupMapper groupMapper;

    private final String BASE_URL = "/accounts/";
    private final ParticipantRepository participantRepository;

    // 신규 모임 개설
    public GroupDto createNewGroup(CreateGroupRequestDto requestDto) {

        // 1. 토큰 기반 유저 아이디 추출
        Long userId = SecurityUtil.getCurrentUserId();

        // 2. 유저 유효성 검사
        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        // 3. 모임 계좌 생성
        CreateAccountRequestDto createGroupRequestDto = CreateAccountRequestDto.createDto(
            userId,
            AccountType.G,
            requestDto.getGroupAccountPassword(),
            1
        );

        AccountDto newGroupAccount = accountService.createGeneralAccount(createGroupRequestDto);

        // 4. 그룹 생성
        TravelGroup group = TravelGroup.createGroupEntity(newGroupAccount.getAccountNo(), requestDto);
        groupRepository.save(group);

        GroupDto groupDto = groupMapper.toDto(group);

        // 개인 참여자 생성

        // 1. 모임 정산에 사용할 개인 계좌 유효성 검증
        InquireAccountRequestDto inquireAccountRequestDto = new InquireAccountRequestDto(requestDto.getPersonalAccountNo());

        AccountDto personalAccount = accountService.getByAccountNo(inquireAccountRequestDto);

        if (!personalAccount.getAccountType().equals(AccountType.I)) {
            throw new InvalidPersonalAccountException();
        }

        // 2. 참여자 생성
        CreateParticipantRequestDto createParticipantRequestDto = CreateParticipantRequestDto.createDto(
            userId,
            group.getGroupId(),
            true,
            requestDto.getPersonalAccountNo()
        );

        // 3. dto 변환
        ParticipantDto newParticipantDto = createNewParticipant(createParticipantRequestDto);

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
    public ParticipantDto createNewParticipant(CreateParticipantRequestDto requestDto) {

        Long userId = SecurityUtil.getCurrentUserId();

        User user = userRepository.findByUserId(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        TravelGroup group = groupRepository.findById(requestDto.getGroupId())
            .orElseThrow(InvalidGroupIdException::new);

        InquireAccountRequestDto inquireAccountRequestDto = new InquireAccountRequestDto(requestDto.getPersonalAccountNo());

        AccountDto accountDto = accountService.getByAccountNo(inquireAccountRequestDto);

        Participant participant = Participant.createParticipant(
            user,
            group,
            requestDto.isMaster(),
            requestDto.getPersonalAccountNo()
        );

        participantRepository.save(participant);

        ParticipantDto participantDto = groupMapper.toParticipantDto(participant);

        return participantDto;
    }

}
