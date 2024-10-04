package com.ssafy.soltravel.v2.domain;

import com.ssafy.soltravel.v2.dto.group.request.CreateGroupRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TravelGroup {

    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    private String groupAccountNo;

    private String travelStartDate;

    private String travelEndDate;

    private String groupName;

    private String icon;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> participants;

    public static TravelGroup createGroupEntity(String accountNo, CreateGroupRequestDto requestDto) {
        TravelGroup travelGroup = TravelGroup.builder()
            .groupAccountNo(accountNo)
            .travelStartDate(requestDto.getTravelStartDate())
            .travelEndDate(requestDto.getTravelEndDate())
            .groupName(requestDto.getGroupName())
            .icon(requestDto.getIcon())
            .build();

        return travelGroup;
    }
}