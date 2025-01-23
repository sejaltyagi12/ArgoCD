package com.ems.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.ems.wrappers.NomineeWrapper;

@Entity
@Table(name = "nominee")
public class Nominee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nominee_id")
	private long nomineeId;

	private String nomineeName;
	private String nomineeRelation;
	private String nomineePhone;
	
	@Size(max = 200, message = "Length of nominee email(200) exceed.")
	private String nomineeEmail;
	
	@Size(max = 500, message = "Length of nominee permanent address(500) exceed.")
	private String nomineePermanentAddress;

	public Nominee() {
	}

	public Nominee(NomineeWrapper nomineeWrapper) {
		setNomineeDetails(nomineeWrapper);
	}

	public void setNomineeDetails(NomineeWrapper nomineeWrapper) {
		this.nomineeName = nomineeWrapper.getNomineeName();
		this.nomineeRelation = nomineeWrapper.getNomineeRelation();
		this.nomineePhone = nomineeWrapper.getNomineePhone();
		this.nomineeEmail = nomineeWrapper.getNomineeEmail();
		this.nomineePermanentAddress = nomineeWrapper.getNomineePermanentAddress();
	}

	/**
	 * @return the nomineeName
	 */
	public String getNomineeName() {
		return nomineeName;
	}

	/**
	 * @param nomineeName
	 *            the nomineeName to set
	 */
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}

	/**
	 * @return the nomineeId
	 */
	public long getNomineeId() {
		return nomineeId;
	}

	/**
	 * @param nomineeId
	 *            the nomineeId to set
	 */
	public void setNomineeId(long nomineeId) {
		this.nomineeId = nomineeId;
	}

	/**
	 * @return the nomineeRelation
	 */
	public String getNomineeRelation() {
		return nomineeRelation;
	}

	/**
	 * @param nomineeRelation
	 *            the nomineeRelation to set
	 */
	public void setNomineeRelation(String nomineeRelation) {
		this.nomineeRelation = nomineeRelation;
	}

	/**
	 * @return the nomineePhone
	 */
	public String getNomineePhone() {
		return nomineePhone;
	}

	/**
	 * @param nomineePhone
	 *            the nomineePhone to set
	 */
	public void setNomineePhone(String nomineePhone) {
		this.nomineePhone = nomineePhone;
	}

	/**
	 * @return the nomineeEmail
	 */
	public String getNomineeEmail() {
		return nomineeEmail;
	}

	/**
	 * @param nomineeEmail
	 *            the nomineeEmail to set
	 */
	public void setNomineeEmail(String nomineeEmail) {
		this.nomineeEmail = nomineeEmail;
	}

	/**
	 * @return the nomineePermanentAddress
	 */
	public String getNomineePermanentAddress() {
		return nomineePermanentAddress;
	}

	/**
	 * @param nomineePermanentAddress
	 *            the nomineePermanentAddress to set
	 */
	public void setNomineePermanentAddress(String nomineePermanentAddress) {
		this.nomineePermanentAddress = nomineePermanentAddress;
	}

}
