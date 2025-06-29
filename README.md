Team Name: RainCheck 

Domain: Weather-Aware Task Planning 

 

Software Specification: 

RainCheck is a Java Swing app that helps users plan their day by linking tasks to weather forecasts in a calendar-style interface. Weather data is displayed hour-by-hour across the timeline, independent of tasks. This means weather remains visible even when tasks are edited or deleted, offering consistent context for planning. 

Users can create, edit, delete, and mark tasks as completed. Each task is visually styled based on its priority level (High, Medium, Low), and users can assign a custom tag, which consists of a name and an emoji selected from a dropdown menu. 

Tasks can be added manually or via voice-to-text, which transcribes speech into editable text. 

Each user has a secure account with persistent, file-based task storage. Upon login, the app loads their personalized task list and weather data. 

RainCheck uses the Visual Crossing Weather API to fetch hourly forecasts (temperature, conditions, precipitation, etc.) for the user’s location. Weather is always available on the calendar timeline, and tasks dynamically align with relevant weather based on their scheduled time. 

 

User Stories: 

George sees a calendar with hourly weather forecasts. As he adds tasks, they appear next to the relevant hour. If he deletes or reschedules a task, the weather will remain unchanged. [Team story] 

Based on a task’s priority, its display color changes to help George distinguish urgent items from less critical ones. [Sean’s/5th member’s story] 

Users can create custom tags (e.g., 📚 School, 🏋️ Gym) with a name and an emoji chosen from a dropdown. Tags improve task categorization. [Sean’s story] 

George adds tasks manually or using voice-to-text, which transcribes and previews input before saving. He can also assign priority levels when creating or editing tasks. [Brad’s story] 

George wants to edit, delete, and mark tasks as done. This helps him stay on track and organized. [Clara’s story] 

George creates an account and logs in using a username and password. His tasks and weather preferences are saved securely. [Ethan’s story] 

 

Proposed Entities for the Domain: 

PriorityLevel (Interface): 

String HIGH 

String MEDIUM 

String LOW 

EmojiConstants (Interface): 

List<String> ALL_EMOJIS – a list of all emojis 

Tag: 

String name (e.g., "School") 

String emoji (selected from dropdown, e.g., "📚") 

Task: 

String title 

DateTime scheduledDateTime 

boolean isCompleted 

float temperature 

PriorityLevel priority 

Color displayColor 

Tag tag 

User: 

String username 

String passwordHash 

List<Task> tasks 

WeatherForecast: 

DateTime forecastDateTime 

String weatherDescription 

float temperature 

float precipitationChance 

float windSpeed 

String iconUrl (emoji like “rain” has the iconUrl pointing to ☔ for example) 

 

Proposed API for the Project: 

RainCheck uses the Visual Crossing Weather API to fetch weather data hourly. This data is continuously displayed in the calendar layout regardless of task presence. When a task is added, it aligns visually with the correct weather slot. 

The API supports JSON responses and allows up to 1,000 calls per day under the free tier. Weather is retrieved via Java HTTP requests and parsed into the app's forecast system. 

 

Scheduled Meeting Times + Mode of Communication: 

Meeting Time: Thursdays, 5–6 PM (before tutorial) 

Mode: Instagram group chat + in-person meetings 
