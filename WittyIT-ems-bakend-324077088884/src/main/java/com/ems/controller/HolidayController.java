package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.Holiday;
import com.ems.exception.ServiceException;
import com.ems.service.AuthenticationService;
import com.ems.service.HolidayService;
import com.ems.wrappers.FileUploadWrapper;
import com.ems.wrappers.HolidayWrapper;

@RestController
@RequestMapping("/holiday")
public class HolidayController {
	
	@Autowired
	private HolidayService holidayService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	/**
	 * Upload holiday banner 
	 * Service url:  /holiday/image/upload method: POST 
	 *
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/image/upload", method = RequestMethod.POST)
	void upload(@RequestParam("id") Integer id, FileUploadWrapper uploadWrapper) throws ServiceException 
	{	
		holidayService.uploadHolidayImage(uploadWrapper, id);
	}
	
	/**
	 * Get list of All Holidays 
	 * Service url:  /holiday/list method: GET 
	 *
	 * @return List of all Holidays
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	List<HolidayWrapper> getHolidays(@RequestParam(value="admin", defaultValue="false", required = false) Boolean adminMode) throws ServiceException 
	{	
		return holidayService.getHolidays(adminMode);
	}
	
	/**
	 * Get specific Holiday by ID
	 * Service url:  /holiday/get method: GET 
	 *
	 * @param Holiday Id
	 * @return Holiday
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	HolidayWrapper getHoliday(@RequestParam("id") Integer id) throws ServiceException 
	{	
		return holidayService.getHoliday(id);
	}
	
	/**
	 * Add and Edits a holiday
	 * Service url:  /holiday/save method: PUT 
	 *
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/save", method = RequestMethod.PUT)
	Holiday addHoliday(@RequestBody HolidayWrapper holidayWrapper) throws ServiceException 
	{
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("Only admin and HR have access to these.");
		return holidayService.addHoliday(holidayWrapper);
	}
	
	/**
	 * Delete a holiday
	 * Service url:  /holiday/delete method: DELETE 
	 *
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	void deleteHoliday(@RequestParam("id") Integer id) throws ServiceException 
	{
		if (!authenticationService.isAdmin() && !authenticationService.isHR())
			throw new ServiceException("Only admin and HR have access.");
		holidayService.deleteHoliday(id);
	}
}
