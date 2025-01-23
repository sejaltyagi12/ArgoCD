package com.ems.repository;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ems.domain.CompanyCategory;
import com.ems.domain.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
	
	
	/**
	 * Find by id
	 * @param id
	 * @return Holiday
	 */
	Holiday findById(Integer id);
	
	/**
	 * Find all 
	 * @return Holiday list 
	 */
	List<Holiday> findAll();
	
	/**
	 * Find all Holidays associated to a company
	 * 
	 * @param Company id
	 * @return Holidays list of all departments
	 */
	List<Holiday> findByCompanyCategory(CompanyCategory category);
		
	

	List<Holiday> findByCompanyCategoryAndHolidayDateBetweenOrderByHolidayDateAsc(CompanyCategory category, DateTime startDate , DateTime endDate);
	
	/**
	 * It will check holiday is not on user applied from date or to date
	 * @param currentYear
	 * @param nextYear
	 * @return
	 */
	@Query("Select count(h.id)>0 from Holiday h where DATE_FORMAT(h.holidayDate, '%d-%m-%Y') = :currentYear or DATE_FORMAT(h.holidayDate, '%d-%m-%Y') =:nextYear")
	Boolean checkIsHolidayOnFromDateOrToDate(@Param("currentYear") String currentYear, @Param("nextYear")String nextYear);
	
	/**
	 * It will check holiday dates
	 * @param date
	 * @return
	 * @author krishna kumar
	 */
	@Query("SELECT COUNT(h.id) > 0 FROM Holiday h WHERE DATE_FORMAT(h.holidayDate, '%d-%m-%Y') = :date")
	Boolean checkIsHolidayOnDate(@Param("date") String date);
}