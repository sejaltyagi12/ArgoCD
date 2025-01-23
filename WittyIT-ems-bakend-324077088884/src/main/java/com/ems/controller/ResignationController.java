package com.ems.controller;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.Resignation;
import com.ems.domain.ResignationReason;
import com.ems.domain.ResignationType;
import com.ems.exception.ServiceException;
import com.ems.service.ResignationService;
import com.ems.wrappers.ResignationWrapper;

@RestController
@RequestMapping("/resignation")
public class ResignationController {
	
	@Autowired
	ResignationService resignationService ;
	
	/**
	 * Submit a resignation.
	 * Service url: 
	 * /resignation/employee method: PUT
	 * 
	 * @param resignation
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(value="/employee", method=RequestMethod.PUT)
	ResignationWrapper submitResignation(@RequestBody Resignation resignation) throws ServiceException
	{
		return resignationService.addResignation(resignation);
	}
	
	/**
	 * Get current resignation of an employee.
	 * Service url: 
	 * /resignation/employee/current method: GET
	 * 
	 * @param resignation
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(value="/employee/current", method=RequestMethod.GET)
	List<ResignationWrapper> getCurrentResignation() throws ServiceException
	{
		return resignationService.getCurrentResignation();
	}
	
	/**
	 * Submit a manager approval for existing resignation.
	 * Service url: 
	 * /resignation/approve/manager method: POST
	 * 
	 * @param resignation
	 * @return resignation
	 * @throws ServiceException
	 */
	@RequestMapping(value="approve/manager", method=RequestMethod.POST)
	ResignationWrapper submitManagerApproval(@RequestBody Resignation resignation) throws ServiceException
	{
		return resignationService.submitManagerApproval(resignation);
	}
	
	/**
	 * Submit a HR manager approval for existing resignation.
	 * Service url: 
	 * /resignation/approve/hr method: POST
	 * 
	 * @param resignation
	 * @return resignation
	 * @throws ServiceException
	 */
	@RequestMapping(value="approve/hr", method=RequestMethod.POST)
	ResignationWrapper submitHrApproval(@RequestBody Resignation resignation) throws ServiceException
	{
		return resignationService.submitHrApproval(resignation);
	}
	
	/**
	 * Gets all resignation by team.
	 * Service url: 
	 * /resignation/submitted-list method: GET
	 * 
	 * @param id the resignation Id (Optional)
	 * @param emp the employee Id (Optional)
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(value="/submitted-list", method=RequestMethod.GET)
	Object getResignationList(@RequestParam(value="id", required=false) Long id , @RequestParam(value="emp", required=false) Long empId) throws ServiceException
	{
		return resignationService.getResignationList(id, empId);
	}
	
	
	/**
	 * Gets all resignation types.
	 * Service url: 
	 * /resignation/types/resignation-types method: GET
	 * 
	 * @return ResignationType List
	 * @throws ServiceException
	 */
	@RequestMapping(value="types/resignation-types", method=RequestMethod.GET)
	List<ResignationType> getResignationTypes() throws ServiceException
	{
		return resignationService.getResignationTypes();
	}
	

	/**
	 * Gets all resignation reasons.
	 * Service url: 
	 * /resignation/types/resignation-reasons method: GET
	 * 
	 * @return ResignationReason List
	 * @throws ServiceException
	 */
	@RequestMapping(value="types/resignation-reasons", method=RequestMethod.GET)
	List<ResignationReason> getResignationReasons() throws ServiceException
	{
		return resignationService.getResignationReasons();
	}

	/**
	 * To Re Hire an Employee Service url: /resignation/activate/employee method:
	 * POST
	 * 
	 * @param pathvariable
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/activate/employee/{empCode}/{date}", method = RequestMethod.POST)
	String activateEmployee(@PathVariable String empCode, @PathVariable String date) throws ServiceException {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime dt = formatter.parseDateTime(date);
		return resignationService.activateEmployee(empCode, dt);
	}

}
