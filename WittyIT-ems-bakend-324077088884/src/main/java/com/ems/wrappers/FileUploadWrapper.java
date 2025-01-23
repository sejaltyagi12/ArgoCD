package com.ems.wrappers;


import org.springframework.web.multipart.MultipartFile;

public class FileUploadWrapper {
	
	private MultipartFile fileContent;
	
	// private Integer month;
	//
	// private Integer year;
	//
	// public Integer getMonth() {
	// return month;
	// }
	//
	// public void setMonth(Integer month) {
	// this.month = month;
	// }
	//
	// public Integer getYear() {
	// return year;
	// }
	//
	// public void setYear(Integer year) {
	// this.year = year;
	// }

	public MultipartFile getFileContent() {
		return fileContent;
	}

	public void setFileContent(MultipartFile fileContent) {
		this.fileContent = fileContent;
	}

}
