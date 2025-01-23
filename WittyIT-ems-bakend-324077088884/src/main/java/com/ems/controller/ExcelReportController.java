package com.ems.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ems.configuration.PropConfig;
import com.ems.exception.ServiceException;
import com.ems.service.AuthenticationService;
import com.ems.service.EmployeeService;
import com.ems.service.EvaluationTargetService;
import com.ems.service.LeaveService;
import com.ems.servicefinder.utils.ExcelType;

@Controller
public class ExcelReportController {
	
	@Autowired
	private LeaveService leaveService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EvaluationTargetService targetService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private PropConfig propConfig;
	
	/**
     * Handle request to download an Excel document
	 * @throws ServiceException 
	 * @throws ParseException 
     */
    @RequestMapping(value = "/reports/leave/download", method = RequestMethod.GET)
    public String downloadLeaveList(@RequestParam("start") String startDate, @RequestParam("end") String endDate, Model model) throws ServiceException, ParseException {
        model.addAttribute("leaveList", leaveService.getLeaveBetween(startDate, endDate));
        model.addAttribute("companyCategory", authenticationService.getAuthenticatedEmployee().getCompanyCategory());
        model.addAttribute("type", ExcelType.LEAVE);
        
        return "";
    }
    
    /**
     * Handle request to download an Excel document
	 * @throws ServiceException 
     * @throws ParseException 
     */
    @RequestMapping(value = "/reports/joinees/download", method = RequestMethod.GET)
    public String downloadJoinees(@RequestParam("start") String startDate, @RequestParam("end") String endDate, Model model) throws ServiceException, ParseException {
        model.addAttribute("employeeList", employeeService.getJoineeBetween(startDate, endDate));
        model.addAttribute("type", ExcelType.JOINEE);
        
        return "";
    }
    
    
    /**
     * Handle request to download an Excel document
	 * @throws ServiceException 
     * @throws ParseException 
     */
    @RequestMapping(value = "/reports/evaluation/download", method = RequestMethod.GET)
    public String downloadEvaluationReport(@RequestParam("cycle") Long cycleId, Model model) throws ServiceException, ParseException {
        model.addAttribute("targetList",  targetService.getEvaluationTargetsByCycle(cycleId));
        model.addAttribute("evaluations", targetService.getManagerPendingEvaluations(cycleId));
        model.addAttribute("type", ExcelType.EVALUATION);
        
        return "";
    }

}
