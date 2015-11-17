package com.syd.weathercontrollers;


import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.syd.weathermodels.Cities;
import com.syd.weathermodels.City;
import com.syd.weathermodels.Weather;
import com.syd.weathermodels.WeatherCondition;

@Controller
@RequestMapping("/weather")
public class WeatherController {
	final static String HINT = "Avaliable cities are " ;
	final static String WEATHER = "Weather in " ;

	@Autowired
	private WeatherCondition weatherCondition;
	@Autowired
	private Cities cities;

	
	@RequestMapping()
	public ModelAndView processWeather(@Valid City city, BindingResult result){
		
		ModelAndView mav = new ModelAndView("weather");
		// cities are populated once when the object is created and the values
		// are not populated again for efficiency.  However, if cities change
		// then the site needs to restart to reload the values.  Therefore, if
		// cities change often, it may be better to reload values here, or at
		// least defect if values have changed since the last request to determine
		// to reload or not.
		mav.addObject("citiesList", cities.getCities());	// used to populate SELECT-OPTIONS
		boolean isCorrectCity = cities.getCities().contains(city.getCityName());
		if (result.hasErrors() || !isCorrectCity){
			mav.addObject("message", HINT + cities.getCities().toString());
		}else{
			mav.addObject("message", WEATHER + city.getCityName());
			Weather weather = weatherCondition.getWeather(city.getCityName());
			mav.addObject("weather", weather);
		}
		
		return mav;
	}
}
