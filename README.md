# CSCI-121-WeatherApp
Final Project for CSCI 121 at Clark University in SP. 2024
This project written in Java uses the OpenWeatherMap API as a framework for a simple weather app. Icons in img/ are from OpenWeatherMap's API.
Some functionality is hit or miss, mainly the search terms, but ZIP codes and Town names work best.

Core features:
- Light/dark mode switching based on time of day
- High/low temperatures with 7 day forecast
- Location searching based on the OpenWeatherMap Geocoding / Reverse Geocoding API

If I had more time to work on the project some improvements would be:
- Cleaner looking GUI with better looking foreground and background elements
- Increase the wiggle room with the Search functionality (although this is primarily due to the API)
- Precipitation percentage when the weather is Rain

Notes:
WeatherApp.java has the **API key removed** in line 56. It will need to be replaced with a valid key for the program to function. It can be obtained freely at https://openweathermap.org/
