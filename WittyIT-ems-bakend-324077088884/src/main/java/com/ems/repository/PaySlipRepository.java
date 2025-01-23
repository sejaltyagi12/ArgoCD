package com.ems.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ems.domain.PaySlip;

public interface PaySlipRepository extends JpaRepository<PaySlip, Long> {

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return PaySlip
	 */
	PaySlip findById(Long id);

	/**
	 * Find by Employee id
	 * 
	 * @param id
	 *            the Employee Id
	 * @return PaySlip
	 */
	List<PaySlip> findByEmpId(Long empId);

	/**
	 * Find by Employee id, Month and year.
	 * 
	 * @param id
	 *            the Employee Id
	 * @param month
	 * @param year
	 * @return PaySlip
	 */
	PaySlip findByEmpIdAndMonthAndYear(Long empId, Integer month, Integer year);

	/**
	 * Find by Employee id, Month and year.
	 * 
	 * @param id
	 *            the Employee Id
	 * @param month
	 * @param year
	 * @return PaySlip list
	 */
	List<PaySlip> findByEmpIdAndYearAndMonthGreaterThanEqual(Long empId, Integer year, Integer month);

	@Query(value = "select ps from PaySlip ps where ps.empId = :empId ORDER BY ps.year DESC,ps.month DESC")
	List<PaySlip> findLatestPaySlip(@Param("empId") Long empId ,Pageable page);
}
