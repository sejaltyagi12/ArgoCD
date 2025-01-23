package com.ems.servicefinder.excelHelper;

import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.ems.servicefinder.utils.EmsMathUtils;

public class CExcelTable {

	public int numberOfColumns;
	public int[] columnsWidth;
	public int rowOffset = 0;
	public int columnOffset = 0;
	private ArrayList<CExcelTableCell> cells = new ArrayList<CExcelTableCell>();
	private Workbook workbook;
	private Sheet sheet;
	private final int NORMAL_WIDTH_MULTIPLIER = 10;

	protected ArrayList<Font> fonts = new ArrayList<Font>();
	protected ArrayList<CellStyle> cellStyles = new ArrayList<CellStyle>();

	public CExcelTable(Workbook workbook, Sheet sheet , int numberOfColumns, int[] columnsWidth) {
		this.workbook = workbook;
		this.sheet = sheet;
		this.numberOfColumns = numberOfColumns;
		this.columnsWidth = columnsWidth;
	}

	public void AddCell(String text) {
		cells.add(new CExcelTableCell(text));
	}

	public void AddCell(CExcelTableCell cell) {
		cells.add(cell);
	}

	protected CellStyle GetCellStyle(CExcelTableCell newCell) {
		CellStyle theCellStyle = null;
		for (CellStyle cs : cellStyles) {
			if (cs.getAlignment() != newCell.halign)
				continue;
			if (cs.getVerticalAlignment() != newCell.valign)
				continue;
			if (newCell.backgroundColorIndex == 0 && cs.getFillPattern() != HSSFCellStyle.NO_FILL)
				continue;
			if (newCell.backgroundColorIndex != 0 && (cs.getFillPattern() != HSSFCellStyle.SOLID_FOREGROUND || cs.getFillForegroundColor() != newCell.backgroundColorIndex))
				continue;
			if (workbook.getFontAt(cs.getFontIndex()).getBoldweight() != newCell.boldWeight)
				continue;

			theCellStyle = cs;
		}

		if (theCellStyle == null) {
			theCellStyle = GetNewCellStyle(newCell);
			Font theFont = GetNewFont(newCell);
			theCellStyle.setFont(theFont);
			this.cellStyles.add(theCellStyle);
		}
		
		theCellStyle.setBorderBottom(newCell.border);
		theCellStyle.setBorderTop(newCell.border);
		theCellStyle.setBorderLeft(newCell.border);
		theCellStyle.setBorderRight(newCell.border);

		return theCellStyle;
	}

	protected CellStyle GetNewCellStyle(CExcelTableCell newCell) {
		CellStyle newCellStyle = workbook.createCellStyle();
		newCellStyle.setAlignment(newCell.halign);
		newCellStyle.setVerticalAlignment(newCell.valign);
		if (newCell.backgroundColorIndex == 0) {
			newCellStyle.setFillPattern(HSSFCellStyle.NO_FILL);
		} else {
			newCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			newCellStyle.setFillForegroundColor(newCell.backgroundColorIndex);
		}
		return newCellStyle;
	}

	protected Font GetNewFont(CExcelTableCell newCell) {
		Font newFont = workbook.createFont();
		newFont.setBoldweight(newCell.boldWeight);
		newFont.setColor(newCell.fontColor);
		return newFont;
	}

	public void CreateTable() {
		setColumnsWidth();
		int rowIndex = rowOffset;
		int columnIndex = columnOffset;
		Row row = null;

		for (CExcelTableCell cellObj : cells) {
			CellStyle cellStyle = GetCellStyle(cellObj);

			if (columnIndex % numberOfColumns == 0) {
				columnIndex = 0;
				row = sheet.createRow(rowIndex++);
			}

			Cell cell = null;

			int startColIndex = columnIndex;
			for (int j = 0; j < cellObj.colSpan; j++) {

				cell = row.createCell((short) columnIndex++);
				cell.setCellStyle(cellStyle);
				
				try 
				{
					double f = Double.valueOf(cellObj.text);
					cell.setCellValue(EmsMathUtils.removeTrailingZeros(f));
				} 
				catch (Exception e) 
				{
					cell.setCellValue(cellObj.text);
				}
			}
			if (cellObj.colSpan > 1) {
				sheet.addMergedRegion(new CellRangeAddress(rowIndex - 1, rowIndex - 1, startColIndex, columnIndex - 1));
			}
		}
	}

	private void setColumnsWidth() {
		for (int i = 0; i < columnsWidth.length; i++) {
			sheet.setColumnWidth(i, columnsWidth[i] * 255 * NORMAL_WIDTH_MULTIPLIER);
		}
	}
}