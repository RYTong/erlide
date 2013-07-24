package com.rytong.ui.internal.templates;

public class AppTemplate {
	private String name;
	private String label;
	private String description;
	private String location;

	public AppTemplate(String name, String label, String desc, String loc) {
		this.name = name;
		this.label = label;
		this.description = desc;
		this.location = loc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
