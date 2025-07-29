# WeatherApiService - How To Call
Call upon task creation and/or edit

This document explains how to call the `WeatherApiService` methods, what data they return, and example usage for each.

---

## 1. getDailyWeather(location, date)

### How to call

Call this method with a location name (e.g. "Toronto") and a `LocalDate` object representing the date you want the forecast for.

Example:
```java
WeatherApiService apiService = new WeatherApiService();
Map<String, Object> daily = apiService.getDailyWeather("Toronto", LocalDate.now());

System.out.println("High: " + daily.get("tempmax") + "°C");
System.out.println("Low: " + daily.get("tempmin") + "°C");
System.out.println("Feels Like High: " + daily.get("feelslikemax") + "°C");
System.out.println("Feels Like Low: " + daily.get("feelslikemin") + "°C");
```
To display the weather icon (as an `ImageIcon`):
```java
ImageIcon icon = (ImageIcon) daily.get("icon");
... // do something with the icon
```

> High: 21.2°C<br />
> Low: 17.6°C<br />
> Feels Like High: 21.2°C<br />
> Feels Like Low: 17.6°C<br />

### What it returns

A `Map<String, Object>` containing these keys:

- `"tempmax"`: highest actual temperature (°C)
- `"tempmin"`: lowest actual temperature (°C)
- `"feelslikemax"`: highest feels like temperature (°C)
- `"feelslikemin"`: lowest feels like temperature (°C)
- `icon` :	A scaled ImageIcon representing the weather	ImageIcon (or null)
---

## 2. getHourlyWeather(location, date, startHour, endHour)

### How to call

Call this method with a location name, a `LocalDate` object, and two integers representing the start and end hour (24-hour format).

Example:
```java
WeatherApiService apiService = new WeatherApiService();
List<Map<String, String>> hourly = apiService.getHourlyWeather("Toronto", LocalDate.now(), 9, 12);

for (Map<String, String> hour : hourly) {
        System.out.println(hour.get("time") + ":" +
        hour.get("feelslike") + ", " +
        hour.get("description"));
        }
```

> 09:00:00: 18.4°C, Clear<br />
> 10:00:00: 18.4°C, Clear<br />
> 11:00:00: 18.9°C, Clear<br />
> 12:00:00: 19.5°C, Clear

### What it returns

A `List<Map<String, String>>`, each map containing:

- `"time"`: timestamp for the hour (e.g., "2025-07-18T10:00:00-04:00")
- `"feelslike"`: feels like temperature at that hour (°C)
- `"description"`: brief weather description for the hour (e.g., "24°C, cloudy")
