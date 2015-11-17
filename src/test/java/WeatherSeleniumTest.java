import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;



public class WeatherSeleniumTest {
	  private WebDriver driver;
	  private String baseUrl;
	  private StringBuffer verificationErrors = new StringBuffer();

	  @Before
	  public void setUp() throws Exception {
	    driver = new FirefoxDriver();
	    baseUrl = "http://localhost:8080/";
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  }

	  @Test
	  public void testWeatherSelenium() throws Exception {
		String HINT = "Avaliable cities are " ;
		String WEATHER = "Weather in ";
		String LOC1 = "Sydney";
		String LOC2 = "Melbourne";
		String LOC3 = "Wollongong";
		String CITY_NAME = "cityName";
		  
	    driver.get(baseUrl + "weather");
	    new Select(driver.findElement(By.id(CITY_NAME))).selectByVisibleText(LOC1);
	    assertTrue(driver.getPageSource().contains(WEATHER + LOC1));
	    new Select(driver.findElement(By.id(CITY_NAME))).selectByVisibleText(LOC2);
	    assertTrue(driver.getPageSource().contains(WEATHER + LOC2));
	    new Select(driver.findElement(By.id(CITY_NAME))).selectByVisibleText(LOC3);
	    assertTrue(driver.getPageSource().contains(WEATHER + LOC3));
	    new Select(driver.findElement(By.id(CITY_NAME))).selectByVisibleText("");
	    assertTrue(driver.getPageSource().contains(HINT));
	  }

	  @After
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }
}
