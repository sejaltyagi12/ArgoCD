package com.ems.controller;

import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ems.exception.ServiceException;
import com.ems.service.PayrollService;
import com.ems.service.PaySlipPDFGeneratorService;
import com.ems.wrappers.PaySlipWrapper;
import com.lowagie.text.DocumentException;

@RestController
@RequestMapping("/payslip")
public class PaySlipController {
	@Autowired
	private PaySlipPDFGeneratorService paySlipPDFGeneratorService;

	@Autowired
	private PayrollService payrollService;

	/**
	 * Get byte array for latest payslip pdf. url: payslip/pdf method: GET
	 * 
	 * @param paySlipId
	 * @return byte array for latest payslip pdf.
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/pdf", method = RequestMethod.GET)
	byte[] getCurrentPaySlipPDF(@RequestParam("id") Long paySlipId)
			throws FileNotFoundException, DocumentException, ServiceException {
		return paySlipPDFGeneratorService.pdfGenerate(paySlipId);
	}

	/**
	 * Get PaySlipWrapper for latest payslip. url: payslip/current method: GET
	 * 
	 * @return Get PaySlipWrapper for latest payslip.
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	PaySlipWrapper getPaySlip() throws ServiceException {
		return payrollService.getLatestPaySlip();
	}
}
