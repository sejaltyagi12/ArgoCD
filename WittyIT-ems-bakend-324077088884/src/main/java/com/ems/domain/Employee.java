package com.ems.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ems.enums.MaritalStatus;
import com.ems.exception.ServiceException;
import com.ems.servicefinder.utils.DateTimeDeserializer;
import com.ems.servicefinder.utils.DateTimeSerializer;
import com.ems.wrappers.EmployeeRegistrationWrapper;
import com.ems.wrappers.NomineeWrapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue
	private Long id;

	private String empCode;

	private String password;

	private String recoveryPassword;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id", nullable = false)
	private CompanyCategory companyCategory;

	private String firstName;

	private String middleName;

	private String lastName;

	private String fatherName;

	private String gender;

	private Date dob;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "employee", orphanRemoval = true)
	private Avatar avatar;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "designation_id", nullable = false)
	private Designation designation;

	private Date joiningDate;

	private Long managerId;

	@Size(max = 100, message = "Length of location(100) exceed.")
	private String location;

	@Size(max = 200, message = "Length of official Email(200) exceed.")
	private String officialEmail;
	
	@Size(max = 100, message = "Length of bank name(100) exceed.")
	private String bankName;

	private String bankAccountNumber;

	private String bankIfsc;

	@Size(max = 200, message = "Length of personal Email(200) exceed.")
	private String personalEmail;

	@Size(max = 500, message = "Length of present address(500) exceed.")
	private String presentAddress;

	@Size(max = 500, message = "Length of permanent address(500) exceed.")
	private String permanentAddress;

	private String primaryPhone;

	private String secondaryPhone;

	private String panNumber;

	private String uanNumber;

	private String aadhar;

	private String bloodGroup;

	private Boolean isActive;

	@LastModifiedBy
	private Long modifiedBy;

	@CreatedBy
	private Long createdBy;

	@LastModifiedDate
	private DateTime lastModified;

	@CreatedDate
	private DateTime creationDate;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", nullable = false)
	private Role role;

	private Boolean deleted;

	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	@Column(name = "resign_date")
	private DateTime resignationDate;

	@Column(name = "resign_reason")
	private String resignationReason;

	private boolean policyAccepted = false;

	@Size(max = 200, message = "Length of hobbies(200) exceed.")
	private String hobbies;
	
	@Size(max = 100, message = "Length of nationality(100) exceed.")
	private String nationality;
	
	private String religion;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "marital_status")
	private MaritalStatus maritalStatus;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "nominee_id", nullable = true)
	private Nominee nominee;

	private DateTime levelChangedFromZero;
	
	@Column(name = "login_attempts")
	private int loginAttempts=0;
	@Column(name = "last_failed_login_attempt")
	private DateTime lastFailedLoginAttempt;
	@Column(name = "account_unlock_time")
	private DateTime accountUnlockTime;

	public int getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(int loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	public DateTime getLastFailedLoginAttempt() {
		return lastFailedLoginAttempt;
	}

	public void setLastFailedLoginAttempt(DateTime lastFailedLoginAttempt) {
		this.lastFailedLoginAttempt = lastFailedLoginAttempt;
	}

	public DateTime getAccountUnlockTime() {
		return accountUnlockTime;
	}

	public void setAccountUnlockTime(DateTime accountUnlockTime) {
		this.accountUnlockTime = accountUnlockTime;
	}

	/**
	 * Sets the Employee Id.
	 *
	 * @param id
	 *            the employee id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets The Employee Id.
	 *
	 * @return the Employee Id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets The Employee Code.
	 *
	 * @return the Employee Code
	 */
	public String getEmpCode() {
		return empCode;
	}

	/**
	 * Sets the Employee Code.
	 *
	 * @param id
	 *            the employee Code
	 */
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	/**
	 * Sets the Employee DOB.
	 *
	 * @param id
	 *            the employee DOB
	 */
	public void setDob(Date dob) {
		this.dob = dob;
	}

	/**
	 * Sets the Employee Joining Date.
	 *
	 * @param id
	 *            the employee Joining Date
	 */
	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	/**
	 * Sets the Employee password.
	 *
	 * @param password
	 *            the employee password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets The Employee password.
	 *
	 * @return the Employee password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets The Employee recovery password.
	 *
	 * @return the Employee recovery password
	 */
	public String getRecoveryPassword() {
		return recoveryPassword;
	}

	/**
	 * Sets the Employee recovery password.
	 *
	 * @param password
	 *            the employee recovery password
	 */
	public void setRecoveryPassword(String recoveryPassword) {
		this.recoveryPassword = recoveryPassword;
	}

	/**
	 * Sets the CompanyCategory.
	 *
	 * @param companyCategory
	 *            the Company Category
	 */
	public void setCompanyCategory(CompanyCategory companyCategory) {
		this.companyCategory = companyCategory;
	}

	/**
	 * Gets The Company Category.
	 *
	 * @return the CompanyCategory
	 */
	public CompanyCategory getCompanyCategory() {
		return companyCategory;
	}

	/**
	 * Sets the Employee First Name.
	 *
	 * @param firstName
	 *            the employee First Name
	 */
	public void setFirstName(String firstName) {
		if (firstName != null)
			this.firstName = firstName.trim();
		this.firstName = firstName;
	}

	/**
	 * Gets The Employee First Name.
	 *
	 * @return the Employee First Name
	 */
	public String getFirstName() {
		if (firstName != null)
			return firstName.trim();
		return firstName;
	}

	/**
	 * Sets the Employee Middle Name.
	 *
	 * @param middleName
	 *            the employee Middle Name
	 */
	public void setMiddleName(String middleName) {
		if (middleName != null)
			this.middleName = middleName.trim();
		this.middleName = middleName;
	}

	/**
	 * Gets The Employee Middle Name.
	 *
	 * @return the Employee Middle Name
	 */
	public String getMiddleName() {
		if (middleName != null)
			return middleName.trim();

		return middleName;
	}

	/**
	 * Sets the Employee Last name.
	 *
	 * @param lastName
	 *            the employee Last name
	 */
	public void setLastName(String lastName) {
		if (lastName != null)
			this.lastName = lastName.trim();
		this.lastName = lastName;
	}

	/**
	 * Gets The Employee Last name.
	 *
	 * @return the Employee Last name
	 */
	public String getLastName() {
		if (lastName != null)
			return lastName.trim();
		return lastName;
	}

	/**
	 * Gets The Employee Last name.
	 *
	 * @return the Employee Last name
	 */
	public String getFullName() {

		String fullName = this.firstName;

		fullName = fullName.trim() + (this.middleName != null ? (" " + this.middleName) : "");

		fullName = fullName.trim() + (this.lastName != null ? (" " + this.lastName) : "");

		return fullName;
	}

	/**
	 * Sets the Employee Father Name.
	 *
	 * @param fatherName
	 *            the employee Father Name
	 */
	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	/**
	 * Gets The Employee Father Name.
	 *
	 * @return the Employee Father Name
	 */
	public String getFatherName() {
		return fatherName;
	}

	/**
	 * Sets the Employee gender.
	 *
	 * @param gender
	 *            the employee gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Gets The Employee gender.
	 *
	 * @return the Employee gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets the Employee dob.
	 *
	 * @param dob
	 *            the employee dob
	 */

	@JsonDeserialize(using = com.ems.servicefinder.utils.DateTimeDeserializer.class)
	public void setDob(DateTime dob) {
		if (dob != null) {
			DateTime dt = new DateTime();
			this.dob = dt.withDate(dob.getYear(), dob.getMonthOfYear(), dob.getDayOfMonth()).toDate();
		}
	}

	/**
	 * Gets The Employee dob.
	 *
	 * @return the Employee dob
	 */
	public Date getDob() {
		return dob;
	}

	/**
	 * Sets the Employee Designation.
	 *
	 * @param Designation
	 *            the Employee Designation
	 */
	public Designation getDesignation() {
		return designation;
	}

	/**
	 * Gets The Employee Designation .
	 *
	 * @return the Employee Designation
	 */
	public void setDesignation(Designation designation) {
		this.designation = designation;
	}

	/**
	 * Sets the Employee Joining Date.
	 *
	 * @param joiningDate
	 *            the employee Joining Date
	 */
	@JsonDeserialize(using = com.ems.servicefinder.utils.DateTimeDeserializer.class)
	public void setJoiningDate(DateTime joiningDate) {
		if (joiningDate != null) {
			DateTime dt = new DateTime();
			this.joiningDate = dt
					.withDate(joiningDate.getYear(), joiningDate.getMonthOfYear(), joiningDate.getDayOfMonth())
					.toDate();
		}
	}

	/**
	 * Gets The Employee Joining Date.
	 *
	 * @return the Employee Joining Date
	 */
	public Date getJoiningDate() {
		return joiningDate;
	}

	/**
	 * Sets the Employee Manager Id.
	 *
	 * @param managerId
	 *            the employee Manager id
	 */
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	/**
	 * Gets The Employee Manager Id.
	 *
	 * @return the Employee Manager Id
	 */
	public Long getManagerId() {
		return managerId;
	}

	/**
	 * Sets the Employee location.
	 *
	 * @param location
	 *            the employee location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Gets The Employee location.
	 *
	 * @return the Employee location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the Employee Official Email.
	 *
	 * @param officialEmail
	 *            the employee officialEmail
	 */
	public void setOfficialEmail(String officialEmail) {
		this.officialEmail = officialEmail;
	}

	/**
	 * Gets The Employee Official Email.
	 *
	 * @return the Employee OfficialEmail
	 */
	public String getOfficialEmail() {
		return officialEmail;
	}

	/**
	 * @return if employee deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	/**
	 * Sets the Employee bank Name.
	 *
	 * @param bankName
	 *            the employee bankName
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * Gets The Employee bank Name.
	 *
	 * @return the Employee bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * Sets the Employee bank Account Number.
	 *
	 * @param bankAccountNumber
	 *            the employee id
	 */
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	/**
	 * Gets The Employee bank Account Number.
	 *
	 * @return the Employee bankAccountNumber
	 */
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}

	/**
	 * Sets the Employee personal Email.
	 *
	 * @param personalEmail
	 *            the employee personalEmail
	 */
	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	/**
	 * Gets The Employee personal Email.
	 *
	 * @return the Employee personalEmail
	 */
	public String getPersonalEmail() {
		return personalEmail;
	}

	/**
	 * Sets the Employee bank Ifsc.
	 *
	 * @param bankIfsc
	 *            the employee bankIfsc
	 */
	public void setBankIfsc(String bankIfsc) {
		this.bankIfsc = bankIfsc;
	}

	/**
	 * Gets The Employee bank Ifsc.
	 *
	 * @return the Employee bankIfsc
	 */
	public String getBankIfsc() {
		return bankIfsc;
	}

	/**
	 * Gets The Employee present Address.
	 *
	 * @return the Employee presentAddress
	 */
	public String getPresentAddress() {
		return presentAddress;
	}

	/**
	 * Sets the Employee present Address.
	 *
	 * @param presentAddress
	 *            the employee presentAddress
	 */
	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}

	/**
	 * Sets the Employee permanent Address.
	 *
	 * @param permanentAddress
	 *            the employee permanentAddress
	 */
	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	/**
	 * Gets The Employee permanent Address.
	 *
	 * @return the Employee permanentAddress
	 */
	public String getPermanentAddress() {
		return permanentAddress;
	}

	/**
	 * Sets the Employee primary Phone.
	 *
	 * @param primaryPhone
	 *            the employee primaryPhone
	 */
	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}

	/**
	 * Gets The Employee primary Phone.
	 *
	 * @return the Employee primaryPhone
	 */
	public String getPrimaryPhone() {
		return primaryPhone;
	}

	/**
	 * Sets the Employee secondary Phone.
	 *
	 * @param secondaryPhone
	 *            the employee secondaryPhone
	 */
	public void setSecondaryPhone(String secondaryPhone) {
		this.secondaryPhone = secondaryPhone;
	}

	/**
	 * Gets The Employee secondary Phone.
	 *
	 * @return the Employee secondaryPhone
	 */
	public String getSecondaryPhone() {
		return secondaryPhone;
	}

	/**
	 * Sets the Employee pan Number.
	 *
	 * @param panNumber
	 *            the employee pan Number
	 */
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	/**
	 * Gets The Employee pan Number.
	 *
	 * @return the Employee panNumber
	 */
	public String getPanNumber() {
		return panNumber;
	}

	/**
	 * Sets the Employee uan Number.
	 *
	 * @param uanNumber
	 *            the employee uanNumber
	 */
	public void setUanNumber(String uanNumber) {
		this.uanNumber = uanNumber;
	}

	/**
	 * Gets The Employee uan Number.
	 *
	 * @return the Employee uanNumber
	 */
	public String getUanNumber() {
		return uanNumber;
	}

	/**
	 * Gets The Employee uan Number.
	 *
	 * @return the Employee uanNumber
	 */
	public String getUanNumberString() {
		return uanNumber == null ? "" : uanNumber;
	}

	/**
	 * Sets the Employee aadhar.
	 *
	 * @param aadhar
	 *            the aadhar number
	 */
	public void setAadhar(String aadhar) {
		this.aadhar = aadhar;
	}

	/**
	 * Gets The Employee aadhar.
	 *
	 * @return the Employee aadhar
	 */
	public String getAadhar() {
		return aadhar;
	}

	/**
	 * Sets the Employee blood Group.
	 *
	 * @param bloodGroup
	 *            the employee bloodGroup
	 */
	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	/**
	 * Gets The Employee bloodGroup.
	 *
	 * @return the Employee bloodGroup
	 */
	public String getBloodGroup() {
		return bloodGroup;
	}

	/**
	 * Sets the Employee isActive.
	 *
	 * @param isActive
	 *            the employee isActive
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * Gets The Employee isActive.
	 *
	 * @return the Employee isActive
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	public Boolean isActive() {
		return isActive;
	}

	/**
	 * Sets the Employee modifiedBy.
	 *
	 * @param modifiedBy
	 *            the employee modifiedBy
	 */
	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * Gets The Employee modifiedBy.
	 *
	 * @return the Employee modifiedBy
	 */
	public Long getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * Sets the Employee createdBy.
	 *
	 * @param createdBy
	 *            the employee createdBy
	 */
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets The Employee createdBy.
	 *
	 * @return the Employee createdBy
	 */
	public Long getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the Employee creation Date.
	 *
	 * @param creationDate
	 *            the employee creationDate
	 */
	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets The Employee Official Email.
	 *
	 * @return the Employee creationDate
	 */
	public DateTime getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the Employee last Modified.
	 *
	 * @param lastModified
	 *            the employee lastModified
	 */
	public void setLastModified(DateTime lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * Gets The Employee last Modified.
	 *
	 * @return the Employee lastModified
	 */
	public DateTime getLastModified() {
		return lastModified;
	}

	/**
	 * Sets the Employee creation role.
	 *
	 * @param role
	 *            the employee role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * Gets The Employee role.
	 *
	 * @return the Employee role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Gets The Employee Avatar.
	 *
	 * @return the Employee Avatar
	 */
	public Avatar getAvatar() {
		return avatar;
	}

	/**
	 * Sets the Employee Avatar.
	 *
	 * @param Avatar
	 *            the employee Avatar
	 */
	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public DateTime getResignationDate() {
		return resignationDate;
	}

	public void setResignationDate(DateTime resignationDate) {
		this.resignationDate = resignationDate;
	}

	public String getResignationReason() {
		return resignationReason;
	}

	public void setResignationReason(String resignationReason) {
		this.resignationReason = resignationReason;
	}

	/**
	 * @return the policyAccepted
	 */
	public boolean isPolicyAccepted() {
		return policyAccepted;
	}

	/**
	 * @param policyAccepted
	 *            the policyAccepted to set
	 */
	public void setPolicyAccepted(boolean policyAccepted) {
		this.policyAccepted = policyAccepted;
	}

	/**
	 * @return the hobbies
	 */
	public String getHobbies() {
		return hobbies;
	}

	/**
	 * @param hobbies
	 *            the hobbies to set
	 */
	public void setHobbies(String hobbies) {
		this.hobbies = hobbies;
	}

	/**
	 * @return the nationality
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * @param nationality
	 *            the nationality to set
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return the religion
	 */
	public String getReligion() {
		return religion;
	}

	/**
	 * @param religion
	 *            the religion to set
	 */
	public void setReligion(String religion) {
		this.religion = religion;
	}

	/**
	 * @return the maritalStatus
	 */
	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * @param maritalStatus
	 *            the maritalStatus to set
	 */
	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * @return the nominee
	 */
	public Nominee getNominee() {
		return nominee;
	}

	/**
	 * @param nominee
	 *            the nominee to set
	 */
	public void setNominee(Nominee nominee) {
		this.nominee = nominee;
	}

	/**
	 * Get the date when level of the employee changed from zero.
	 * 
	 * @return
	 */
	public DateTime getLevelChangedFromZero() {
		return levelChangedFromZero;
	}

	/**
	 * Set the date when level of the employee changed from zero.
	 * 
	 * @param levelChangedFromZero
	 */
	public void setLevelChangedFromZero(DateTime levelChangedFromZero) {
		this.levelChangedFromZero = levelChangedFromZero;
	}

	public void setNomineeDetails(NomineeWrapper nomineeWrapper) {
		if (isNomineeWrapperEmpty(nomineeWrapper)) {
			this.nominee = null;
		} else {
			if (this.nominee != null) {
				this.nominee.setNomineeDetails(nomineeWrapper);
			} else {
				this.nominee = new Nominee(nomineeWrapper);
			}
		}
	}

	public boolean isNomineeWrapperEmpty(NomineeWrapper nomineeWrapper) {
		return (nomineeWrapper == null) || (StringUtils.isBlank(nomineeWrapper.getNomineeName())
				&& StringUtils.isBlank(nomineeWrapper.getNomineeEmail())
				&& StringUtils.isBlank(nomineeWrapper.getNomineePermanentAddress())
				&& StringUtils.isBlank(nomineeWrapper.getNomineePhone())
				&& StringUtils.isBlank(nomineeWrapper.getNomineeRelation()));
	}

	public Employee() {

	}

	public Employee(EmployeeRegistrationWrapper employee) throws ServiceException {
		setEmployeeDetail(employee);
		this.password = employee.getEncodedPassword();
	}

	public void setEmployeeDetail(EmployeeRegistrationWrapper employee) {
		this.id = employee.getId();

		this.empCode = employee.getEmpCode();

		this.firstName = employee.getFirstName();

		this.middleName = employee.getMiddleName();

		this.lastName = employee.getLastName();

		this.fatherName = employee.getFatherName();

		this.gender = employee.getGender();

		this.setDob(employee.getDob());

		this.setJoiningDate(employee.getJoiningDate());

		this.managerId = employee.getManagerId();

		this.location = employee.getLocation();

		this.officialEmail = employee.getOfficialEmail();

		this.bankName = employee.getBankName();

		this.bankAccountNumber = employee.getBankAccountNumber();

		this.bankIfsc = employee.getBankIfsc();

		this.personalEmail = employee.getPersonalEmail();

		this.presentAddress = employee.getPresentAddress();

		this.permanentAddress = employee.getPermanentAddress();

		this.primaryPhone = employee.getPrimaryPhone();

		this.secondaryPhone = employee.getSecondaryPhone();

		this.panNumber = employee.getPanNumber();

		this.uanNumber = employee.getUanNumber();

		this.aadhar = employee.getAadhar();

		this.bloodGroup = employee.getBloodGroup();

		this.isActive = employee.getIsActive();

		this.deleted = employee.isDeleted();

		this.hobbies = employee.getHobbies();
		this.nationality = employee.getNationality();
		this.religion = employee.getReligion();
		this.maritalStatus = employee.getMaritalStatus();
	}

}
