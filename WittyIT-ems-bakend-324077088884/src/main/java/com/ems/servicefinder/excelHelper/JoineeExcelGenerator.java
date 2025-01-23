package com.ems.servicefinder.excelHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.ems.wrappers.ExcelReportWrapper;


public class JoineeExcelGenerator {
	
	DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
	
	 public void buildExcelDocument(Map<String, Object> model,
             Workbook workbook,
             HttpServletRequest request,
             HttpServletResponse response) throws Exception 
	 {
		 
		 @SuppressWarnings("unchecked")
		List<ExcelReportWrapper> employeeList = (List<ExcelReportWrapper>) model.get("employeeList");
		 			 
		 	Sheet sheet = workbook.createSheet("Joinee Details");
			CExcelTable table = new CExcelTable(workbook, sheet, 8, new int[] { 2, 2, 2, 2, 2, 2, 2, 2});

			table.AddCell(new CExcelTableCell("S.No", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
			table.AddCell(new CExcelTableCell("Employee Code", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
			table.AddCell(new CExcelTableCell("Employee Name", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
			
			table.AddCell(new CExcelTableCell("Date of joining", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
			table.AddCell(new CExcelTableCell("Designation", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
			table.AddCell(new CExcelTableCell("Department", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
			table.AddCell(new CExcelTableCell("Manager", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
			table.AddCell(new CExcelTableCell("Manager Code", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
			

			for (ExcelReportWrapper excelReportWrapper : employeeList) 
			{
				table.AddCell(new CExcelTableCell( "" + (employeeList.indexOf(excelReportWrapper) + 1)));
				table.AddCell(new CExcelTableCell(excelReportWrapper.getEmployee().getEmpCode()));
				table.AddCell(new CExcelTableCell(excelReportWrapper.getEmployee().getFullName()));

				table.AddCell(new CExcelTableCell("" + dateFormat.format(excelReportWrapper.getEmployee().getJoiningDate())));
				table.AddCell(new CExcelTableCell(excelReportWrapper.getEmployee().getDesignation().getDesignation()));
				table.AddCell(new CExcelTableCell(excelReportWrapper.getEmployee().getDesignation().getDepartment().getDeptName()));
				
				String manager = excelReportWrapper.getManager() == null ? "" : excelReportWrapper.getManager().getFullName();
				table.AddCell(new CExcelTableCell(manager));
				
				String managerCode = excelReportWrapper.getManager() == null ? "" : excelReportWrapper.getManager().getEmpCode();
				table.AddCell(new CExcelTableCell(managerCode));
			}

			table.CreateTable();
	 }

}
