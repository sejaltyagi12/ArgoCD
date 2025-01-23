package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.PaySlip;
import com.ems.domain.PayrollFixed;
import com.ems.domain.PayrollVariable;
import com.ems.exception.ServiceException;
import com.ems.service.PayrollService;
import com.ems.wrappers.FileUploadWrapper;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

	@Autowired
	private PayrollService payrollService;

	/**
	 * Upload payroll Excel File. Service url: /payroll/salary/upload method:
	 * POST
	 *
	 * @param uploadWrapper
	 *            FileUploadWrapper bean containing the file to be uploaded
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/pay/upload", method = RequestMethod.POST)
	void changeAvatar(@RequestParam("month") Integer month, @RequestParam("year") Integer year,
			FileUploadWrapper uploadWrapper) throws ServiceException {
		payrollService.processPaySlipSheet(uploadWrapper, month, year);
	}

	/**
	 * Pay Slip history. Service url: /payroll/salary/slips method: GET
	 *
	 * @param id
	 *            Employee ID
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/pay/slips", method = RequestMethod.GET)
	List<PaySlip> getPaySlipsList(@RequestParam(value = "id", required = false) Long id) throws ServiceException {
		return payrollService.getPaySlipsList(id);
	}

	/**
	 * Generate Pay Slip from Payroll. Service url: /payroll/pay-slips/generate
	 * method: GET
	 *
	 * @param id
	 *            Employee ID
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/pay-slips/generate", method = RequestMethod.GET)
	void generatePaySlip(@RequestParam(value = "month", required = true) Integer month,
			@RequestParam(value = "year", required = true) Integer year) throws ServiceException {
		payrollService.generatePaySlip(month, year);
	}

	/**
	 * Saves the fixed payroll of an employee. Service url: /payroll/fixed
	 * method: PUT
	 *
	 * @param id
	 *            PayrollFixed
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/fixed", method = RequestMethod.PUT)
	long setFixedPayroll(@RequestBody PayrollFixed payrollFixed) throws ServiceException {
		return payrollService.setFixedPayroll(payrollFixed);
	}

	/**
	 * Saves the variable payroll of an employee. Service url: /payroll/variable
	 * method: PUT
	 *
	 * @param id
	 *            PayrollFixed
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/variable", method = RequestMethod.PUT)
	long setVariablePayroll(@RequestBody PayrollVariable payrollVariable) throws ServiceException {
		return payrollService.setVariablePayroll(payrollVariable);
	}

	/**
	 * Gets the fixed payroll of an employee. Service url: /payroll/list/fixed
	 * method: GET
	 *
	 * @param id
	 *            PayrollFixed
	 * @param Employee
	 *            Id
	 * 
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/list/fixed", method = RequestMethod.GET)
	Object getFixedPayroll(@RequestParam(value = "emp", required = false) Long empId,
			@RequestParam(value = "id", required = false) Long id) throws ServiceException {
		return payrollService.getFixedPayroll(empId, id);
	}

	/**
	 * Gets the fixed payroll of an employee. Service url:
	 * /payroll/fixed/current method: GET
	 *
	 * @param Employee
	 *            ID
	 * 
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/fixed/current", method = RequestMethod.GET)
	List<PayrollFixed> getCurrentFixedPayroll(@RequestParam(value = "emp", required = false) Long empId)
			throws ServiceException {
		return payrollService.getCurrentFixedPayroll(empId);
	}

	/**
	 * Gets the variable payroll of an employee. Service url:
	 * /payroll/list/varaible method: GET
	 *
	 * @param id
	 *            PayrollVariable
	 * @param Employee
	 *            Id
	 * 
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/list/variable", method = RequestMethod.GET)
	Object getVariablePayrollList(@RequestParam(value = "emp", required = false) Long empId,
			@RequestParam(value = "id", required = false) Long id) throws ServiceException {
		return payrollService.getVariablePayrollList(empId, id);
	}

	/**
	 * Gets the fixed payroll of an employee. Service url: /payroll/variable/get
	 * method: GET
	 *
	 * @param Employee
	 *            ID
	 * 
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/variable/get", method = RequestMethod.GET)
	PayrollVariable getVariblePayroll(@RequestParam(value = "emp", required = false) Long empId,
			@RequestParam(value = "month", required = true) Integer month,
			@RequestParam(value = "year", required = true) Integer year) throws ServiceException {
		return payrollService.getVariblePayroll(empId, month, year);
	}

}