package com.ems.wrappers;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

import com.ems.domain.Employee;
import com.ems.domain.Resignation;
import com.ems.domain.ResignationReason;
import com.ems.domain.ResignationType;
import com.ems.domain.Resignation.Status;
import com.ems.servicefinder.utils.DateTimeDeserializer;
import com.ems.servicefinder.utils.DateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The wrapper is used because we need to change change private notes to null
 * sometime. But working with entity directly was changing DB. Thus DTO is
 * created.
 * 
 * @author Sarit Mukherjee
 *
 */
public class ResignationWrapper {

	private Long id;

	@JsonIgnore
	private Employee employee;
	private Long managerId;

	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime resignationDate;

	private String resignationText;

	@JsonIgnore
	private DateTime creationDate;

	@JsonIgnore
	private DateTime modifiedDate;

	@JsonIgnore
	private Long createdBy;

	@JsonIgnore
	private Long modifiedBy;

	private String managerPublicNotes;

	private String managerPrivateNotes;

	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime lastDay;

	private String hrPublicNotes;

	private String hrPrivateNotes;

	private ResignationReason reason;

	private ResignationType type;

	@Enumerated(EnumType.STRING)
	private Status managerStatus = Status.PENDING;
	
	@Enumerated(EnumType.STRING)
	private Status hrStatus =  Status.PENDING;

	@Transient
	private String employeeName;

	@Transient
	private String employeeCode;

	@Transient
	private Long reasonId;

	@Transient
	private Long typeId;

	@Transient
	private boolean managerAccepted;

	@Transient
	private boolean hrAccepted;
	
	private boolean reHired;
	
	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime joiningDate;

	public ResignationWrapper(Resignation resignation) {
		this.id = resignation.getId();
		this.employee = resignation.getEmployee();
		this.managerId = resignation.getManagerId();
		this.resignationDate = resignation.getResignationDate();
		this.resignationText = resignation.getResignationText();
		this.creationDate = resignation.getCreationDate();
		this.modifiedDate = resignation.getModifiedDate();
		this.createdBy = resignation.getCreatedBy();
		this.modifiedBy = resignation.getModifiedBy();
		this.managerPublicNotes = resignation.getManagerPublicNotes();
		this.managerPrivateNotes = resignation.getManagerPrivateNotes();
		this.lastDay = resignation.getLastDay();
		this.hrPublicNotes = resignation.getHrPublicNotes();
		this.hrPrivateNotes = resignation.getHrPrivateNotes();
		this.reason = resignation.getReason();
		this.type = resignation.getType();
		this.managerStatus = resignation.getManagerStatus();
		this.hrStatus = resignation.getHrStatus();
		this.employeeName = resignation.getEmployeeName();
		this.employeeCode = resignation.getEmployeeCode();
		this.reasonId = resignation.getReasonId();
		this.typeId = resignation.getTypeId();
		this.managerAccepted = resignation.isManagerAccepted();
		this.hrAccepted = resignation.isHrAccepted();
		this.reHired = resignation.getEmployee().getDeleted();
		this.joiningDate = new DateTime(resignation.getEmployee().getJoiningDate());
	}

//	/**
//	 * Resignation.Status describes the possibilities for a resignation status.
//	 * Possible types: APPROVED, PENDING, REJECTED
//	 * 
//	 * @author Avinash Tyagi
//	 */
//	public enum Status {
//		APPROVED("Approved"), PENDING("Pending"), REJECTED("Rejected");
//
//		String stringValue;
//
//		private Status(String stringValue) {
//			this.stringValue = stringValue;
//		}
//
//		/**
//		 * Gets the string value.
//		 *
//		 * @return the string value
//		 */
//		public String getStringValue() {
//			return this.stringValue;
//		}
//
//		public String toString() {
//			return this.stringValue;
//		}
//
//		public static Status getEnum(String value) {
//			for (Status v : values()) {
//				if (v.getStringValue().equalsIgnoreCase(value)) {
//					return v;
//				}
//			}
//			throw new IllegalArgumentException();
//		}
//	}
	/**
	 * Getter of joining date
	 * @return
	 */
	public DateTime getJoiningDate() {
		return joiningDate;
	}

	/**
	 * @return the managerAccepted
	 */
	public boolean isManagerAccepted() {
		return managerAccepted;
	}

	/**
	 * @param managerAccepted
	 *            the managerAccepted to set
	 */
	public void setManagerAccepted(boolean managerAccepted) {
		this.managerAccepted = managerAccepted;
	}

	/**
	 * @return the hrAccepted
	 */
	public boolean isHrAccepted() {
		return hrAccepted;
	}
	/**
	 * @return reHired
	 * @return
	 */
	public boolean isReHired() {
		return reHired;
	}

	/**
	 * @param hrAccepted
	 *            the hrAccepted to set
	 */
	public void setHrAccepted(boolean hrAccepted) {
		this.hrAccepted = hrAccepted;
	}

	/**
	 * @return the reasonId
	 */
	public Long getReasonId() {
		return reasonId;
	}

	/**
	 * @param reasonId
	 *            the reasonId to set
	 */
	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}

	/**
	 * @return the typeId
	 */
	public Long getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 *            the typeId to set
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee
	 *            the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * @return the resignationDate
	 */
	public DateTime getResignationDate() {
		return resignationDate;
	}

	/**
	 * @param resignationDate
	 *            the resignationDate to set
	 */
	public void setResignationDate(DateTime resignationDate) {
		this.resignationDate = resignationDate;
	}

	/**
	 * @return the resignationText
	 */
	public String getResignationText() {
		return resignationText;
	}

	/**
	 * @param resignationText
	 *            the resignationText to set
	 */
	public void setResignationText(String resignationText) {
		this.resignationText = resignationText;
	}

	/**
	 * @return the creationDate
	 */
	public DateTime getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the modifiedDate
	 */
	public DateTime getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate
	 *            the modifiedDate to set
	 */
	public void setModifiedDate(DateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employee.getFullName();
	}

	/**
	 * @return the managerPublicNotes
	 */
	public String getManagerPublicNotes() {
		return managerPublicNotes;
	}

	/**
	 * @param managerPublicNotes
	 *            the managerPublicNotes to set
	 */
	public void setManagerPublicNotes(String managerPublicNotes) {
		this.managerPublicNotes = managerPublicNotes;
	}

	/**
	 * @return the managerPrivateNotes
	 */
	public String getManagerPrivateNotes() {
		return managerPrivateNotes;
	}

	/**
	 * @param managerPrivateNotes
	 *            the managerPrivateNotes to set
	 */
	public void setManagerPrivateNotes(String managerPrivateNotes) {
		this.managerPrivateNotes = managerPrivateNotes;
	}

	/**
	 * @return the lastDay
	 */
	public DateTime getLastDay() {
		return lastDay;
	}

	/**
	 * @param lastDay
	 *            the lastDay to set
	 */
	public void setLastDay(DateTime lastDay) {
		this.lastDay = lastDay;
	}

	/**
	 * @return the hrPublicNotes
	 */
	public String getHrPublicNotes() {
		return hrPublicNotes;
	}

	/**
	 * @param hrPublicNotes
	 *            the hrPublicNotes to set
	 */
	public void setHrPublicNotes(String hrPublicNotes) {
		this.hrPublicNotes = hrPublicNotes;
	}

	/**
	 * @return the hrPrivateNotes
	 */
	public String getHrPrivateNotes() {
		return hrPrivateNotes;
	}

	/**
	 * @param hrPrivateNotes
	 *            the hrPrivateNotes to set
	 */
	public void setHrPrivateNotes(String hrPrivateNotes) {
		this.hrPrivateNotes = hrPrivateNotes;
	}

	/**
	 * @return the reason
	 */
	public ResignationReason getReason() {
		return reason;
	}

	/**
	 * @param reason
	 *            the reason to set
	 */
	public void setReason(ResignationReason reason) {
		this.reason = reason;
	}

	/**
	 * @return the type
	 */
	public ResignationType getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ResignationType type) {
		this.type = type;
	}

	/**
	 * @return the managerStatus
	 */
	public Status getManagerStatus() {
		return managerStatus;
	}

	/**
	 * @param managerStatus
	 *            the managerStatus to set
	 */
	public void setManagerStatus(Status managerStatus) {
		this.managerStatus = managerStatus;
	}

	/**
	 * @return the hrStatus
	 */
	public Status getHrStatus() {
		return hrStatus;
	}

	/**
	 * @param hrStatus
	 *            the hrStatus to set
	 */
	public void setHrStatus(Status hrStatus) {
		this.hrStatus = hrStatus;
	}

	/**
	 * @param employeeName
	 *            the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the modifiedBy
	 */
	public Long getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @return the managerId
	 */
	public Long getManagerId() {
		return managerId;
	}

	/**
	 * @param managerId
	 *            the managerId to set
	 */
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	/**
	 * @param modifiedBy
	 *            the modifiedBy to set
	 */
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the employeeCode
	 */
	public String getEmployeeCode() {
		return employee.getEmpCode();
	}

	/**
	 * @param employeeCode
	 *            the employeeCode to set
	 */
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	
	

	@Override
	public String toString() {
		return "ResignationWrapper [id=" + id + ", employee=" + employee + ", managerId=" + managerId
				+ ", resignationDate=" + resignationDate + ", resignationText=" + resignationText + ", creationDate="
				+ creationDate + ", modifiedDate=" + modifiedDate + ", createdBy=" + createdBy + ", modifiedBy="
				+ modifiedBy + ", managerPublicNotes=" + managerPublicNotes + ", managerPrivateNotes="
				+ managerPrivateNotes + ", lastDay=" + lastDay + ", hrPublicNotes=" + hrPublicNotes
				+ ", hrPrivateNotes=" + hrPrivateNotes + ", reason=" + reason + ", type=" + type + ", managerStatus="
				+ managerStatus + ", hrStatus=" + hrStatus + ", employeeName=" + employeeName + ", employeeCode="
				+ employeeCode + ", reasonId=" + reasonId + ", typeId=" + typeId + ", managerAccepted="
				+ managerAccepted + ", hrAccepted=" + hrAccepted + "]";
	}

}
