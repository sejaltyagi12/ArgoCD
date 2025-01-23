package com.ems.servicefinder.excelHelper;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class CExcelTableCell {
	public String text = "";
	public int fontSize = 10;
	public short boldWeight = Font.BOLDWEIGHT_NORMAL;
	public short backgroundColorIndex;
	public short halign = CellStyle.ALIGN_LEFT;
	public short valign = CellStyle.VERTICAL_TOP;
	public short border = CellStyle.BORDER_NONE;
	public int colSpan = 1;
	public short fontColor = HSSFColor.BLACK.index;

	public CExcelTableCell(String text) {
		this.text = text;
	}

	public CExcelTableCell(String text, short boldWeight) {
		this.text = text;
		this.boldWeight = boldWeight;
	}

	public CExcelTableCell(String text, short boldWeight, short backgroundColorIndex) {
		this.text = text;
		this.boldWeight = boldWeight;
		this.backgroundColorIndex = (short) backgroundColorIndex;
	}

	public CExcelTableCell(String text, short boldWeight, short backgroundColorIndex, int colSpan) {
		this.text = text;
		this.boldWeight = boldWeight;
		this.backgroundColorIndex = (short) backgroundColorIndex;
		this.colSpan = colSpan;
	}

	public CExcelTableCell(String text, short boldWeight, short backgroundColorIndex, int colSpan, short halign, short valign) {
		this.text = text;
		this.boldWeight = boldWeight;
		this.backgroundColorIndex = (short) backgroundColorIndex;
		this.colSpan = colSpan;
		this.halign = halign;
		this.valign = valign;
	}
	
	public CExcelTableCell(String text, short boldWeight, short backgroundColorIndex, int colSpan, short halign, short valign, short border ) {
		this.text = text;
		this.boldWeight = boldWeight;
		this.backgroundColorIndex = (short) backgroundColorIndex;
		this.colSpan = colSpan;
		this.halign = halign;
		this.valign = valign;
		this.border = border;
	}
	
	public CExcelTableCell(String text, short boldWeight, short backgroundColorIndex, int colSpan, short halign, short valign, short border , short fontColor) {
		this.text = text;
		this.boldWeight = boldWeight;
		this.backgroundColorIndex = (short) backgroundColorIndex;
		this.colSpan = colSpan;
		this.halign = halign;
		this.valign = valign;
		this.border = border;
		this.fontColor = fontColor;
	}
}
