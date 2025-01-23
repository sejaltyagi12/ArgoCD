package com.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ems.configuration.PropConfig;
import com.ems.domain.PaySlip;
import com.ems.exception.ServiceException;
import com.ems.service.EmployeeService;
import com.ems.service.PayrollService;

@Controller
public class PdfGeneratorController {
	
	@Autowired
	private PayrollService payrollService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private PropConfig propConfig;
	
	/**
     * Handle request to download an Excel document
	 * @throws ServiceException 
     */
    @RequestMapping(value = "/pay-slip/download", method = RequestMethod.GET)
    public String download(@RequestParam("id") Long paySlipId, Model model) throws ServiceException {
    	PaySlip paySlip = payrollService.getPaySlip(paySlipId);
        model.addAttribute("paySlip", paySlip);
        model.addAttribute("employee", employeeService.findById(paySlip.getEmpId()));
        model.addAttribute("logoPath", propConfig.getLogoPath());
        
        return "";
    }

}
