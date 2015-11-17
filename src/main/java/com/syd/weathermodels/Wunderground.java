package com.syd.weathermodels;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	private final static String OBSERVATION_EPOCH = "observation_epoch";
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

	private void setWeatherInfo(String attribute, String value) {
		switch (attribute) {
		case OBSERVATION_EPOCH:
			Date date = new Date(Long.parseLong(value)*1000);	//
			SimpleDateFormat sdf = new SimpleDateFormat("EEEE HH:mm a"); 
			weather.setUpdatedTime(sdf.format(date));
			break;
		case WEATHER:
			weather.setWeather(value);
			break;
		case TEMP:
			weather.setTemperature(value + TEMP_UNIT);
			break;
		case WIND_KPH:
			weather.setWind(value + WIND_UNIT);
			break;
		}
	}

	private void parseCurrentObservation(JsonNode node) {
		Iterator<String> fieldNames = node.fieldNames();
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			JsonNode fieldValue = node.get(fieldName);
			if (fieldValue.isObject()) {
				parseCurrentObservation(fieldValue);
			} else {
				if (fieldValue.isArray()) {
					String value = fieldValue.get(0).get("zmw").asText();
				} else {
					String value = fieldValue.asText();
					setWeatherInfo(fieldName, value);
				}
			}
		}
	}

	private String parseResults(JsonNode node) {
		Iterator<String> fieldNames = node.fieldNames();
		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			JsonNode fieldValue = node.get(fieldName);
			if (fieldValue.isObject()) {
				parseResults(fieldValue);
			} else {
				if (fieldValue.isArray()) {
					String value = fieldValue.get(0).get(ZWM).asText();
					return value;
				} else {
					String value = fieldValue.asText();
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

	private void reopenUrlWithUniqueLink(JsonNode jsonNode) {
		String zmw = parseResults(jsonNode);	// parse fieldname:results json field to get ZWM 
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
		try {
			root = mapper.readTree(getLink());
			JsonNode jsonNode = root.get(CURRENT_OBSERVATION);	// this field contains weather data
			if (null == jsonNode || jsonNode.isMissingNode()) { // if CURRENT_OBSERVATION is null may be the city name is non-unique
				jsonNode = root.get(RESPONSE);
				if (null == jsonNode || jsonNode.isMissingNode()) { //no RESPONSE, then error 
					weather.setMessage("Name: " + RESPONSE + " and " + CURRENT_OBSERVATION + " are missing.");
				 } else { //multiple city name founden URL using unique link
					reopenUrlWithUniqueLink(jsonNode); 
				}
			} else { // found a unique city weather data
				parseCurrentObservation(jsonNode);
			}
		} catch (IOException e) {
			e.printStackTrace();
			setError(true);
			weather.setMessage("Cannot parse json from URL site!");
		}
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
