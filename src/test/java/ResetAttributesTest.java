

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.syd.weathermodels.Weather;

public class ResetAttributesTest {
	@Test
	public final void ResetAttributesTest() {
		Weather weather = new Weather();
		weather.resetAttributes();
		assertSame("", weather.getMessage());
		assertSame("", weather.getCity());
		assertSame("", weather.getUpdatedTime());
		assertSame("", weather.getWeather());
		assertSame("", weather.getTemperature());
		assertSame("", weather.getWind());
	}

}
