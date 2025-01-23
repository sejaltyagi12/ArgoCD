package com.ems.servicefinder.pdfHelper;

import com.ems.domain.Employee;
import com.ems.domain.PaySlip;
import com.ems.servicefinder.utils.NumberToWords;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.awt.Color;
import java.util.Map;

/**
 * Created by Avinash Tyagi on 2017-06-15.
 */
public class PdfView extends AbstractPdfView {
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"");
       
		PaySlip paySlip = (PaySlip) model.get("paySlip");
		
		Employee employee = (Employee) model.get("employee");
		
		String logoPath = (String) model.get("logoPath");
		
		DateTime paySlipDate = new DateTime().withDayOfMonth(1).withMonthOfYear(paySlip.getMonth()).withYear(paySlip.getYear());
		
		writer.setPageEvent(new GenericPdf.GenericHeaderFooter("", "", logoPath));
		
		CTable compNameTable = new CTable(100F, 4, new int[] { 1, 1, 1, 1 });
		compNameTable.AddCell(new CParaPart(employee.getCompanyCategory().getCompanyName(), GenericPdf.Fonts.extraLargeBold, GenericPdf.Fonts.extraLargeSize, Element.ALIGN_CENTER, 0), new Color(200, 200, 200), 4, true);
		document.add(compNameTable.GetTable());
		
		document.add(CParagraph.SpacerParagraph());
		
		CTable monthTable = new CTable(100F, 4, new int[] { 1, 1, 1, 1 });
		monthTable.AddCell(new CParaPart("Payslip for " + DateTimeFormat.forPattern("MMMM yyyy").print(paySlipDate), GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_CENTER, 0), new Color(200, 200, 200), 4, true);
		document.add(monthTable.GetTable());
		
		document.add(CParagraph.SpacerParagraph());
		document.add(CParagraph.SpacerParagraph());
		document.add(CParagraph.SpacerParagraph());

		CTable table = new CTable(100F, 12, new int[] { 1, 1, 1, 1 , 1, 1});
		table.AddCell(new CParaPart("Employee Code: " + employee.getEmpCode(), GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 4, 0.2f, 1.0f, 1f, 1f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 4, 0.2f, 1.0f, 0f, 0f);
		table.AddCell(new CParaPart("Name: " + employee.getFullName(), GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 4, 0.2f, 1.0f, 1f, 1f);
		
		table.AddCell(new CParaPart("Branch: Delhi" , GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 4, 0.2f, 0.2f, 1f, 1f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 4, 0.2f, 0.2f, 0f, 0f);
		table.AddCell(new CParaPart("Department: " + employee.getDesignation().getDepartment().getDeptName(), GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 4, 0.2f, 0.2f, 1f, 1f);

		table.AddCell(new CParaPart("Designation: " + employee.getDesignation().getDesignation(), GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 4, 0.2f, 0.2f, 1f, 1f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 4, 0.2f, 0.2f, 0f, 0f);
		table.AddCell(new CParaPart("UAN: " + employee.getUanNumberString() , GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 4, 0.2f, 0.2f, 1f, 1f);
		
		table.AddCell(new CParaPart("Month Days: " + paySlipDate.dayOfMonth().getMaximumValue(), GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 4, 1.0f, 0.2f, 1f, 1f);
		table.AddCell(new CParaPart("Days Present: " + (paySlip.getWorkingDays() == null ? "" : paySlip.getWorkingDays()), GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_CENTER, 0), new Color(200, 200, 200), 4, 1.0f, 0.2f, 0f, 0f);
		table.AddCell(new CParaPart("Absent: " + (paySlipDate.dayOfMonth().getMaximumValue() - (paySlip.getWorkingDays() == null ? 0 : paySlip.getWorkingDays())) + " Days", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 4, 1.0f, 0.2f, 1f, 1f);
		
		table.AddCell(new CParaPart("Earnings " , GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 1.0f, 0f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("Amount", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 1.0f, 0f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("Deductions" , GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 1.0f, 0f, 0f, 0.2f);
		table.AddCell(new CParaPart("Amount", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 1.0f, 0f, 0.2f, 1.0f);
		
		Float pf = (paySlip.getPf()== null ? 0 : paySlip.getEsi());
		
		table.AddCell(new CParaPart("Basic Salary", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", paySlip.getBasicSalary()), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("Statutory PF", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", pf), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		
		Float hra = (paySlip.getHra() == null ? 0 : paySlip.getHra());
		Float esi = (paySlip.getEsi()== null ? 0 : paySlip.getEsi());
		
		table.AddCell(new CParaPart("House Rent Allowance" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", hra), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("Statutory ESI", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", esi), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);

		Float specialAllowances = (paySlip.getSpecialAllowances()== null ? 0 : paySlip.getSpecialAllowances());
		Float deductionLeave = (paySlip.getDeductionLeave()== null ? 0 : paySlip.getDeductionLeave());
		
		table.AddCell(new CParaPart("Special Allowance" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", specialAllowances), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("Leave", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3,  0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", deductionLeave), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);

		Float medicalAllowances = (paySlip.getMedical()== null ? 0 : paySlip.getMedical());
		Float deductionTds = (paySlip.getDeductionTds()== null ? 0 : paySlip.getDeductionTds());
		
		table.AddCell(new CParaPart("Medical Allowance" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", medicalAllowances), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("TDS", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", deductionTds), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);

		Float attireAllowances = (paySlip.getAttireAllowances()== null ? 0 : paySlip.getAttireAllowances());
		Float otherDeductions = (paySlip.getOtherDeductions()== null ? 0 : paySlip.getOtherDeductions());
		
		table.AddCell(new CParaPart("Attire Allowances" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", attireAllowances), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("Other Deductions", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", otherDeductions), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		
		Float lta = (paySlip.getLta()== null ? 0 : paySlip.getLta());

		table.AddCell(new CParaPart("LTA" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", lta), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		
		Float vehicleReimburse = (paySlip.getVehicleReimburse()== null ? 0 : paySlip.getVehicleReimburse());
		vehicleReimburse = vehicleReimburse + (paySlip.getVehicleRunning()== null ? 0 : paySlip.getVehicleRunning());
		
		table.AddCell(new CParaPart("Vehicle Running" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", vehicleReimburse), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);

		Float telephoneReimburse = (paySlip.getTelephoneReimburse()== null ? 0 : paySlip.getTelephoneReimburse());
		
		table.AddCell(new CParaPart("Tel. Reimbursement" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", telephoneReimburse), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		
		Float buisinessPromotion = (paySlip.getBusinessPromotion()== null ? 0 : paySlip.getBusinessPromotion());
		
		table.AddCell(new CParaPart("Buisiness Promotion" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", buisinessPromotion), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);

		Float driverAllowances = (paySlip.getDriverAllowances()== null ? 0 : paySlip.getDriverAllowances());
		
		table.AddCell(new CParaPart("Driver Allowances" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", driverAllowances), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);

		Float bonus = (paySlip.getBonus()== null ? 0 : paySlip.getBonus());
		
		table.AddCell(new CParaPart("Bonus" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", bonus), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);

		Float variablebonus = (paySlip.getVariableBonus()== null ? 0 : paySlip.getVariableBonus());
		
		table.AddCell(new CParaPart("Var. Bonus" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", variablebonus), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);

		Float otherAllowances = (paySlip.getOtherAllowances()== null ? 0 : paySlip.getOtherAllowances());
		
		table.AddCell(new CParaPart("Other Allowances" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", otherAllowances), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		
		Float arrear = (paySlip.getArrear()== null ? 0 : paySlip.getArrear());
		
		table.AddCell(new CParaPart("Arrear (If any)" , GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", arrear), GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normal, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 0.2f, 0.2f, 1.0f);

		Float totalEarnings = paySlip.getBasicSalary() + hra + specialAllowances + medicalAllowances + attireAllowances + telephoneReimburse + vehicleReimburse + lta + arrear + buisinessPromotion + driverAllowances + bonus + variablebonus + otherAllowances;
		Float totalDeduction = pf + esi + deductionLeave + deductionTds + otherDeductions;
		
		table.AddCell(new CParaPart("Total Earnings" , GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 1.0f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", totalEarnings), GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 1.0f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("Total Deduction", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 0.2f, 1.0f, 0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", totalDeduction), GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 0.2f, 1.0f, 0.2f, 1.0f);

		Float netPay = totalEarnings - totalDeduction;
		
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 1.0f, 0.2f, 1.0f, 0.2f);
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 1.0f, 0.2f, 0.2f, 1.0f);
		table.AddCell(new CParaPart("Net Pay", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 3, 1.0f, 0.2f, 0f, 0.2f);
		table.AddCell(new CParaPart("" + String.format("%.02f", netPay), GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_RIGHT, 0), new Color(200, 200, 200), 3, 1.0f, 0.2f, 0.2f, 1.0f);

		table.AddCell(new CParaPart("", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 12, true);
		
		table.AddCell(new CParaPart("Rupees in Words: " + NumberToWords.convert(netPay.longValue()) + " Rupees Only", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 12, true);
		
		table.AddCell(new CParaPart("", GenericPdf.Fonts.normalBold, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 12, true);
		
		table.AddCell(new CParaPart("Note - This is system generated copy hence stamp is not required", GenericPdf.Fonts.normalItalic, GenericPdf.Fonts.normalLeading, Element.ALIGN_LEFT, 0), new Color(200, 200, 200), 12, true);

		document.add(table.GetTable());
		document.add(CParagraph.SpacerParagraph());
        
    }
}
