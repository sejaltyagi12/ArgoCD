package com.ems.servicefinder.pdfHelper;

import java.awt.Color;
import java.util.ArrayList;

public class CTableCell {
	public ArrayList<CParaPart> TextParts;
	public Color BackgroundColor;
	public int ColSpan;
	public boolean noBorder;
	public Float bottomBorderThickness = 1.0f;
	public Float topBorderThickness = 1.0f;
	public Float leftBorderThickness = 1.0f;
	public Float rightBorderThickness = 1.0f;
	public CImage image;
	public boolean isImage = false;

	//single text part constructors
	public CTableCell(CParaPart textPart) {
		this(GetTextParts(textPart), new Color(255, 255, 255), 1);
	}

	public CTableCell(CParaPart textPart, Color color) {
		this(GetTextParts(textPart), color, 1);
	}
	

	public CTableCell(CParaPart textPart, int colSpan) {
		this(GetTextParts(textPart), new Color(255, 255, 255), colSpan);
	}
	
	public CTableCell(CParaPart textPart, int colSpan, boolean noBorder) {
		this(GetTextParts(textPart), new Color(255, 255, 255), colSpan);
		this.noBorder = noBorder;
	}

	public CTableCell(CParaPart textPart, Color color, int colSpan) {
		this(GetTextParts(textPart), color, colSpan);
	}
	
	public CTableCell(CParaPart textPart, Color color, int colSpan,  boolean noBorder) {
		this(GetTextParts(textPart), color, colSpan, noBorder);
	}
	
	public CTableCell(CParaPart textPart, Color color, int colSpan, Float bottomBorderThickness, Float topBorderThickness,  Float leftBorderThickness, Float rightBorderThickness) {
		this(GetTextParts(textPart), color, colSpan, bottomBorderThickness, topBorderThickness, leftBorderThickness, rightBorderThickness);
	}

	//array text part constructors
	public CTableCell(ArrayList<CParaPart> textParts) {
		this(textParts, new Color(255, 255, 255), 1);
	}

	public CTableCell(ArrayList<CParaPart> textParts, Color color, int colSpan) {
		this.TextParts = textParts;
		this.BackgroundColor = color;
		this.ColSpan = colSpan;
	}
	
	public CTableCell(ArrayList<CParaPart> textParts, Color color, int colSpan, Float bottomBorderThickness, Float topBorderThickness,  Float leftBorderThickness, Float rightBorderThickness) {
		this.TextParts = textParts;
		this.BackgroundColor = color;
		this.ColSpan = colSpan;
		this.topBorderThickness = topBorderThickness;
		this.bottomBorderThickness = bottomBorderThickness;
		this.leftBorderThickness = leftBorderThickness;
		this.rightBorderThickness = rightBorderThickness;
	}
	
	public CTableCell(ArrayList<CParaPart> textParts, Color color, int colSpan, boolean noBorder) {
		this.TextParts = textParts;
		this.BackgroundColor = color;
		this.ColSpan = colSpan;
		this.noBorder = noBorder;
	}

	public CTableCell(CImage image , int colspan , boolean noBorder) {
		this.image = image;
		this.ColSpan = colspan;
		this.noBorder = noBorder;
		isImage = true;
	}

	//other stuff
	protected static ArrayList<CParaPart> GetTextParts(CParaPart textPart) {
		ArrayList<CParaPart> textParts = new ArrayList<CParaPart>();
		textParts.add(textPart);
		return textParts;
	}
}
