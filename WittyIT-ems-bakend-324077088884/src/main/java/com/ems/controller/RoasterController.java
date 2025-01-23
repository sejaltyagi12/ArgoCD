package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.Roaster;
import com.ems.exception.ServiceException;
import com.ems.service.RoasterService;
import com.ems.wrappers.FileUploadWrapper;

@RestController
@RequestMapping("/roaster")
public class RoasterController {
	
	@Autowired
	private RoasterService roasterService;
	
	/**
	 * Upload Roaster Excel File 
	 * Service url: 
	 * /roaster/upload method: POST
	 *
	 * @param uploadWrapper
	 *            FileUploadWrapper bean containing the file to be uploaded
	 * @throws ServiceException
	 *             the service exception
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	void changeAvatar(FileUploadWrapper uploadWrapper)
			throws ServiceException {
		 roasterService.processRoasterSheet(uploadWrapper);
	}
	
	/**
	 * Get list of roasters in a month
	 * Service url:  /roaster/get method: GET 
	 *
	 * @return List of Roasters
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	List<Roaster> getRoasters(@RequestParam(value = "id", required = false) Long id) throws ServiceException 
	{
		return roasterService.getRoasterList(id);
	}

}
