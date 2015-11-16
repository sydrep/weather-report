package com.syd.weathermodels;

public class Weather {
	private String message;
	private String city;
	private String updatedTime;
	private String weather;
	private String temperature;
	private String wind;
	
	/*
	 * Empty all attribute values
	 */
	public void resetAttributes(){
		setMessage("");
		setCity("");
		setUpdatedTime("");
		setWeather("");
		setTemperature("");
		setWind("");
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getWind() {
		return wind;
	}
	public void setWind(String wind) {
		this.wind = wind;
	}

}
