<p align="center">
  <img src="images/RainCheck-logo.png" alt="RainCheck Logo" width="150" />
</p>

<h1 align="center">RainCheck</h1>

<p align="center">
  RainCheck is a Java Swing app that helps users plan their day by linking tasks with real-time weather forecasts in a calendar-style interface. Built using Clean Architecture, the app promotes modular, scalable, and testable code practices.
</p>

---

## 🌦️ Domain: Weather-Aware Task Planning

RainCheck helps users schedule and manage daily tasks while providing context-aware weather data. Hourly forecasts are displayed in a timeline that persists regardless of task updates. Tasks and weather are visually aligned for smarter planning.

---

## 🧩 Software Specification

- Calendar-style timeline with **hourly weather forecasts**
- **Tasks appear beside the forecast** at their scheduled hour
- Weather display remains constant, even if tasks are edited or deleted
- Tasks can be:
    - ✅ **Created, edited, deleted, or marked as completed**
    - 🏷️ Assigned a **priority** (High, Medium, Low)
    - 🧠 Tagged with a **custom name + emoji**
    - 🎤 **Added via voice-to-text input** *(optional enhancement)*
- Secure **account-based login system** using local file storage
- Weather data fetched from the **Visual Crossing Weather API**
- Personalized task lists and weather context are loaded on login

---

## 👥 User Stories

- **[Team Story]** George sees a calendar with hourly weather forecasts. As he adds tasks, they appear next to the relevant hour. If he deletes or reschedules a task, the weather remains unchanged.

- **[Kian’s Story]** Based on a task’s priority, its display color changes to help George distinguish urgent items from less critical ones.

- **[Sean’s Story]** Users can create custom tags (e.g., 📚 School, 🏋️ Gym) with a name and an emoji chosen from a dropdown. Tags improve task categorization.

- **[Brad’s Story]** George adds tasks manually or using voice-to-text, which transcribes and previews input before saving. He can also assign priority levels when creating or editing tasks.

- **[Clara’s Story]** George wants to edit, delete, and mark tasks as done. This helps him stay on track and organized.

- **[Ethan’s Story]** George creates an account and logs in using a username and password. His tasks and weather preferences are saved securely.

---

## 📦 Domain Model: Proposed Entities

| **Entity**         | **Attributes / Description**                                                                 |
|--------------------|---------------------------------------------------------------------------------------------|
| `User`             | `username`, `passwordHash`, `List<Task> tasks`                                              |
| `Task`             | `title`, `scheduledDateTime`, `isCompleted`, `temperature`, `priority`, `displayColor`, `tag` |
| `PriorityLevel`    | Interface with constants: `HIGH`, `MEDIUM`, `LOW`                                           |
| `Tag`              | `name`, `emoji` (e.g., `"📚"`)                                                               |
| `EmojiConstants`   | Interface with `List<String> ALL_EMOJIS`                                                    |
| `WeatherForecast`  | `forecastDateTime`, `weatherDescription`, `temperature`, `precipitationChance`, `windSpeed`, `iconUrl` |

---

## 🌐 Weather API Integration

- Uses the **Visual Crossing Weather API** for hourly weather data
- Supports temperature, conditions, precipitation, wind, and icon imagery
- Data is fetched via **Java HTTP requests**
- Supports **JSON response format**
- Up to **1,000 free API calls/day** under the free tier
- Weather is parsed and mapped to `WeatherForecast` entities

---

## 🧠 Clean Architecture Overview

| **Layer**        | **Purpose**                                                                 |
|------------------|------------------------------------------------------------------------------|
| `entities/`       | Domain models (`User`, `Task`, `Tag`, etc.)                                 |
| `usecases/`       | Business logic for login, signup, task and weather features                 |
| `interfaces/`     | Input/Output boundaries and data gateway interfaces                         |
| `controllers/`    | Handle UI actions, call use case interactors                                |
| `presenters/`     | Format use case results for the view                                        |
| `views/`          | Java Swing UI components (e.g., calendar, task list, login form)            |
| `dataaccess/`     | Implements data storage (e.g., `TaskFileStorage`, `FileUserDataAccessObject`)|
| `services/`       | Communicates with external weather API                                      |
| `resources/`      | Icons, emoji lists, static configuration                                    |
| `Main.java`       | Application entry point and dependency wiring                               |

---

## 🧪 Project Focus by Feature/Member

| **Member** | **Feature Responsibility**                                                        |
|------------|-----------------------------------------------------------------------------------|
| Ethan      | User account creation, login, and secure task & weather data persistence          |
| Brad       | Task input methods including manual entry and voice-to-text transcription         |
| Kian       | Task priority visualization with dynamic color coding for urgency                  |
| Sean       | Custom task tags with name and emoji selection from dropdown                       |
| Clara      | Task management: editing, deleting, and marking tasks as completed                 |
| Team       | Display of hourly weather forecasts and integration with Visual Crossing Weather API |

---

## 📅 Group Coordination

- **Meeting Time**: Thursdays, 5–6 PM (before tutorial)
- **Communication**: Instagram group chat + in-person meetings

---

## 📁 Example File Storage (per user)

Each user has their own JSON file stored in the `userdata/` directory. This file contains their login credentials (safely hashed) and personal task list.

### Example: `userdata/george.json`

```json
{
  "username": "george",
  "passwordHash": "9c1185a5c5e9fc54612808977ee8f548b2258d31",
  "tasks": [
    {
      "title": "Study for test",
      "scheduledDateTime": "2025-07-08T10:00:00",
      "isCompleted": false,
      "temperature": 21.5,
      "weatherDescription": "Sunny",
      "priority": "HIGH",
      "displayColor": "#FF5733",
      "tag": { "name": "School", "emoji": "📚" }
    }
  ]
}
```
Each file stores tasks, preferences, and any custom tags in a lightweight, JSON-based format.

### 🔍 Field Descriptions

- `username`: the user's unique login name.
- `passwordHash`: a one-way hashed version of their password using a secure algorithm like SHA-256.
- `tasks`: a list of the user's scheduled tasks.
    - `title`: the name of the task.
    - `scheduledDateTime`: the date & time the task is planned for.
    - `isCompleted`: whether the task is finished.
    - `temperature`: the temperature (°C) at the time of the task (based on forecast).
    - `weatherDescription`: textual description of the weather at the task time (e.g., "Sunny", "Cloudy", "Rainy").
    - `priority`: the task's urgency level (`HIGH`, `MEDIUM`, or `LOW`).
    - `displayColor`: a hex color code for visual styling in the UI.
    - `tag`: an optional custom tag containing:
        - `name`: descriptive label (e.g., "School").
        - `emoji`: selected emoji icon (e.g., "📚").


---

### 🔐 Why `passwordHash` Is Secure

Even if someone steals the `.json` file:

- The **actual password is never stored** — only its **hashed** version.
- Hashing is a **one-way mathematical function**: you can turn a password into a hash, but **you can’t reverse it**.
- We can use algorithms like `SHA-256`, or even better, `PBKDF2` or `bcrypt` with a **salt** to prevent brute-force attacks and rainbow table lookups.

---

### 🔑 How Secure Login Works

At **sign-up**:
1. The user enters a password (e.g., `george123`)
2. The app hashes it using SHA-256 (e.g., `hash("george123")`)
3. The hash is stored as `passwordHash` in their JSON file

At **login**:
1. User enters their password again
2. App hashes that input
3. It compares the new hash with the stored one
4. ✅ If they match → login success  
   ❌ If not → reject login

🔒 Even if a hacker has the hash:
- They **cannot convert it back** into the original password.
- The best they can do is try every possible password to see if any produce the same hash. That’s why using long, complex passwords — and adding a unique salt (random value) to each password before hashing — makes it extremely difficult for attackers to guess the original password, even if they steal the stored hashes.

---
