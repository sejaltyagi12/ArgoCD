package com.ems.service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ems.domain.Employee;
import com.ems.domain.Roaster;
import com.ems.domain.RoasterType;
import com.ems.exception.ServiceException;
import com.ems.repository.RoasterRepository;
import com.ems.repository.RoasterTypeRepository;
import com.ems.wrappers.FileUploadWrapper;

@Service
@Transactional
public class RoasterService {

	private static final Integer DATE_START_ROW = 0;
	
	private static final Integer DATE_START_COLUMN = 2;
	
	private static final Integer ROASTER_START_ROW_INDEX = 2;
	
	private static final Integer ROASTER_START_COLUMN_INDEX = 2;
	
	private static final Integer EMPLOYEE_CODE_COLUMN_INDEX = 1;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private RoasterTypeRepository roasterTypeRepository;
	
	@Autowired
	private RoasterRepository roasterRepository;
	
	
	/**
	 * Get list of roasters in month
	 * Url associated: /roaster/get
	 * @param uploadWrapper FileUploadWrapper
	 * @throws ServiceException
	 */
	public List<Roaster> getRoasterList(Long id) throws ServiceException {
		
		Employee employee = id == null ? authenticationService.getAuthenticatedEmployee() : employeeService.findById(id);
		
		if(!(authenticationService.isAdmin() || authenticationService.isHR()) && id != null)
			throw new ServiceException("You dont have rights to view.");
		
		DateTime firstDayOfMonth = new DateTime().dayOfMonth().withMinimumValue();
		
		DateTime lastDayOfMonth = new DateTime().dayOfMonth().withMaximumValue();
		
		return roasterRepository.findByEmpIdAndRoasterDateBetween(employee.getId(), firstDayOfMonth.minusDays(1) , lastDayOfMonth);

	}
	
	
	/**
	 * To process uploaded roaster excel
	 * Url associated: /roaster/upload
	 * @param uploadWrapper FileUploadWrapper
	 * @throws ServiceException
	 */
	public void processRoasterSheet(FileUploadWrapper uploadWrapper) throws ServiceException {
		
        Workbook workbook;
        try{
    		ByteArrayInputStream bis = new ByteArrayInputStream(uploadWrapper.getFileContent().getBytes());
        	
            if (uploadWrapper.getFileContent().getOriginalFilename().endsWith("xls")) {
                workbook = new HSSFWorkbook(bis);
            } else if (uploadWrapper.getFileContent().getOriginalFilename().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(bis);
            } else {
                throw new ServiceException("Received file does not have a standard excel extension.");
            }

			List<DateTime> dates = new ArrayList<DateTime>();

 			for (Row row : workbook.getSheetAt(0)) 
			{
				Employee employee = null;
				
				Iterator<Cell> cellIterator = row.cellIterator();
				
				if (row.getRowNum() == DATE_START_ROW) 
				{
					while (cellIterator.hasNext()) 
					{
						Cell cell = cellIterator.next();
						if(cell.getColumnIndex() >= DATE_START_COLUMN)
							dates.add(new DateTime(cell.getDateCellValue()));
					}
				}
				
				if (row.getRowNum() >= ROASTER_START_ROW_INDEX) 
				{
					while (cellIterator.hasNext()) 
					{
						Cell cell = cellIterator.next();
												
						if (cell.getColumnIndex() == EMPLOYEE_CODE_COLUMN_INDEX)
						{
							employee = employeeService.findByEmpCode(cell.getStringCellValue());
						}
						else if ((cell.getColumnIndex() >= ROASTER_START_COLUMN_INDEX))
						{
							if (employee != null) 
							{
								RoasterType roasterType = roasterTypeRepository.findByType(cell.getStringCellValue());
								
								if (roasterType != null) 
								{
									Roaster roaster = roasterRepository.findByEmpIdAndRoasterDate(employee.getId(), dates.get(cell.getColumnIndex() - ROASTER_START_ROW_INDEX));
									
									if (roaster == null)
										roaster = new Roaster(roasterType, dates.get(cell.getColumnIndex() - ROASTER_START_ROW_INDEX), employee.getId());
									else if (roaster.getRoasterDate().isAfterNow())
										roaster.setRoasterType(roasterType);
									
									roasterRepository.save(roaster);
								}
							}
						}
					}
				}
			}
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
	}
	
}
