package com.ssafy.soltravel.v2.repository;

import com.ssafy.soltravel.v2.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
