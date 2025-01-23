package com.ems.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.Notification;
import com.ems.domain.NotificationType;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	
	/**
	 * Find by id and active.
	 * @param id employee Id or Zero(0). Id=0 means general notifications
	 * @param active Flag
	 * @return Notification
	 */
	Notification findByIdAndActive(Long id, Boolean isActive);
	
	
	/**
	 * Find by Receiver Id and active.
	 * 
	 * @param id employee Id or Zero(0). Id=0 means general notifications
	 * @param active Flag
	 * @return Notification List
	 */
	List<Notification> findByReceiverIdAndActive(Long id, Boolean isActive);
	
	
	/**
	 * Find by Type.
	 * @param Notification Type
	 * @return Notification List
	 */
	List<Notification> findByNotificationType(NotificationType notificationType);
	
	
	/**
	 * Find by id, Not Expired and active.
	 * @param id employee Id or Zero(0). Id=0 means general notifications
	 * @param date Today's Date to check Expiry
	 * @param active Flag
	 * @return List of an employee Notification
	 */
	List<Notification> findByReceiverIdAndExpiryDateGreaterThanEqualAndActive(Long id, DateTime date , Boolean isActive);
	
	
	/**
	 * Find by Not Expired and active.
	 * 
	 * @param date Today's Date to check Expiry
	 * @param active Flag
	 * @return List of an employee Notification
	 */
	List<Notification> findByNotificationTypeAndExpiryDateLessThanEqualAndActive(NotificationType notificationType, DateTime date , Boolean isActive);
	
	
	/**
	 * Find by senderId,receiverId, notificationType and CreationDate.
	 * Get list of notifications 
	 * 
	 * @param senderId employee Id which sends the notification
	 * @param receiverId employee Id which receives the notification
	 * @param notificationType Notification Type
	 * @param startDate Creation Start Date
	 * @param endDate Creation End Date
	 * 
	 * @return List of an employee Notification
	 */
	List<Notification> findBySenderIdAndReceiverIdAndNotificationTypeAndCreationDateBetweenAndExpiryDateGreaterThanEqualAndActive(Long senderId, Long receiverId, NotificationType notificationType, DateTime startDate , DateTime endDate, DateTime expiryDate, Boolean active);
	
}
