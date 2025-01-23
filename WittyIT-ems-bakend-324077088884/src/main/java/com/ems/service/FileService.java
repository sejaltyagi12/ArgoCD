package com.ems.service;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.configuration.PropConfig;
import com.ems.servicefinder.utils.UserUtils;

@Service
public class FileService {

	@Autowired
	private PropConfig propConfig;

	/**
	 * Stores an Asset.
	 * 
	 */

	public String storeAsset(Long id, byte[] fileContent, String fileName, String category) throws Exception {

		fileName = UserUtils.validateSpecialCharacters(fileName);
		String assetBasePathPath = propConfig.getAssetBasePath() + File.separator;
		String contextPath = category + File.separator + id + File.separator;
		String path = assetBasePathPath + contextPath;

		String absolutePath = path + fileName;

		try {
			File dirFile = new java.io.File(path);
			if (!dirFile.exists()) {
				if (!dirFile.mkdirs())
					throw new Exception(
							"The File: " + fileName + ", could not be uploaded, because no upload dir exists.");
			}

			FileUtils.writeByteArrayToFile(new java.io.File(absolutePath), fileContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contextPath + fileName;
	}

	/**
	 * Gets the Asset complete url.
	 * 
	 */
	public String getAssetCompleteUrl(String absUrl) {
		if (absUrl != null)
			return propConfig.getApplicationUrl()+absUrl;

		return null;
	}

}
