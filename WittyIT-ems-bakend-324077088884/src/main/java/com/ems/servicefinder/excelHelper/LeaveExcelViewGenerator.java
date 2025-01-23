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
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.ems.domain.CompanyCategory;
import com.ems.domain.LeaveHistory.Status;
import com.ems.wrappers.ExcelReportWrapper;

public class LeaveExcelViewGenerator {

	DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

	public void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		@SuppressWarnings("unchecked")
		List<ExcelReportWrapper> leaveList = (List<ExcelReportWrapper>) model.get("leaveList");
		CompanyCategory companyCategory = (CompanyCategory) model.get("companyCategory");

		Sheet sheet = workbook.createSheet("Leave Details");
		CExcelTable table = new CExcelTable(workbook, sheet, 11, new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });

		table.AddCell(new CExcelTableCell("S.No", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
		table.AddCell(new CExcelTableCell("Employee Code", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
		table.AddCell(new CExcelTableCell("Employee Name", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));

		table.AddCell(new CExcelTableCell("Applied Date", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
		table.AddCell(new CExcelTableCell("Start Date", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
		table.AddCell(new CExcelTableCell("End Date", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));

		table.AddCell(new CExcelTableCell("Number of Days", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
		table.AddCell(new CExcelTableCell("Deduction Details", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
		table.AddCell(new CExcelTableCell("Type of Leave", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
		table.AddCell(new CExcelTableCell("Status", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));
		table.AddCell(new CExcelTableCell("Leave Status Date", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP));

		for (ExcelReportWrapper excelReportWrapper : leaveList) {
			table.AddCell(new CExcelTableCell("" + (leaveList.indexOf(excelReportWrapper) + 1)));
			table.AddCell(new CExcelTableCell(excelReportWrapper.getEmployee().getEmpCode()));
			table.AddCell(new CExcelTableCell(excelReportWrapper.getEmployee().getFullName()));

			table.AddCell(new CExcelTableCell("" + dateFormat.format(excelReportWrapper.getLeave().getAppliedDate())));
			table.AddCell(new CExcelTableCell("" + dateFormat.format(excelReportWrapper.getLeave().getFromDate())));
			table.AddCell(new CExcelTableCell("" + dateFormat.format(excelReportWrapper.getLeave().getToDate())));

			table.AddCell(new CExcelTableCell("" + excelReportWrapper.getLeave().getDayCount()));

			// Setting Deduction Details.
			StringBuffer deductionDetails = new StringBuffer("");

			if (excelReportWrapper.getLeave().getLeaveHistoryDetails() != null && !excelReportWrapper.getLeave().getLeaveHistoryDetails().isEmpty()) {

				// For casual leave
				if (excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedCasualLeave() != 0.0f
						|| excelReportWrapper.getLeave().getLeaveType().getTypeId().intValue() == 1) {
					deductionDetails
							.append(excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedCasualLeave() + "CL + ");
				}

				// For Sick leave
				if (excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedSickLeave() != 0.0f
						|| excelReportWrapper.getLeave().getLeaveType().getTypeId().intValue() == 2) {
					deductionDetails.append(excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedSickLeave() + "SL + ");
				}

				// For Privilege leave
				if (excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave() != 0.0f
						|| excelReportWrapper.getLeave().getLeaveType().getTypeId().intValue() == 4) {
					deductionDetails
							.append(excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave() + "PL + ");
				}

				// // For Civil duty leave
				// if
				// ((leaveHistory.getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave()
				// != 0.0f
				// ||
				// leaveHistory.getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay()
				// != 0.0f)
				// && leaveHistory.getLeaveType().getTypeId().intValue() == 3) {
				// deductionDetails.append(employee.getCompanyCategory().getTotalCivilDuityLeave()
				// + "CDL + ");
				// }

				// For Civil duty leave
				if (excelReportWrapper.getLeave().getLeaveType().getTypeId().intValue() == 3) {
					deductionDetails.append(companyCategory.getTotalCivilDuityLeave() + "CDL + ");
				}

				// For Marriage leave
				if (excelReportWrapper.getLeave().getLeaveType().getTypeId().intValue() == 5) {

					if ((excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave() != 0.0f
							|| excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay() != 0.0f)) {
						deductionDetails.append(companyCategory.getTotalMarriageLeave() + "ML + ");
					}
				}

				// For Maternity leave
				if (excelReportWrapper.getLeave().getLeaveType().getTypeId().intValue() == 6) {

					if ((excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave() != 0.0f
							|| excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay() != 0.0f)) {
						deductionDetails.append(companyCategory.getTotalMaternityLeave() + "MTL + ");
					}
				}

				// For Paternity leave
				if (excelReportWrapper.getLeave().getLeaveType().getTypeId().intValue() == 7) {

					if ((excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedPrivilegeLeave() != 0.0f
							|| excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay() != 0.0f)) {
						deductionDetails.append(companyCategory.getTotalPaternityLeave() + "PTL + ");
					}
				}

				// For Leave without pay leave
				if (excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay() != 0.0f) {
					deductionDetails
							.append(excelReportWrapper.getLeave().getLeaveHistoryDetails().get(0).getDeductedLeaveWithOutPay() + "LWP");
				} else {
					// Incase no LWP removing extra " + ".
					deductionDetails.setLength(deductionDetails.length() - 3);
				}
			}
			if (deductionDetails.length() == 0) {

				// For Civil duty leave
				if (excelReportWrapper.getLeave().getLeaveType().getTypeId().intValue() == 3) {
					deductionDetails.append(companyCategory.getTotalCivilDuityLeave() + "CDL");
				}

				if (excelReportWrapper.getLeave().getLeaveType().getTypeId().intValue() == 5) {
					// Incase less then total ML
					deductionDetails.append(
							(Days.daysBetween(excelReportWrapper.getLeave().fromDate(), excelReportWrapper.getLeave().toDate()).getDays() + 1) + "ML");
				}
				if (excelReportWrapper.getLeave().getLeaveType().getTypeId().intValue() == 6) {
					// Incase less then total Maternity Leave
					deductionDetails.append(
							(Days.daysBetween(excelReportWrapper.getLeave().fromDate(), excelReportWrapper.getLeave().toDate()).getDays() + 1) + "MTL");
				}
				if (excelReportWrapper.getLeave().getLeaveType().getTypeId().intValue() == 7) {
					// Incase less then total Paternity Leave
					deductionDetails.append(
							(Days.daysBetween(excelReportWrapper.getLeave().fromDate(), excelReportWrapper.getLeave().toDate()).getDays() + 1) + "PTL");
				}
			}
			table.AddCell(new CExcelTableCell(deductionDetails.toString()));
			table.AddCell(new CExcelTableCell("" + excelReportWrapper.getLeave().getLeaveType().getType()));
			table.AddCell(new CExcelTableCell(excelReportWrapper.getLeave().getStatus().getStringValue()));
			DateTime leaveStatusDate = null;
			if(excelReportWrapper.getLeave().getApprovedDate() != null) {
				leaveStatusDate = new DateTime(excelReportWrapper.getLeave().getApprovedDate());
			}
			
			if (excelReportWrapper.getLeave().getStatus().getStringValue().equalsIgnoreCase(Status.REJECTED.getStringValue()) && excelReportWrapper.getLeave().getRejectedDate() != null) {
				leaveStatusDate = new DateTime(excelReportWrapper.getLeave().getRejectedDate().toDate());
			}

			if (excelReportWrapper.getLeave().getStatus().getStringValue().equalsIgnoreCase(Status.WITHDRAWN.getStringValue()) && excelReportWrapper.getLeave().getWithdrawnDate() != null) {
				leaveStatusDate = new DateTime(excelReportWrapper.getLeave().getWithdrawnDate().toDate());
			}
			table.AddCell(new CExcelTableCell((leaveStatusDate != null ? leaveStatusDate.toString("MMM dd, yyyy") : null)));
		}

//		For note only
		table.AddCell(new CExcelTableCell("*Note:	Casual Leave(CL), Sick Leave(SL), Privilege Leave(PL), Marriage Leave(ML), Paternity Leave(PTL), Maternity Leave(MTL), Civil Duty Leave(CDL)", Font.BOLDWEIGHT_BOLD, HSSFColor.RED.index, 11,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_NONE, HSSFColor.WHITE.index));
		
		table.CreateTable();
	}

}
