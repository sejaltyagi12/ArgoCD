package com.ems.servicefinder.excelHelper;

import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import com.ems.servicefinder.utils.ExcelType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * Created by Avinash Tyagi on 2017-06-15.
 */
public class ExcelView extends AbstractXlsView{

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"my-xls-file.xls\"");
        
        ExcelType type = (ExcelType) model.get("type");
        
		switch (type) {
		case LEAVE:
			new LeaveExcelViewGenerator().buildExcelDocument(model,
					workbook, request, response);
			break;
			
		case JOINEE:
			new JoineeExcelGenerator().buildExcelDocument(model,
					workbook, request, response);
			break;
			
		case EVALUATION:
			new EvaluationReportGenerator().buildExcelDocument(model,
					workbook, request, response);
			break;

		default:
			break;
		}
    }

}
