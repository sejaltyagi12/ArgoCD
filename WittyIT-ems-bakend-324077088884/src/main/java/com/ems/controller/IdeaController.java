package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ems.domain.Idea;
import com.ems.exception.ServiceException;
import com.ems.service.IdeaService;

@RestController
@RequestMapping("/idea")
public class IdeaController {
	
	@Autowired
	private IdeaService ideaService;	
	
	/**
	 * Adds Idea and Suggestions
	 * Service url:  /idea/apply method: PUT 
	 *
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/post", method = RequestMethod.PUT)
	void addIdea(@RequestBody Idea idea) throws ServiceException 
	{
		ideaService.addIdea(idea);
	}
	
	/**
	 * Gets Idea and Suggestions posted by employee
	 * Service url:  /idea/get method: GET 
	 *
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	List<Idea> getIdeas() throws ServiceException 
	{
		return ideaService.getIdeas();
	}
	
	/**
	 * Gets Idea and Suggestions posted by employee
	 * Service url:  /idea/manager/list method: GET 
	 *
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/team/list", method = RequestMethod.GET)
	List<Idea> getTeamPostedIdeas() throws ServiceException 
	{
		return ideaService.getManagerIncludedIdeas();
	}
	
	
	/**
	 * Gets Idea and Suggestions posted by employee
	 * Service url:  /idea/get method: GET 
	 *
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	Idea getIdea(@RequestParam("id") Long id) throws ServiceException 
	{
		return ideaService.findById(id);
	}
	
	
	/**
	 * Gets Idea and Suggestions posted by employee
	 * Service url:  /idea/manager/list method: PUT 
	 *
	 * @throws ServiceException the service exception
	 */
	@RequestMapping(value = "/status", method = RequestMethod.PUT)
	void setIdeaReadStatus(@RequestParam("id") Long id, @RequestParam("read") Boolean isRead ) throws ServiceException 
	{
		ideaService.setIdeaReadStatus(id, isRead);
	}

}
