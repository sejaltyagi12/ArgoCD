package com.ems.wrappers;

import org.joda.time.DateTime;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.ems.domain.Nominee;
import com.ems.enums.MaritalStatus;
import com.ems.servicefinder.utils.DateTimeDeserializer;
import com.ems.servicefinder.utils.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class EmployeeRegistrationWrapper {

	private Long id;

	private String empCode;

	private String password;

	private Integer companyId;

	private String firstName;

	private String middleName;

	private String lastName;

	private String fatherName;

	private String gender;

	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime dob;

	private String departmentName;

	private Integer deptId;

	private Long designationId;

	private String designation;

	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime joiningDate;

	private Long managerId;

	private String location;

	private String officialEmail;

	private String bankName;

	private String bankAccountNumber;

	private String bankIfsc;

	private String personalEmail;

	private String presentAddress;

	private String permanentAddress;

	private String primaryPhone;

	private String secondaryPhone;

	private String panNumber;

	private String uanNumber;

	private String token;

	private String aadhar;

	private String bloodGroup;

	private Boolean isActive;

	private String role;

	private Integer roleId;

	private Boolean deleted;

	@JsonSerialize(using = DateTimeSerializer.class)
	@JsonDeserialize(using = DateTimeDeserializer.class)
	private DateTime resignationDate;

	private String resignationReason;

	private String hobbies;
	private String nationality;
	private String religion;
	private MaritalStatus maritalStatus;

	private NomineeWrapper nomineeDetails;

	/**
	 * Gets The Employee id.
	 *
	 * @return the Employee id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the Employee id.
	 *
	 * @param id
	 *            the employee id
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * Gets The Employee Code.
	 *
	 * @return the Employee Code
	 */
	public String getEmpCode() {
		return empCode;
	}

	/**
	 * Sets the Employee password.
	 *
	 * @param id
	 *            the employee id
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getEncodedPassword() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		return passwordEncoder.encode(password);
		// this.password = password;
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
	 * Sets the Employee companyId.
	 *
	 * @param id
	 *            the employee companyId
	 */
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	/**
	 * Gets The Employee companyId.
	 *
	 * @return the Employee companyId
	 */
	public Integer getCompanyId() {
		return companyId;
	}

	/**
	 * Sets the Employee First Name.
	 *
	 * @param id
	 *            the employee First Name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets The Employee First Name.
	 *
	 * @return the Employee First Name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the Employee Middle Name.
	 *
	 * @param id
	 *            the employee Middle Name
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Gets The Employee Middle Name.
	 *
	 * @return the Employee Middle Name
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * Sets the Employee Last name.
	 *
	 * @param id
	 *            the employee Last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets The Employee Last name.
	 *
	 * @return the Employee Last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets the Employee Father Name.
	 *
	 * @param id
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
	 * @param id
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
	 * @param id
	 *            the employee dob
	 */
	public void setDob(DateTime dob) {
		this.dob = dob;
	}

	/**
	 * Gets The Employee dob.
	 *
	 * @return the Employee dob
	 */
	public DateTime getDob() {
		return dob;
	}

	/**
	 * Sets the Employee Dapartment Id.
	 *
	 * @param id
	 *            the employee id
	 */
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	/**
	 * Gets The Employee Dapartment Id.
	 *
	 * @return the Employee Dapartment Id
	 */
	public Integer getDeptId() {
		return deptId;
	}

	/**
	 * Gets The Employee Designation.
	 *
	 * @return the Employee Designation
	 */
	public String getDesignation() {
		return designation;
	}

	/**
	 * Sets the Employee Designation.
	 *
	 * @param Designation
	 */
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * Gets The Employee Role.
	 *
	 * @return the Employee Role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the Employee Role.
	 *
	 * @param Role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Sets the Employee Designation Id.
	 *
	 * @param id
	 *            the employee id
	 */
	public void setDesignationId(Long designationId) {
		this.designationId = designationId;
	}

	/**
	 * Gets The Employee Designation Id.
	 *
	 * @return the Employee Designation Id
	 */
	public Long getDesignationId() {
		return designationId;
	}

	/**
	 * Sets the Employee Joining Date.
	 *
	 * @param id
	 *            the employee Joining Date
	 */
	public void setJoiningDate(DateTime joiningDate) {
		this.joiningDate = joiningDate;
	}

	/**
	 * Gets The Employee Joining Date.
	 *
	 * @return the Employee Joining Date
	 */
	public DateTime getJoiningDate() {
		return joiningDate;
	}

	/**
	 * Sets the Employee Manager Id.
	 *
	 * @param id
	 *            the employee id
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
	 * @param id
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
	 * @param id
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
	 * @return the deleted
	 */
	public Boolean getDeleted() {
		return deleted;
	}

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

	/**
	 * Sets the Employee bank Name.
	 *
	 * @param id
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
	 * @param id
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
	 * @param id
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
	 * @param id
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
	 * @param id
	 *            the employee presentAddress
	 */
	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}

	/**
	 * Sets the Employee permanent Address.
	 *
	 * @param id
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
	 * @param id
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
	 * @param id
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
	 * @param id
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
	 * @param id
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
	 * Sets the Employee aadhar.
	 *
	 * @param id
	 *            the employee id
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
	 * @param id
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
	 * @param id
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

	/**
	 * Sets the Employee Department Name.
	 *
	 * @param id
	 *            the employee Department Name
	 */
	public String getDepartmentName() {
		return departmentName;
	}

	/**
	 * Gets The Employee Department Name.
	 *
	 * @return the Employee Department Name
	 */
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	/**
	 * Sets the Employee creation role Id.
	 *
	 * @param id
	 *            the employee roleId
	 */
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	/**
	 * Gets The Employee role Id.
	 *
	 * @return the Employee roleId
	 */
	public Integer getRoleId() {
		return roleId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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
	 * @return the nomineeDetails
	 */
	public NomineeWrapper getNomineeDetails() {
		return nomineeDetails;
	}

	/**
	 * @param nomineeDetails
	 *            the nomineeDetails to set
	 */
	public void setNomineeDetails(NomineeWrapper nomineeDetails) {
		this.nomineeDetails = nomineeDetails;
	}

	public EmployeeRegistrationWrapper() {

	}

	@Override
	public String toString() {
		return "EmployeeRegistrationWrapper [id=" + id + ", empCode=" + empCode + ", password=" + password
				+ ", companyId=" + companyId + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName="
				+ lastName + ", fatherName=" + fatherName + ", gender=" + gender + ", dob=" + dob + ", departmentName="
				+ departmentName + ", deptId=" + deptId + ", designationId=" + designationId + ", designation="
				+ designation + ", joiningDate=" + joiningDate + ", managerId=" + managerId + ", location=" + location
				+ ", officialEmail=" + officialEmail + ", bankName=" + bankName + ", bankAccountNumber="
				+ bankAccountNumber + ", bankIfsc=" + bankIfsc + ", personalEmail=" + personalEmail
				+ ", presentAddress=" + presentAddress + ", permanentAddress=" + permanentAddress + ", primaryPhone="
				+ primaryPhone + ", secondaryPhone=" + secondaryPhone + ", panNumber=" + panNumber + ", uanNumber="
				+ uanNumber + ", token=" + token + ", aadhar=" + aadhar + ", bloodGroup=" + bloodGroup + ", isActive="
				+ isActive + ", role=" + role + ", roleId=" + roleId + ", deleted=" + deleted + ", resignationDate="
				+ resignationDate + ", resignationReason=" + resignationReason + ", hobbies=" + hobbies
				+ ", nationality=" + nationality + ", religion=" + religion + ", maritalStatus=" + maritalStatus
				+ ", nomineeDetails=" + nomineeDetails + "]";
	}

	
}
