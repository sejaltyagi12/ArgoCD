package com.ems.servicefinder.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

public class ImageReplacedElementFactory implements ReplacedElementFactory {

	private ReplacedElementFactory superFactory;
	private File file;
	
	public ImageReplacedElementFactory(ReplacedElementFactory superFactory) {
        this.superFactory = superFactory;
    }

	public void setImageFile(File file) {
		this.file = file;
	}
	@Override
	public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth,
			int cssHeight) {
		Element element = box.getElement();
		if(element == null) {
			return null;
		}
		String nodeName = element.getNodeName();
		String className = element.getAttribute("class");
		 if ("div".equals(nodeName) && className.contains("img")) {
			 try {
				byte[] bytes = FileUtils.readFileToByteArray(file);
				 Image image = Image.getInstance(bytes);
				 FSImage fsImage = new ITextFSImage(image);
				 if (fsImage != null) {
				     if ((cssWidth != -1) || (cssHeight != -1)) {
				         fsImage.scale(cssWidth, cssHeight);
				     }
				     return new ITextImageElement(fsImage);
				 }
			} catch (BadElementException | IOException e) {
				e.printStackTrace();
			}
		 }
		 return superFactory.createReplacedElement(c, box, uac, cssWidth, cssHeight);
	}

	@Override
	public void reset() {
		superFactory.reset();

	}

	@Override
	public void remove(Element e) {
		superFactory.remove(e);

	}

	@Override
	public void setFormSubmissionListener(FormSubmissionListener listener) {
		superFactory.setFormSubmissionListener(listener);

	}

}

