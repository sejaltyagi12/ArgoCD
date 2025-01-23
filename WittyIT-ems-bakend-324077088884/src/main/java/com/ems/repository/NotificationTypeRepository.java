package com.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.NotificationType;

public interface NotificationTypeRepository extends JpaRepository<NotificationType, Long> {

}
