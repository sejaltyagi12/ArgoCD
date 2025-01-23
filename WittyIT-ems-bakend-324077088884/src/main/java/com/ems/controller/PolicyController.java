package com.ems.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.exception.ServiceException;
import com.ems.service.PolicyService;

@RestController
@RequestMapping("/policy")
public class PolicyController {

	@Autowired
	private PolicyService policyService;

	@RequestMapping(value = "/pdf", method = RequestMethod.GET)
	byte[] getPdf() throws IOException {
		Path pdfPath = Paths.get("E:/new EMS/ems/src/main/resources/static/policies/policy.pdf");
		byte[] pdf = Files.readAllBytes(pdfPath);

		return Base64.getEncoder().encode(pdf);
		// return pdf;
	}

	@RequestMapping(value = "/html", method = RequestMethod.GET)
	byte[] getHtml() throws IOException {
		Path pdfPath = Paths.get("E:/new EMS/ems/src/main/resources/static/policies/ht.html");
		byte[] pdf = Files.readAllBytes(pdfPath);

		return Base64.getEncoder().encode(pdf);
	}

	/**
	 * Getting all policy pdf url as a Map<FileName,Url>
	 * 
	 * @author Sarit Mukherjee
	 * @return Map<FileName,Url>
	 */
	@GetMapping(value = "/getAll")
	Map<String, String> getAll() {
		return policyService.getAll();

	}

	/**
	 * Getting a pdf url by it's name. If name not given then it will give the first file in the folder.
	 * @param name
	 * @return url of the file
	 * @throws ServiceException
	 */
	@GetMapping(value = "/get")
	String get(@RequestParam(value = "name", required = false) String name) throws ServiceException {
		return policyService.get(name);

	}

}
