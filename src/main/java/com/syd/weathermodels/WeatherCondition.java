package com.syd.weathermodels;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Autowired;


public class WeatherCondition {
	@Autowired
	private Wunderground wunderground;
	
	public Wunderground getWunderground() {
		return wunderground;
	}

	public void setWunderground(Wunderground wunderground) {
		this.wunderground = wunderground;
	}

	public WeatherCondition(){
	}
	
	public Weather getWeather(String city){
		Weather weather = null;
		wunderground.openUrl(city);
		if (!wunderground.isError()){
			wunderground.parseJson();
			if (!wunderground.isError()){
				weather = wunderground.getWeather();
			}
		}
		return weather;
	}
}
