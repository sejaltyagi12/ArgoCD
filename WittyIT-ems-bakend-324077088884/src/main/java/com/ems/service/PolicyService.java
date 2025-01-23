package com.ems.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.configuration.PropConfig;
import com.ems.exception.ServiceException;

@Service
public class PolicyService {

	@Autowired
	private PropConfig propConfig;

	public Map<String, String> getAll() {
		Map<String, String> policyPdfMap = new HashMap<>();
		File policyFolder = new File(propConfig.getAssetBasePath() + "policy");
		for (String string : policyFolder.list()) {
			policyPdfMap.put(string.split("\\.")[0], propConfig.getApplicationUrl() + propConfig.getBaseContext() + "policy/" + string);
		}
		return policyPdfMap;
	}

	
	/**
	 *  Will get the policy pdf url with the name given. If name is not given, it will get the url of first file in the folder.
	 * @param name
	 * @return
	 * @throws ServiceException
	 */
	public String get(String name) throws ServiceException {
		File policyFolder = new File(propConfig.getAssetBasePath() + File.separator + "policy");
		if( name != null && !name.isEmpty()) {
		for (String string : policyFolder.list()) {
			if(string.split("\\.")[0].equals(name))
				return propConfig.getApplicationUrl() + propConfig.getBaseContext() + "policy/" + string;
		}
		throw new ServiceException("Can't find the policy pdf named :" + name);
		}else {
			return propConfig.getApplicationUrl() + propConfig.getBaseContext() + "policy/" + policyFolder.list()[0];
		}
	}

}
