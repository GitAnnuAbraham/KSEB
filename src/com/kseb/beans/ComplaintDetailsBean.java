package com.kseb.beans;

import java.util.Date;

public class ComplaintDetailsBean {

	int Id;
	String desc;
	Date loggedOn;
	String status;
	Date resolvedOn;
	String allocatedTo;
	int consumerId;

	public int getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(int consumerId) {
		this.consumerId = consumerId;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getLoggedOn() {
		return loggedOn;
	}

	public void setLoggedOn(Date loggedOn) {
		this.loggedOn = loggedOn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getResolvedOn() {
		return resolvedOn;
	}

	public void setResolvedOn(Date resolvedOn) {
		this.resolvedOn = resolvedOn;
	}

	public String getAllocatedTo() {
		return allocatedTo;
	}

	public void setAllocatedTo(String allocatedTo) {
		this.allocatedTo = allocatedTo;
	}

}
