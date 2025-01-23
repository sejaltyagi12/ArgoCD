package com.ems.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ems.configuration.PropConfig;
import com.ems.domain.CompanyCategory;
import com.ems.domain.Employee;
import com.ems.domain.Holiday;
import com.ems.exception.ServiceException;
import com.ems.repository.HolidayRepository;
import com.ems.servicefinder.utils.UserUtils;
import com.ems.wrappers.FileUploadWrapper;
import com.ems.wrappers.HolidayWrapper;

@Service
@Transactional
public class HolidayService {

	@Autowired
	private HolidayRepository holidayRepository;

	@Autowired
	private CompanyCategoryService companyCategoryService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private FileService fileService;

	@Autowired
	private PropConfig propConfig;

	/**
	 * Find by id.
	 *
	 * @param id
	 * 
	 * @return the Holiday
	 */
	public Holiday findById(Integer id) {
		return holidayRepository.findById(id);
	}

	/**
	 * Fetch all Holidays.
	 *
	 * 
	 * @return the Holiday List
	 * @throws ServiceException
	 */
	public List<HolidayWrapper> getHolidays(Boolean adminMode) throws ServiceException {

		List<HolidayWrapper> holidayWrappers = new ArrayList<HolidayWrapper>();
		List<Holiday> holidays = (authenticationService.isAdmin() || authenticationService.isHR()) && adminMode
				? holidayRepository.findAll()
				: holidayRepository.findByCompanyCategoryAndHolidayDateBetweenOrderByHolidayDateAsc(
						authenticationService.getAuthenticatedEmployee().getCompanyCategory(), new DateTime().withTimeAtStartOfDay(),
						new DateTime().dayOfYear().withMaximumValue());

		for (Holiday holiday : holidays) {
			holidayWrappers.add(new HolidayWrapper(holiday.getId(), holiday.getName(), holiday.getHolidayDate(),
					holiday.getCompanyCategory().getCompanyName(), holiday.getCompanyCategory().getCompanyId(),
					fileService.getAssetCompleteUrl(
							holiday.getImageUrl() != null ? propConfig.getBaseContext() + holiday.getImageUrl()
									: null)));
		}

		return holidayWrappers;
	}
	
	/**
	 * It will check holiday is not on user applied from date or to date
	 * @param currentYear
	 * @param nextYear
	 * @return
	 */
	public Boolean checkIsHolidayOnFromDateOrToDate(DateTime startDate,DateTime endDate){
		String pattern = "dd-MM-yyyy";

        // Create a DateTimeFormatter
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);

        // Format the Joda-Time DateTime to a String
        String currentYear = formatter.print(startDate);
        String nextYear = formatter.print(endDate);
		return holidayRepository.checkIsHolidayOnFromDateOrToDate(currentYear,nextYear);
		
	}

	/**
	 * Gets a single holiday based on Id.
	 *
	 * @param id
	 *            the Holiday Id
	 * @return the Holiday
	 * @throws ServiceException
	 */
	public HolidayWrapper getHoliday(Integer id) throws ServiceException {

		Holiday holiday = holidayRepository.findById(id);

		if (holiday != null)
			return new HolidayWrapper(holiday.getId(), holiday.getName(), holiday.getHolidayDate(),
					holiday.getCompanyCategory().getCompanyName(), holiday.getCompanyCategory().getCompanyId(),
					fileService.getAssetCompleteUrl(
							holiday.getImageUrl() != null ? propConfig.getBaseContext() + holiday.getImageUrl()
									: null));

		throw new ServiceException("Holiday not found");
	}

	/**
	 * Add Holiday/ Update Holiday
	 *
	 * @param Holiday
	 * @throws ServiceException
	 */
	public Holiday addHoliday(HolidayWrapper holidayWrapper) throws ServiceException {

		Holiday holiday;

		if (holidayWrapper.getId() != null) {
			holiday = findById(holidayWrapper.getId());

			if (holiday != null) {
				holiday.setName(holidayWrapper.getName());
				holiday.setHolidayDate(holidayWrapper.getHolidayDate());
			} else {
				throw new ServiceException("Holiday not found.");
			}
		} else {
			holiday = new Holiday(holidayWrapper.getId(), holidayWrapper.getName(), holidayWrapper.getHolidayDate());
		}

		// setting holiday date +1 days because in DB holiday date is set one day before
		holiday.setHolidayDate(holiday.getHolidayDate().withZone(DateTimeZone.UTC).withTimeAtStartOfDay().plusDays(1));

		CompanyCategory company = companyCategoryService.findById(holidayWrapper.getCompanyId());

		if (company != null)
			holiday.setCompanyCategory(company);
		else
			throw new ServiceException("Company not found");

		return holidayRepository.save(holiday);
	}

	/**
	 * Delete Holiday.
	 *
	 * @param Holiday
	 * @throws ServiceException
	 */
	public void deleteHoliday(Integer id) throws ServiceException {

		Holiday holiday = holidayRepository.findById(id);

		if (holiday != null)
			holidayRepository.delete(holiday);
		else
			throw new ServiceException("Holiday not found");
	}

	/**
	 * upload holiday Image Url associated: /holiday/image/upload
	 * 
	 * @param Holiday
	 *            Id
	 * @param FileUploadWrapper
	 * @throws ServiceException
	 */
	public String uploadHolidayImage(FileUploadWrapper uploadWrapper, Integer id) throws ServiceException {

		Holiday holiday = holidayRepository.findById(id);

		if (holiday == null)
			throw new ServiceException("Holiday not found.");

		UserUtils.validateFileSize(512, uploadWrapper.getFileContent(), "image", false, true, false);

		try {
			String fileName = UserUtils.getAssetStandardName(uploadWrapper.getFileContent().getName(), "image");

			// String imageUrl = storeAsset(holiday,
			// uploadWrapper.getFileContent().getBytes(), fileName);
			String imageUrl = fileService.storeAsset(holiday.getId().longValue(),
					uploadWrapper.getFileContent().getBytes(), fileName, "holiday");
			holiday.setImageUrl(imageUrl);

			holidayRepository.save(holiday);

			return fileService.getAssetCompleteUrl(holiday.getImageUrl());
		} catch (Exception e) {
			throw new ServiceException("Error while uploading image.");
		}
	}

	public Set<LocalDate> getHolidayDatesBetween(Employee employee, LocalDate startDate, LocalDate endDate) {
		Set<LocalDate> holiday = new HashSet<>();
		List<Holiday> holidayList = holidayRepository.findByCompanyCategoryAndHolidayDateBetweenOrderByHolidayDateAsc(
				employee.getCompanyCategory(), startDate.toDateTimeAtStartOfDay(), endDate.toDateTimeAtStartOfDay());

		for (Holiday data : holidayList) {
			holiday.add(new LocalDate(data.getHolidayDate()));
		}

		return holiday;
	}
	
	/**
	 * It will check holiday is not on user applied from date or to date
	 * 
	 * @param currentYear
	 * @param nextYear
	 * @return
	 * @author krishna kumar
	 */
	public String checkHolidayOnFromDateOrToDateForShowingMessage(DateTime startDate, DateTime endDate) {

		String pattern = "dd-MM-yyyy";
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);

		String startDateString = formatter.print(startDate);
		String endDateString = formatter.print(endDate);

		boolean isStartDateHoliday = holidayRepository.checkIsHolidayOnDate(startDateString);
		boolean isEndDateHoliday = holidayRepository.checkIsHolidayOnDate(endDateString);

		if (isStartDateHoliday) {
			return startDateString;
		} else if (isEndDateHoliday) {
			return endDateString;
		}

		return null;

	}


}
