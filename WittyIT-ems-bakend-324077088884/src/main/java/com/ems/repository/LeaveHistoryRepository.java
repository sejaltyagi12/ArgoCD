package com.ems.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ems.domain.LeaveHistory;
import com.ems.domain.LeaveHistory.Status;
import com.ems.domain.LeaveType;

public interface LeaveHistoryRepository extends JpaRepository<LeaveHistory, Long> {

	@Query("SELECT COALESCE(MAX(lh.id), 0) +1 FROM LeaveHistory lh")
	Long getMaxLeaveHistoryId();

	/**
	 * Find by id
	 * 
	 * @param id
	 * @return LeaveHistory
	 */
	LeaveHistory findById(Long id);

	/**
	 * Find by employee id
	 * 
	 * @param employee
	 *            id
	 * @return LeaveHistory list of leave
	 */
	List<LeaveHistory> findByEmpId(Long empId);

	/**
	 * Find by Status
	 * 
	 * @param status
	 * @return LeaveHistory list of leave
	 */
	List<LeaveHistory> findByStatus(Status status);

	/**
	 * Delete all leaves of an employee
	 * 
	 * @param employee
	 *            id
	 */
	void deleteByEmpId(Long empId);

	/**
	 * Find leave by managerId and Status
	 * 
	 * @param manager
	 *            id
	 * @param Status
	 * @return LeaveHistory list of leave
	 */
	List<LeaveHistory> findByManagerIdAndStatus(Long empId, Status status);

	/**
	 * Find leave by managerId
	 * 
	 * @param manager
	 *            id
	 * @return LeaveHistory list of leave
	 */
	List<LeaveHistory> findByManagerId(Long managerId);

	/**
	 * Find leave list between two dates
	 * 
	 * @param actionTaken
	 * @return LeaveHistory list of leave
	 */
	List<LeaveHistory> findByFromDateBetweenOrToDateBetween(Date fromStartDate, Date fromEndDate, Date toStartDate,
			Date toEndDate);

	/**
	 * Find leave list between two dates
	 * 
	 * @param actionTaken
	 * @return LeaveHistory list of leave
	 */
	List<LeaveHistory> findByEmpIdAndToDateBetween(Long empId, Date fromStartDate, Date fromEndDate);

	/**
	 * Find leave by employeeId, Leave Type, status and date between
	 * 
	 * @param employee
	 *            id
	 * @param leaveType
	 * @param Status
	 * @param fromDate
	 * @param toDate
	 * @return LeaveHistory list of leave
	 */
	List<LeaveHistory> findByEmpIdAndLeaveTypeAndStatusAndToDateBetween(Long empId, LeaveType leaveType, Status status,
			Date fromDate, Date toDate);
	
	
	/**
	 * Count leave by employeeId, Leave Type, status and date between
	 * @param empId
	 * @param leaveType
	 * @param status
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @author Sarit Mukherjee
	 */
	long countByEmpIdAndLeaveTypeAndStatusAndToDateBetween(Long empId, LeaveType leaveType, Status status,
			Date fromDate, Date toDate);

	/**
	 * Find leave by employeeId, status, and date between
	 * 
	 * @param employee
	 *            id
	 * @param leaveType
	 * @param Status
	 * @param fromDate
	 * @param toDate
	 * @return LeaveHistory list of leave
	 */
	List<LeaveHistory> findByEmpIdAndStatusAndToDateBetween(Long empId, Status status, Date fromDate, Date toDate);

	@Query("Select lh from LeaveHistory lh where lh.status=:status and lh.fromDate < :currentDate")
	List<LeaveHistory> findAllAutoApprovedLeaveList(@Param("status") Status status,
			@Param("currentDate") Date currentDate);

	@Query("Select lh from LeaveHistory lh join lh.leaveType lt where lh.empId=:empId and lh.status=:status and lt.type In :type")
	List<LeaveHistory> findByEmpIdAndLeaveTypeAndStatus(@Param("empId") Long empId, @Param("type") List<String> type,
			@Param("status") Status status);

	@Query("Select lh from LeaveHistory lh where lh.empId=:empId and lh.status IN :status and lh.fromDate <=:toDate and lh.toDate >=:fromDate")
	List<LeaveHistory> findDuplicateEntry(@Param("empId") Long empId, @Param("status") List<Status> status,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

	@Query("Select lh from LeaveHistory lh  join lh.leaveType lt where lh.empId=:empId and lh.status =:status and (lh.fromDate BETWEEN :fromDate AND :toDate)")
	List<LeaveHistory> findAllDataByMonth(@Param("empId") Long empId, @Param("status") Status status,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	@Query("Select lh from LeaveHistory lh  join lh.leaveType lt where lh.empId=:empId and lh.status =:status and (lh.fromDate BETWEEN :fromDate AND :toDate or lh.toDate BETWEEN :fromDate AND :toDate)")
	List<LeaveHistory> findAllDataByMonthWithToDate(@Param("empId") Long empId, @Param("status") Status status,
			@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);
	
	/**
	 * Will get all leave histories but the user id given will not contain in it and level of the employees of the leave historys will be less then the level given.
	 * @param empId
	 * @return
	 */
	@Query(value = "select * from leave_history lh join employee e on lh.emp_id = e.id join designation d on e.designation_id = d.designation_id where convert(substring_index(d.level,'_',-1),unsigned integer) < :level and not e.id = :empId", nativeQuery = true)
	List<LeaveHistory> findAllButHimselfWithLessLevel(@Param("level") Integer level,@Param("empId") Long empId);
	
	/**Using String Status as it is a native sql native query
	 * @param level
	 * @param empId
	 * @param status
	 * @return
	 */
	@Query(value = "select * from leave_history lh join employee e on lh.emp_id = e.id join designation d on e.designation_id = d.designation_id where convert(substring_index(d.level,'_',-1),unsigned integer) < :level and not e.id = :empId and lh.status = :status", nativeQuery = true)
	List<LeaveHistory> findAllButHimselfWithLessLevelAndStatus(@Param("level") Integer level,@Param("empId") Long empId, @Param("status") String status);
	
	/**
	 * It will get user all sick leaves  as list
	 * @param empId
	 * @param typeId
	 * @return
	 */
	@Query(nativeQuery = true,value = "SELECT * FROM leave_history AS l WHERE emp_id = :empId and type_id=:typeId")
	List<LeaveHistory> findLastSickLeaveOfEmployee(@Param("empId") Long empId, @Param("typeId") int typeId);
	
	/**
	 * get login employee leave by leave id
	 * @param empId
	 * @param id
	 * @return
	 */
	@Query(nativeQuery = true,value = "SELECT lhd.deducted_casual_leave,lhd.deducted_privilege_leave,lhd.deducted_sick_leave,lhd.deducted_leave_with_out_pay,lhd.first_month_leave_with_out_pay FROM leave_history_details lhd JOIN leave_history lh ON lhd.leave_history_detail = lh.id WHERE lh.emp_id =:empId and lh.id=:id ORDER BY lhd.leave_history_detail_id DESC LIMIT 1;")
	Object[] findLastLeaveOfEmployee(@Param("empId")Long empId,@Param("id")Long id);
	
	
	/**
	 * get all leaves by logged in manager
	 * @param managerId
	 * @param status
	 * @return
	 * @author krishna kumar
	 */
	@Query(nativeQuery = true, value = "SELECT lh.* FROM leave_history lh JOIN employee e ON lh.emp_id = e.id WHERE e.manager_id = :managerId AND e.is_active = true")
	List<LeaveHistory> findLeavesByManagerIdAndActive(@Param("managerId") Long managerId);
		
	
}
