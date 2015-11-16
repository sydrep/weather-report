package com.syd.weathermodels;

import org.hibernate.validator.constraints.NotEmpty;

public class City {
	@NotEmpty(message="Please select a city!")
	private String cityName;

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

}
