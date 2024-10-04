package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUser_userId(Long userId);

}
