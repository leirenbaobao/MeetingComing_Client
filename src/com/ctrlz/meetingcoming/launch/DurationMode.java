package com.ctrlz.meetingcoming.launch;

public class DurationMode {
	
	private String duration_string;
	
	private String duration_number;
	public String getDuration_number() {
		return duration_number;
	}

	public void setDuration_number(String duration_number) {
		this.duration_number = duration_number;
	}

	private boolean isChoosed;

	public boolean isChoosed() {
		return isChoosed;
	}

	public void setChoosed(boolean isChoosed) {
		this.isChoosed = isChoosed;
	}

	public String getDuration_string() {
		return duration_string;
	}

	public void setDuration_string(String duration_string) {
		this.duration_string = duration_string;
	}

}
