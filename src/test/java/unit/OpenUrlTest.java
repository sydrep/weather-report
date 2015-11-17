package unit;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.syd.weathermodels.Weather;
import com.syd.weathermodels.Wunderground;

public class OpenUrlTest {
	final static String city = "Sydney";
	String urlStr = "http://api.wunderground.com/api/00c370b16edd3922/conditions/q/AU";
	
	Wunderground wunderground;
	Weather weather;

	@Before
	public void setUp() throws Exception {
		wunderground = new Wunderground();
		weather = new Weather();
		wunderground.setWeather(weather);
		wunderground.setUrlStr(urlStr);
	}

	@Test
	public final void OpenUrlTest() {
		wunderground.openUrl(city);
		assertSame(city, wunderground.getWeather().getCity());
	}

	@Test
	public final void OpenUrlTestFail() {
		urlStr = "abc";
		wunderground.setUrlStr(urlStr);
		
		wunderground.openUrl(city);
		assertTrue(wunderground.isError());
	}

}
