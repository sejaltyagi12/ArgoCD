package com.ems.servicefinder.pdfHelper;

import com.itextpdf.text.Font;
import com.itextpdf.text.Element;

public class CParaPart {
	public String Text;
	public com.itextpdf.text.Font Font;
	public int HorizontalAlign;
	public float Indent;
	public float Leading;

	public CParaPart(String text, Font font, float leading) {
		this.Text = text;
		this.Font = font;
		this.Leading = leading;
		this.HorizontalAlign = Element.ALIGN_LEFT;
		this.Indent = 0;
	}

	public CParaPart(String text, Font font, float leading, int horizontalAlignment, float indent) {
		this.Text = text;
		this.Font = font;
		this.Leading = leading;
		this.HorizontalAlign = horizontalAlignment;
		this.Indent = indent;
	}
}
