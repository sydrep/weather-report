package com.syd.weathermodels;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Wunderground {
	// Wunderground json format fieldnames
	private final static String SUFFIX = ".json";
	private final static String RESPONSE = "response";
	private final static String ZWM = "zmw"; // when multiple city names occur
												// use this to get weather data
	private final static String CURRENT_OBSERVATION = "current_observation";
	private final static String RESULTS = "results";
	private final static String OBSERVATION_TIME_RFC822 = "observation_time_rfc822";
	private final static String WEATHER = "weather";
	private final static String TEMP = "temp_c";
	private final static String WIND_KPH = "wind_kph";
	private final static String TEMP_UNIT = "\u00b0C";//\u2103 does not print on the page;
	private final static String WIND_UNIT = "km/h";

	@Autowired
	private Weather weather;

	private boolean isError = false;

	// populate the values using @Value with a properties file with
	// property placeholder string
	@Value("${urlStr}")
	private String urlStr;

	private URL link = null;

	public void openUrl(String city) {
		String fullPath = urlStr + "/" + city + SUFFIX;
		try {
			link = new URL(fullPath);
			weather.resetAttributes();
			weather.setCity(city);
			weather.setMessage("");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			setError(true);
			weather.setMessage("Cannot access site, please check URL correctness! URL: " + fullPath);
		}
	}

	private void setWeatherInfo(String attribute, String value, Output op) {
		switch (attribute) {
		case OBSERVATION_TIME_RFC822:
			op.redirect("************* : " + attribute);
			weather.setUpdatedTime(value);
			break;
		case WEATHER:
			op.redirect("************* : " + attribute);
			weather.setWeather(value);
			break;
		case TEMP:
			op.redirect("************* : " + attribute);
			weather.setTemperature(value + TEMP_UNIT);
			break;
		case WIND_KPH:
			op.redirect("************* : " + attribute);
			weather.setWind(value + WIND_UNIT);
			break;
		}
	}

	private void parseCurrentObservation(JsonNode node, Output op) {
		Iterator<String> fieldNames = node.fieldNames();
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			JsonNode fieldValue = node.get(fieldName);
			if (fieldValue.isObject()) {
				op.redirect(fieldName + " : (obj)-------------------------------------------");
				parseCurrentObservation(fieldValue, op);
			} else {
				if (fieldValue.isArray()) {
					String value = fieldValue.get(0).get("zmw").asText();
					op.redirect(fieldName + " : " + value);
				} else {
					String value = fieldValue.asText();
					setWeatherInfo(fieldName, value, op);
					op.redirect(fieldName + " : " + value);
				}
			}
		}
	}

	private String parseResults(JsonNode node, Output op) {
		Iterator<String> fieldNames = node.fieldNames();
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			JsonNode fieldValue = node.get(fieldName);
			if (fieldValue.isObject()) {
				op.redirect(fieldName + " : (obj)-------------------------------------------");
				parseResults(fieldValue, op);
			} else {
				if (fieldValue.isArray()) {
					String value = fieldValue.get(0).get(ZWM).asText();
					op.redirect(fieldName + " : " + value);
					return value;
				} else {
					String value = fieldValue.asText();
					op.redirect(fieldName + " : " + value);
				}
			}
		}
		return null;
	}

	private void openUrlByZmw(String zmw) {
		String fullPath = urlStr + "/" + ZWM + ":" + zmw + SUFFIX;
		try {
			link = new URL(fullPath);
			weather.setMessage("");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			setError(true);
			weather.setMessage("Cannot access site, please check URL correctness! URL: " + fullPath);
		}
	}

	private void reopenUrlWithUniqueLink(JsonNode jsonNode, Output op) {
		String zmw = parseResults(jsonNode, op);	// parse fieldname:results json field to get ZWM 
		if (null == zmw) {	// found a unique link
			setError(true);
			weather.setMessage("Cannot locate city!");
		}else{
			openUrlByZmw(zmw);
			if (!this.isError()) {
				parseJson();
			}
		}
	}

	public void parseJson() {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		Output op = new Output("out.txt");
		try {
			root = mapper.readTree(getLink());
			JsonNode jsonNode = root.get(CURRENT_OBSERVATION);	// this field contains weather data
			if (null == jsonNode || jsonNode.isMissingNode()) { // if CURRENT_OBSERVATION is null may be the city name is non-unique
				jsonNode = root.get(RESPONSE);
				if (null == jsonNode || jsonNode.isMissingNode()) { //no RESPONSE, then error 
					op.redirect("Name: " + RESPONSE + " and " + CURRENT_OBSERVATION + " are missing."); setError(true); 
					weather.setMessage("Name: " + RESPONSE + " and " + CURRENT_OBSERVATION + " are missing.");
				 } else { //multiple city name found, open URL using unique link
					reopenUrlWithUniqueLink(jsonNode, op); 
				}
			} else { // found a unique city weather data
				parseCurrentObservation(jsonNode, op);
			}
		} catch (IOException e) {
			e.printStackTrace();
			op.redirect("Cannot parse json from URL site!");
			setError(true);
			weather.setMessage("Cannot parse json from URL site!");
		}
		op.close();
	}

	// To resolve ${} in @Value
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
	}

	public String getUrlStr() {
		return urlStr;
	}

	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}

	public Wunderground() {
	}

	public URL getLink() {
		return link;
	}

}
