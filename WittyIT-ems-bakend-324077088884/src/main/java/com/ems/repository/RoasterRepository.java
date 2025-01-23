package com.ems.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.domain.Roaster;

public interface RoasterRepository extends JpaRepository<Roaster, Long> {
	
	/**
	 * Find by id and RoasterDate
	 * @param id
	 * @param date the Roaster Date
	 * @return Roaster
	 */
	Roaster findByEmpIdAndRoasterDate(Long id, DateTime dateTime);
	
	/**
	 * Find by id and RoasterDates
	 * @param id
	 * @param startDate the Roaster start Date
	 * @param endDate the Roaster start Date
	 * 
	 * @return Luist of Roaster
	 */
	List<Roaster> findByEmpIdAndRoasterDateBetween(Long id, DateTime startDate, DateTime endDate);

}
