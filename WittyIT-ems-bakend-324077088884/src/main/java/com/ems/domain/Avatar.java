package com.ems.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

/**
 * Avatar is the user's picture.
 *
 * @author Avinash Tyagi
 */
@Entity
@Table(name = "avatar")
public class Avatar {
	
	@Id
    @GeneratedValue
    private long id;
    
    private String name;
    
    @Transient
    private MultipartFile avatarContent;
    
    @NotNull
	@OneToOne
    @JoinColumn(name="emp_id", nullable=false)
    private Employee employee;
    
    private String avatarUrl;
    
    @Transient
    private String avatarCompleteUrl;
    
	/**
	 * Gets  the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}

	
	/**
	 * Gets  the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets  the profile.
	 *
	 * @return the profile
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * Sets the profile.
	 *
	 * @param profile the new profile
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * Gets  the avatar content.
	 *
	 * @return the avatar content
	 */
	public MultipartFile getAvatarContent() {
		return avatarContent;
	}

	/**
	 * Sets the avatar content.
	 *
	 * @param avatarContent the new avatar content
	 */
	public void setAvatarContent(MultipartFile avatarContent) {
		this.avatarContent = avatarContent;
	}

	/**
	 * @return the avatarUrl
	 */
	public String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * @param avatarUrl the avatarUrl to set
	 */
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 * @return the avatarCompleteUrl
	 */
	public String getAvatarCompleteUrl() {
		return avatarCompleteUrl;
	}

	/**
	 * @param avatarCompleteUrl the avatarCompleteUrl to set
	 */
	public void setAvatarCompleteUrl(String avatarCompleteUrl) {
		this.avatarCompleteUrl = avatarCompleteUrl;
	}

}
