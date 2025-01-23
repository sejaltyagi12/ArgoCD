package com.ems.servicefinder.pdfHelper;

import com.itextpdf.text.Image;

public class CImage {

	private String imagePath = "";
	private  int maxHeight = 0;
	
	public CImage(String imagePath, int maxHeight) {
		this.imagePath = imagePath;
		this.maxHeight = maxHeight;
	}

	public Image Get() throws Exception {
		
		Image image = Image.getInstance(imagePath);
		float scalePercentage = image.getHeight()/maxHeight;
		image.scalePercent(scalePercentage);
		return image;
	}

}
