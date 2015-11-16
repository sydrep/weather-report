

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.syd.weathermodels.Weather;

public class ParseJsonTest {
	String urlStr = "http://api.wunderground.com/api/00c370b16edd3922/conditions/q/AU";
	
	WundergroundWrapper wunderground;
	Weather weather;

	@Before
	public void setUp() throws Exception {
		wunderground = new WundergroundWrapper();
		weather = new Weather();
		wunderground.setWeather(weather);
	}

	@Test
	public final void ParseJsonTest() {
		String expected = "Mostly Cloudy";
		wunderground.parseJson();
		if (!wunderground.isError()){
			weather = wunderground.getWeather();
			assertEquals(expected, weather.getWeather());
		}
	}

}
