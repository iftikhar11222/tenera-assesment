# tenera-assesment

To Run it Locally kindly follow the below steps

1.mvn docker:build

2. docker run -it [yoursystemport]:9090 tenera-assement/boot

http://localhost:port/weather-resource/weather/history?location=Islamabad,PK

http://localhost:port/weather-resource/weather/current?location=Islamabad,PK


**Application Architecture** (Traditional Onion Architecture).

Controller Layer: (com.tenera.assessment.controllers)
This layer contains two controller classes.
a)CurrentWeatherController.java: It validates the request parameter using spring validation and throws the ConstraintVolitionException if the input is not valid or if it is empty. If request is valid it delegetes the request to service layer and if no error or exception occurrs returns the CurrentWeatherDTO.java as a response in json format.


b)WeatherHistoryController.java:It validates the request parameter using spring validation and throws the ConstraintVolitionException if the input is not valid
or if it is empty. If request is valid it delegates the request to service layer and if no error or exception occurs returns the WeatherHistoryDTO.java as a
response in json format.



**Service Layer**: (com.tenera.assessment.services)
It has two methods getCurrentWeatherHistory and getCurrentWeatherByCity and getWeatherHistoryByLocation. Both the methods first call the getGeocodeInfoByLocation method
of LocationProvider class which takes the GeoCodingInfo.java as parameter which contains a city name and optional country code (2-3) digit and call the below url to obtain the coordinates' information about the city. Next it passes the same coordinates to the WeatherInfoProvider to get the information about the weather
both the services' response are mapped by separate mapper classes, and it throws an exception which in case if some error occurred during the
processing or in external response it is passed to GlobalExceptionHandle controller which identify the exception and return the error response to the caller.




**Data Access Layer**: (com.tenera.assessment.external):
It contains to components LocationProvider and WeatherInfoProvider. Both component calls the external open weather (check below url).
During the development I realize that when the open weather api  called with city and country name for current or weather history it returns
different response format which could add extra complexity in when mapping the response So instead of calling the weather info api with city and
country name I used the open weather api url which uses coordinate instead of city name to retrieve the current weather or weather history

(Url for getting coordinates of the location)
https://api.openweathermap.org/data/2.5/onecall?lat=13.4105&lon=52.5244&exclude=current,minutely,hourly&appid=49112a81a18efd77dd17b31eb756ce9d
(Url for getting the weather info )
http://api.openweathermap.org/geo/1.0/direct?q=London,,GB&limit=1&appid=49112a81a18efd77dd17b31eb756ce9d

**Error Handling:**
            GlobalExceptionhandler.java is spring controller advic component which catch any exception thrown in application identify the 
exception type and returns the error response.


Test Coverage (Unit test and Integration Test) : 80%


            
  
