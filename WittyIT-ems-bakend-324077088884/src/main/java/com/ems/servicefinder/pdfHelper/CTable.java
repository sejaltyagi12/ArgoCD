package com.ems.servicefinder.pdfHelper;

import java.awt.Color;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class CTable {
	private float TableWidthPercent;
	private int Columns;
	private int[] ColumnWidths;
	private int CellPadding;
	private ArrayList<CTableCell> Cells;

	public CTable(float tableWidthPercent, int columns, int[] columnWidths) {
		this.TableWidthPercent = tableWidthPercent;
		this.Columns = columns;
		this.ColumnWidths = columnWidths;
		this.Cells = new ArrayList<CTableCell>();
		this.CellPadding = 5;
	}

	public CTable(float tableWidthPercent, int columns, int[] columnWidths, int cellPadding) {
		this.TableWidthPercent = tableWidthPercent;
		this.Columns = columns;
		this.ColumnWidths = columnWidths;
		this.Cells = new ArrayList<CTableCell>();
		this.CellPadding = cellPadding;
	}
	
	public void AddCell(CImage image, int colSpan, boolean noBorder) {
		this.Cells.add(new CTableCell(image,colSpan ,noBorder));
	}

	public void AddCell(CParaPart part) {
		this.Cells.add(new CTableCell(part));
	}

	public void AddCell(CParaPart part, Color color) {
		this.Cells.add(new CTableCell(part, color));
	}

	public void AddCell(CParaPart part, int colSpan) {
		this.Cells.add(new CTableCell(part, colSpan));
	}

	public void AddCell(CParaPart part, int colSpan, boolean noBorder) {
		this.Cells.add(new CTableCell(part, colSpan,noBorder));
	}
	
	public void AddCell(CParaPart part, Color color, int colSpan) {
		this.Cells.add(new CTableCell(part, color, colSpan));
	}
	
	public void AddCell(CParaPart part, Color color, int colSpan, boolean noBorder) {
		this.Cells.add(new CTableCell(part, color, colSpan, noBorder));
	}
	
	public void AddCell(CParaPart part, Color color, int colSpan, Float bottomBorderThickness, Float topBorderThickness, Float leftBorderThickness, Float rightBorderThickness) {
		this.Cells.add(new CTableCell(part, color, colSpan, bottomBorderThickness, topBorderThickness, leftBorderThickness, rightBorderThickness));
	}

	public PdfPTable GetTable() {
		PdfPTable table = new PdfPTable(this.Columns);
		table.setWidthPercentage(this.TableWidthPercent);
		try {
			table.setWidths(this.ColumnWidths);
		} catch (DocumentException e) {
			//what the heck is this exception even?
		}

		for (CTableCell c : Cells) {
			PdfPCell cell = new PdfPCell();
			cell.setVerticalAlignment(Element.ALIGN_TOP);
			if(!c.isImage){
				
				cell.addElement(CParagraph.Get(c.TextParts));
			}else{
				try {
					cell.addElement(c.image.Get());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			cell.setUseAscender(true);
			cell.setPadding(this.CellPadding);
			//cell.setBackgroundColor(c.BackgroundColor);
			cell.setColspan(c.ColSpan);
			
			cell.setBorderWidthBottom(c.bottomBorderThickness);
			cell.setBorderWidthTop(c.topBorderThickness);
			cell.setBorderWidthLeft(c.leftBorderThickness);
			cell.setBorderWidthRight(c.rightBorderThickness);

			if(c.bottomBorderThickness < 1)
				cell.setBorderColorBottom(BaseColor.LIGHT_GRAY);
			
			if(c.topBorderThickness < 1)
				cell.setBorderColorTop(BaseColor.LIGHT_GRAY);
			
			if(c.leftBorderThickness < 1)
				cell.setBorderColorLeft(BaseColor.LIGHT_GRAY);
			
			if(c.rightBorderThickness < 1)
				cell.setBorderColorRight(BaseColor.LIGHT_GRAY);
			
			if(c.noBorder)
			{
				cell.setBorder(Rectangle.NO_BORDER);
			}
			table.addCell(cell);
		}

		return table;
	}

}
