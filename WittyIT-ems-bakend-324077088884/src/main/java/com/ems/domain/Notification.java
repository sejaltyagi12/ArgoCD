package com.ems.domain;


import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ems.servicefinder.utils.DateTimeDeserializer;
import com.ems.servicefinder.utils.DateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Notification {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NotNull
	private Long senderId;
	
	@NotNull
	private Long receiverId;

	@NotNull
	private String text;
	
	@NotNull
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="notification_type_id",nullable =false)
	private NotificationType notificationType;

	@NotNull
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime expiryDate = new DateTime().minusDays(1);
	
	@JsonSerialize(using=DateTimeSerializer.class)
    @JsonDeserialize(using=DateTimeDeserializer.class)
	private DateTime actualExpiryDate;
	
	@NotNull
	private Boolean active = true ;
	
	@CreatedDate
	private DateTime creationDate;
	
	@JsonIgnore
	@LastModifiedDate
	private DateTime modifiedDate;
	
	@JsonIgnore
	@CreatedBy
	private Long createdBy;
	
	@JsonIgnore
	@LastModifiedBy
	private Long modifiedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	public DateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(DateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public DateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(DateTime creationDate) {
		this.creationDate = creationDate;
	}

	public DateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(DateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(Long modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public DateTime getActualExpiryDate() {
		return actualExpiryDate;
	}

	public void setActualExpiryDate(DateTime actualExpiryDate) {
		this.actualExpiryDate = actualExpiryDate;
	}

	public Notification(Long senderId, Long receiverId, String text,
			DateTime expiryDate , NotificationType notificationType) {
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.text = text;
		this.expiryDate = expiryDate;
		this.notificationType = notificationType;
	}

	public Notification() {
	}


}
