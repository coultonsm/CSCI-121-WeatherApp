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
WeatherApp.java is the where the main method is located and for the program to run. The other files are for object definitions and creations.
WeatherApp.java has the **API key removed** in line 56. It will need to be replaced with a valid key for the program to function. It can be obtained freely at https://openweathermap.org/

You will also need to install Google's Gson library into the project enviroment. (tested with 2.14.0 on 10/20/26)
https://github.com/google/gson

<img width="602" height="401" alt="Example Daytime weather for Boston, MA" src="https://github.com/user-attachments/assets/4e4bd9ea-c434-4ba1-aeef-d8bfefbda80b" />

<img width="602" height="401" alt="Example Nighttime weather for Wales, England" src="https://github.com/user-attachments/assets/a2cab3f7-7c74-4db3-8cee-71b4cdd6aa51" />

<img width="602" height="401" alt="Screenshot 2026-10-20 at 5 06 35 PM" src="https://github.com/user-attachments/assets/af7fa7a9-6432-4079-badd-588482c9c811" />
