package com.ssafy.soltravel.v2.service.group;

import com.amazonaws.services.kms.model.InvalidGrantIdException;
import com.ssafy.soltravel.v1.domain.Enum.AccountType;
import com.ssafy.soltravel.v2.domain.Participant;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.account.request.InquireAccountRequestDto;
import com.ssafy.soltravel.v2.dto.group.GroupDto;
import com.ssafy.soltravel.v2.dto.group.ParticipantDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateGroupRequestDto;
import com.ssafy.soltravel.v2.dto.group.request.CreateParticipantRequestDto;
import com.ssafy.soltravel.v2.exception.UserNotFoundException;
import com.ssafy.soltravel.v2.exception.account.InvalidGroupAccountException;
import com.ssafy.soltravel.v2.exception.group.InvalidGroupIdException;
import com.ssafy.soltravel.v2.mapper.GroupMapper;
import com.ssafy.soltravel.v2.repository.GroupRepository;
import com.ssafy.soltravel.v2.repository.ParticipantRepository;
import com.ssafy.soltravel.v2.repository.UserRepository;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.util.LogUtil;
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

    private final String BASE_URL = "http://localhost:8080/api/v1/bank/accounts/";
    private final ParticipantRepository participantRepository;

    // 신규 모임 개설
    public GroupDto createNewGroup(CreateGroupRequestDto requestDto) {

        User user = userRepository.findByUserId(requestDto.getUserId())
            .orElseThrow(() -> new UserNotFoundException(requestDto.getUserId()));

        InquireAccountRequestDto inquireAccountRequestDto = new InquireAccountRequestDto(
            requestDto.getUserId(),
            requestDto.getGroupAccountNo(),
            requestDto.getAccountPassword()
        );

        AccountDto accountDto = accountService.getByAccountNo(inquireAccountRequestDto);

        // 모임은 모임계좌타입만 생성 가능
        if(!accountDto.getAccountType() .equals(AccountType.G)){
            throw new InvalidGroupAccountException();
        }

        TravelGroup group = TravelGroup.createGroupEntity(accountDto.getAccountNo(), requestDto);

        groupRepository.save(group);

        return groupMapper.toDto(group);
    }

    public ParticipantDto createNewParticipant(CreateParticipantRequestDto requestDto) {

        User user = userRepository.findByUserId(requestDto.getUserId())
            .orElseThrow(() -> new UserNotFoundException(requestDto.getUserId()));

        TravelGroup group = groupRepository.findById(requestDto.getGroupId())
            .orElseThrow(() -> new InvalidGroupIdException());

        InquireAccountRequestDto inquireAccountRequestDto = new InquireAccountRequestDto(
            user.getUserId(),
            requestDto.getPersonalAccountNo(),
            requestDto.getAccountPassword()
        );

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
