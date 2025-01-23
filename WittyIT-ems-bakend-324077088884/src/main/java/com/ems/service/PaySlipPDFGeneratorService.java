package com.ems.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.ems.configuration.PropConfig;
import com.ems.exception.ServiceException;
import com.ems.servicefinder.utils.ImageReplacedElementFactory;
import com.ems.wrappers.PaySlipWrapper;
import com.lowagie.text.DocumentException;

@Service
public class PaySlipPDFGeneratorService {

	private TemplateEngine templateEngine;
	private HttpServletRequest httpServletRequest;
	private HttpServletResponse httpServletResponse;
	private ServletContext servletContext;
	private PayrollService payrollService;
	private PropConfig propConfig;

	@Autowired
	public PaySlipPDFGeneratorService(TemplateEngine templateEngine, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, ServletContext servletContext, PayrollService payrollService,
			PropConfig propConfig) {
		this.templateEngine = templateEngine;
		this.httpServletRequest = httpServletRequest;
		this.httpServletResponse = httpServletResponse;
		this.servletContext = servletContext;
		this.payrollService = payrollService;
		this.propConfig = propConfig;
	}

	public byte[] pdfGenerate(Long paySlipId) throws FileNotFoundException, DocumentException, ServiceException {
		PaySlipWrapper paySlipWrapper = payrollService.getEmployeePaySlip(paySlipId);
		WebContext webContext = new WebContext(httpServletRequest, httpServletResponse, servletContext);
		webContext.setVariable("paySlip", paySlipWrapper);
		// webContext.setVariable("url", propConfig.getApplicationUrl() +
		// "logo/wittybrain.jpg");
		String htmlContent = templateEngine.process("payslip.html", webContext);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();
		ImageReplacedElementFactory imageReplacedElementFactory = new ImageReplacedElementFactory(
				renderer.getSharedContext().getReplacedElementFactory());
		imageReplacedElementFactory.setImageFile(
				new File(getClass().getClassLoader().getResource("static/images/wittybrain.png").getFile()));
		renderer.getSharedContext().setReplacedElementFactory(imageReplacedElementFactory);
		renderer.setDocumentFromString(htmlContent);
		renderer.layout();
		try {
			renderer.createPDF(os, false);
		} catch (DocumentException e) {
			// logger.error("Unable to create PDF.");
			throw new ServiceException("Unable to create PDF.");
		} finally {
			renderer.finishPDF();
		}
		/* return Base64.getEncoder().encode(os.toByteArray()); */
		return os.toByteArray();
	}

	public String createNewPdf(Long paySlipId) throws Exception {
		PaySlipWrapper paySlipWrapper = payrollService.getEmployeePaySlip(paySlipId);
		WebContext webContext = new WebContext(httpServletRequest, httpServletResponse, servletContext);
		webContext.setVariable("paySlip", paySlipWrapper);
		webContext.setVariable("url", propConfig.getApplicationUrl() + "logo/wittybrain.jpg");
		String htmlContent = templateEngine.process("payshlip.html", webContext);

		FileOutputStream os = null;
		try {
			final File outputFile = new File("D:/test3.pdf");
			os = new FileOutputStream(outputFile);

			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(htmlContent);
			renderer.layout();
			renderer.createPDF(os, false);
			renderer.finishPDF();
			System.out.println("PDF created successfully");
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					/* ignore */ }
			}
		}
		return "SuccessFull";
	}
}
