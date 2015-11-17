package unit;

import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.syd.weathermodels.City;


public class CityNameTest {
	@Test
	public final void CityNameTest() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		City city = new City();
		
		Set<ConstraintViolation<City>> constraintViolations = validator.validate(city);

		assertEquals(1, constraintViolations.size());
		assertEquals("Please select a city!", constraintViolations.iterator().next().getMessage());
	}

}
