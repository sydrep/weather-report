#Java Get Current Weather
##Overview
###Select a city to display its weather
This web project allows a user to select 3 Australian cities: Sydney, Melbourne and Wollongong from a dropdown list in a browser to display its real-time weather.
 
##Code Structure
- webapp/WEB-INF/VIEW/weather.jsp - displays the dropdown list and the weather information

- Weather.java - this class stores all the weather data that will be displayed to the browser, including City,
Updated time, Weather, Temperature, and Wind

-  Cities.java - contains a list of cities that is passed to the dropdown list as select options.  The actual list of cities is stored in /src/main/resources/property/cities.properties file which can grow or shrink without having to modify java code.

- City.java - stores the city name from the user selected option of the dropdown list and the City object is passed to the controller

- WeatherController.java - handles request mapped to "weather", e.g. for local host testing http://localhost:8080/weather/weather

When a user selects a city from the dropdown list, the City object will be passed to this controller which calls WeatherCondition.getWeather() to return the Weather object that contains the weather information.  The Weather object is passed to the weather.jsp which is then displayed to the user.

- WeatherCondition.java - a wrapper to call specific weather API.  In this project we use the Wunderground API.

- Wunderground.java - a weather API. 

##Build
Use Maven build.