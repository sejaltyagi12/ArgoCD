package com.ems.wrappers;

public class ChartData {

	private String name;
	
	private Long count;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public ChartData(String name, Long count) {
		this.name = name;
		this.count = count;
	}
	
	
}
