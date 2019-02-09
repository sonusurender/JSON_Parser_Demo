package com.json_demo;

public class JSONData_Model {
	/** Local Json Parser Getter and Setter **/
	private String name, location;
	private String[] phoneNumber;

	public JSONData_Model(String name, String location, String[] phoneNumber) {
		this.name = name;
		this.location = location;
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

	public String[] getPhoneNumber() {
		return phoneNumber;
	}

	/** ---------------------------------------------- **/

	/** Sever Json Parser Getter and Setter **/
	private String date, status, title;

	public JSONData_Model(String title, String status, String date) {
		this.date = date;
		this.status = status;
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public String getTitle() {
		return title;
	}

	public String getStatus() {
		return status;
	}
	/** ----------------------------------------------------- **/
}
