package com.ems.service;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ems.configuration.PropConfig;
import com.ems.domain.Employee;
import com.ems.domain.Notification;
import com.ems.domain.NotificationType;
import com.ems.exception.ServiceException;
import com.ems.framework.DateUtil;
import com.ems.repository.NotificationRepository;
import com.ems.repository.NotificationTypeRepository;

@Service
@Transactional
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private NotificationTypeRepository notificationTypeRepository;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private PropConfig propConfig;

	// Return Birthday Notification Type
	public NotificationType getNotificationType(String eventType) {
		List<NotificationType> notificationTypes = notificationTypeRepository.findAll();

		for (NotificationType notificationType : notificationTypes) {
			if (notificationType.getType().equalsIgnoreCase(eventType))
				return notificationType;
		}

		return null;
	}

	// Return Birthday Notification Type
	public NotificationType getAdminNotificationType() {
		List<NotificationType> notificationTypes = notificationTypeRepository.findAll();

		for (NotificationType notificationType : notificationTypes) {
			if (notificationType.getType().toLowerCase().contains("admin"))
				return notificationType;
		}

		return null;
	}

	/**
	 * Fetch all Notifications of an Employee.
	 *
	 * 
	 * @return list of Notification
	 * @throws ServiceException
	 */
	public List<Notification> getNotifications(Boolean adminOnly) throws ServiceException {

		Employee employee = authenticationService.getAuthenticatedEmployee();

		DateTime today = DateUtil.resetTime(new DateTime());

		List<Notification> expiryNotifications = notificationRepository
				.findByNotificationTypeAndExpiryDateLessThanEqualAndActive(getAdminNotificationType(),
						today.minusHours(1), true);

		for (Notification notification : expiryNotifications) {
			notification.setActive(false);
			notification.setActualExpiryDate(notification.getExpiryDate());
			notificationRepository.saveAndFlush(notification);
		}

		// Default notifications
		List<Notification> notifications = notificationRepository.findByReceiverIdAndActive(0L, true);

		if (adminOnly)
			notifications = notificationRepository.findByNotificationType(getAdminNotificationType());
		else
			notifications.addAll(notificationRepository.findByReceiverIdAndExpiryDateGreaterThanEqualAndActive(
					employee.getId(), today.minusHours(1), true));

		return notifications;
	}

	/**
	 * Send the birthday wishes notifications
	 *
	 * 
	 * @param employee
	 *            id of employee to be wished
	 * @throws ServiceException
	 */
	public void sendEventNotification(Long id, String eventType) throws ServiceException {
		Employee authenticatedEmployee = authenticationService.getAuthenticatedEmployee();

		DateTime today = new DateTime().withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);

		List<Notification> notifications = notificationRepository
				.findBySenderIdAndReceiverIdAndNotificationTypeAndCreationDateBetweenAndExpiryDateGreaterThanEqualAndActive(
						authenticatedEmployee.getId(), id, getNotificationType(eventType), today.minusMonths(2),
						today.plusDays(1), today, true);

		if (!notifications.isEmpty())
			throw new ServiceException("Already Wished");

		Employee eventEmployee = employeeService.findById(id);

		if (eventEmployee != null) {
			DateTime expiry = new DateTime(
					eventType.equalsIgnoreCase("Birthday") ? eventEmployee.getDob() : eventEmployee.getJoiningDate())
							.withYear(today.getYear()).plusDays(1);

			String notificationText = "Wish you a very Happy " + eventType + ". - "
					+ authenticatedEmployee.getFullName();

			notificationRepository.save(new Notification(authenticatedEmployee.getId(), id, notificationText,
					DateUtil.resetTime(expiry), getNotificationType(eventType)));
		} else {
			throw new ServiceException("Employee not found.");
		}
	}

	/**
	 * Send admin notifications
	 *
	 * 
	 * @param Notifaication
	 * @throws ServiceException
	 * @see {@link Notification}
	 */
	public Long sendAdminNotification(Notification notification) throws ServiceException {
		Employee authenticatedEmployee = authenticationService.getAuthenticatedEmployee();

		if (notification.getText() == null || notification.getExpiryDate() == null)
			throw new ServiceException("Invalid data. Text or expiry date missing.");

		notification.setSenderId(authenticatedEmployee.getId());
		notification.setReceiverId(0L);
		notification.setNotificationType(getAdminNotificationType());

		notificationRepository.save(notification);

		return notification.getId();
	}

	/**
	 * Delete a notifications
	 *
	 * 
	 * @param Notification
	 * @throws ServiceException
	 * @see {@link Notification}
	 */
	public void deleteAdminNotification(Long id) throws ServiceException {
		Notification notification = notificationRepository.findByIdAndActive(id, true);

		if (notification.getNotificationType() != getAdminNotificationType())
			throw new ServiceException("Not an admin notification.");

		notification.setActive(false);
		notification.setActualExpiryDate(new DateTime());

		notificationRepository.save(notification);
	}

}
