

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.syd.weathermodels.Wunderground;

// This wrapper by pass URL connection to wunderground.com and
// uses a fixed file for testing
public class WundergroundWrapper extends Wunderground{
	@Override
	public URL getLink() {
		File json = new File("src\\test\\resources\\wundergroundUniqueResponse.json");
		URL fileUrl = null;
		try {
			fileUrl = json.toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return fileUrl;
	}

}
