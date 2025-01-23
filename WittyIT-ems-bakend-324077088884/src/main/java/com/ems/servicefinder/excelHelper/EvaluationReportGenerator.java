package com.ems.servicefinder.excelHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.ems.servicefinder.utils.EmsMathUtils;
import com.ems.wrappers.EvaluationHistoryWrapper;
import com.ems.wrappers.EvaluationQuestionWrapper;
import com.ems.wrappers.TargetListWrapper;

public class EvaluationReportGenerator {

	DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

	public void buildExcelDocument(Map<String, Object> model,
			Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		@SuppressWarnings("unchecked")
		List<TargetListWrapper> targetList = (List<TargetListWrapper>) model .get("targetList");
		
		Collections.sort(targetList);

		@SuppressWarnings("unchecked")
		List<EvaluationHistoryWrapper> evaluationList = (List<EvaluationHistoryWrapper>) model .get("evaluations");

		List<EvaluationQuestionWrapper> questions = new ArrayList<>();

		for (TargetListWrapper targetListWrapper : targetList) 
		{
			List<EvaluationQuestionWrapper> tempQuestions = targetListWrapper.getQuestions();
			
			Collections.sort(tempQuestions);
			
			questions.addAll(tempQuestions);
		}

		Sheet sheet = workbook.createSheet("Evaluations List");
		
		int columnCount = questions.size()*2 + 4;
		
		int columnWidths[] = new int[columnCount];
		
		for (int i = 0; i < columnCount; i++) 
			columnWidths[i] = 2;
		
		CExcelTable table = new CExcelTable(workbook, sheet, columnCount, columnWidths);
		
		table.AddCell(new CExcelTableCell(" ", Font.BOLDWEIGHT_BOLD, HSSFColor.LIGHT_GREEN.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		table.AddCell(new CExcelTableCell(" ", Font.BOLDWEIGHT_BOLD, HSSFColor.LIGHT_GREEN.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		
		// Adding Question Categories 
		for (TargetListWrapper targetListWrapper : targetList)
			table.AddCell(new CExcelTableCell(targetListWrapper.getCategory(), Font.BOLDWEIGHT_BOLD, HSSFColor.LIGHT_GREEN.index, targetListWrapper.getQuestions().size()*2, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_MEDIUM));

		table.AddCell(new CExcelTableCell(" ", Font.BOLDWEIGHT_BOLD, HSSFColor.LIGHT_GREEN.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		table.AddCell(new CExcelTableCell(" ", Font.BOLDWEIGHT_BOLD, HSSFColor.LIGHT_GREEN.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		
		// Adding Questions 
		table.AddCell(new CExcelTableCell(" ", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		table.AddCell(new CExcelTableCell(" ", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));

		for (EvaluationQuestionWrapper question : questions)
			table.AddCell(new CExcelTableCell(question.getName(), Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 2, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		
		table.AddCell(new CExcelTableCell(" ", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		table.AddCell(new CExcelTableCell(" ", Font.BOLDWEIGHT_BOLD, HSSFColor.SKY_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		
		// Adding ratings and other data
		table.AddCell(new CExcelTableCell("S.No", Font.BOLDWEIGHT_BOLD, HSSFColor.CORNFLOWER_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		table.AddCell(new CExcelTableCell("Employee Name", Font.BOLDWEIGHT_BOLD, HSSFColor.CORNFLOWER_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		
		for(@SuppressWarnings("unused") EvaluationQuestionWrapper question : questions)
		{
			table.AddCell(new CExcelTableCell("Self Rating", Font.BOLDWEIGHT_BOLD, HSSFColor.PALE_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
			table.AddCell(new CExcelTableCell("Manager Rating", Font.BOLDWEIGHT_BOLD, HSSFColor.PALE_BLUE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		}
		
		table.AddCell(new CExcelTableCell("Employee Average", Font.BOLDWEIGHT_BOLD, HSSFColor.LIGHT_ORANGE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
		table.AddCell(new CExcelTableCell("Manager Average", Font.BOLDWEIGHT_BOLD, HSSFColor.LIGHT_ORANGE.index, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));

		for (int i = 0; i < evaluationList.size(); i++)
		{
			EvaluationHistoryWrapper evaluation = evaluationList.get(i);
			
			int totalManagerRatings = 0;
			int totalEmployeeRatings = 0;
						
			table.AddCell(new CExcelTableCell(""+(i+1), Font.BOLDWEIGHT_NORMAL, HSSFCellStyle.NO_FILL, 1, CellStyle.ALIGN_CENTER, CellStyle.ALIGN_CENTER));
			table.AddCell(new CExcelTableCell(evaluation.getEmployeeName(), Font.BOLDWEIGHT_NORMAL, HSSFCellStyle.NO_FILL, 1, CellStyle.ALIGN_CENTER, CellStyle.ALIGN_CENTER));
						
			List<EvaluationQuestionWrapper> employeeQuestions = new ArrayList<>();
			
			List<TargetListWrapper> categories = evaluation.getCategories();
			
			Collections.sort(categories);
			
			for (TargetListWrapper category : categories)
			{
				List<EvaluationQuestionWrapper> tempQuestions = category.getQuestions();
				
				Collections.sort(tempQuestions);
				
				employeeQuestions.addAll(tempQuestions);
			}
			
			for (EvaluationQuestionWrapper question : questions)
			{
				EvaluationQuestionWrapper presentQuestion = null;
				
				for (EvaluationQuestionWrapper employeeQuestion : employeeQuestions)
				{
					if(question.getId() == employeeQuestion.getId())
						presentQuestion = employeeQuestion;
				}
				
				if(presentQuestion != null)
				{
					totalEmployeeRatings +=  presentQuestion.getTarget().getEvaluationHistory().getEmployeeRating();
					
					table.AddCell(new CExcelTableCell("" +  presentQuestion.getTarget().getEvaluationHistory().getEmployeeRating(), Font.BOLDWEIGHT_NORMAL, HSSFCellStyle.NO_FILL, 1, CellStyle.ALIGN_CENTER, CellStyle.ALIGN_CENTER, CellStyle.BORDER_THIN));
					
					if (presentQuestion.getTarget().getEvaluationHistory().getManagerRating() != null)
					{
						table.AddCell(new CExcelTableCell("" +  presentQuestion.getTarget().getEvaluationHistory().getManagerRating(), Font.BOLDWEIGHT_NORMAL, HSSFCellStyle.NO_FILL, 1, CellStyle.ALIGN_CENTER, CellStyle.ALIGN_CENTER, CellStyle.BORDER_THIN));

						totalManagerRatings += presentQuestion.getTarget().getEvaluationHistory().getManagerRating();
					}
					else
					{
						table.AddCell(new CExcelTableCell(" ", Font.BOLDWEIGHT_BOLD, HSSFCellStyle.NO_FILL, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
					}
				}
				else
				{
					table.AddCell(new CExcelTableCell(" ", Font.BOLDWEIGHT_BOLD, HSSFCellStyle.NO_FILL, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
					table.AddCell(new CExcelTableCell(" ", Font.BOLDWEIGHT_BOLD, HSSFCellStyle.NO_FILL, 1, CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_TOP, CellStyle.BORDER_THIN));
				}
			}
			
			float avgManagerRating =  EmsMathUtils.round(((float) totalManagerRatings/employeeQuestions.size()),1);
			float avgEmployeeRating = EmsMathUtils.round(((float) totalEmployeeRatings/employeeQuestions.size()),1);
			
			// Adding average ratings 
			table.AddCell(new CExcelTableCell("" +  avgEmployeeRating, Font.BOLDWEIGHT_BOLD,  HSSFCellStyle.NO_FILL, 1, CellStyle.ALIGN_CENTER, CellStyle.ALIGN_CENTER, CellStyle.BORDER_THIN));
			table.AddCell(new CExcelTableCell("" +  avgManagerRating, Font.BOLDWEIGHT_BOLD, HSSFCellStyle.NO_FILL, 1, CellStyle.ALIGN_CENTER, CellStyle.ALIGN_CENTER, CellStyle.BORDER_THIN));
		}

		table.CreateTable();
	}

}
