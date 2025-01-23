package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.Notification;
import com.ems.exception.ServiceException;
import com.ems.service.AuthenticationService;
import com.ems.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	/**
	 * Get list of Notification 
	 * Service url:  /notification/get method: GET 
	 *
	 * @return List of all Notification
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	List<Notification> getNotifications(@RequestParam(value="admin", defaultValue="false") Boolean adminOnly) throws ServiceException 
	{
		return notificationService.getNotifications(adminOnly);
	}
	
	/**
	 * Sends a Notification (Admin only) 
	 * Service url:  /notification/admin method: PUT 
	 *
	 * @return List of all Notification
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/admin", method = RequestMethod.PUT)
	Long sendAdminNotification(@RequestBody Notification notification) throws ServiceException 
	{
		if(!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("You dont have right. Admin only");
			
		 return notificationService.sendAdminNotification(notification);
	}
	
	
	/**
	 * Sends a birthday Notification Notification 
	 * Service url:  /notification/get method: PUT 
	 *
	 * @return List of all Notification
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/event", method = RequestMethod.PUT)
	void sendBirthdayNotification(@RequestParam("id") Long id,@RequestParam("eventType") String eventType) throws ServiceException 
	{
		 notificationService.sendEventNotification(id,eventType);
	}
	
	
	/**
	 * Delete a Notification (Admin Only)
	 * Service url:  /notification/admin method: DELETE 
	 *
	 * @param Id of Notification
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/admin", method = RequestMethod.DELETE)
	void deleteNotification(@RequestParam("id") Long id) throws ServiceException 
	{
		if(!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("You dont have right. Admin only");
		
		 notificationService.deleteAdminNotification(id);
	}
	
	
	public NotificationController() {
		// TODO Auto-generated constructor stub
	}

}