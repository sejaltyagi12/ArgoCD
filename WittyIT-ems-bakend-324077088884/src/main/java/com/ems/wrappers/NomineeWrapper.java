package com.ems.wrappers;

import com.ems.domain.Nominee;

public class NomineeWrapper {
	private String nomineeName;
	private String nomineeRelation;
	private String nomineePhone;
	private String nomineeEmail;
	private String nomineePermanentAddress;

	public NomineeWrapper() {
	}

	public NomineeWrapper(Nominee nominee) {
		this.nomineeName = nominee.getNomineeName();
		this.nomineeRelation = nominee.getNomineeRelation();
		this.nomineePhone = nominee.getNomineePhone();
		this.nomineeEmail = nominee.getNomineeEmail();
		this.nomineePermanentAddress = nominee.getNomineePermanentAddress();
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
