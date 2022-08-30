package com.lucasilva.spaceprobe.resources.exceptions;

import java.io.Serializable;

public class StandardError implements Serializable {

	private static final long serialVersionUID = 18412444L;

	private Integer status;
	private String code;
	private String details;
	private Long timesStamp;

	public StandardError(Integer status, String code, String details, Long timesStamp) {
		this.status = status;
		this.code = code;
		this.details = details;
		this.timesStamp = timesStamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Long getTimesStamp() {
		return timesStamp;
	}

	public void setTimesStamp(Long timesStamp) {
		this.timesStamp = timesStamp;
	}
}