# <p align="center"><b>RainCheck</b></p>

## Table of Contents

- [Project Summary](#project-summary)
- [Authors and Contributors](#authors-and-contributors)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [License](#license)
- [Feedback](#feedback)
- [Contributing](#contributing)

## Project Summary

**RainCheck** is a desktop task planner built with Java Swing that helps users organize their week by combining their personal schedule with live hourly weather forecasts.

Traditional task planners ignore weather, but RainCheck bridges this gap by integrating forecast data directly into a visual week-view calendar. This helps users make informed decisions about when to schedule outdoor activities, avoid weather disruptions, and better prioritize tasks.

Users can create, edit, and delete tasks — each with customizable priority levels, tag names and emojis, reminders, and embedded weather info. All tasks persist across sessions and are tied to secure personal accounts.

RainCheck is useful for anyone who wants to manage time more effectively while staying aware of changing weather conditions that could affect their plans.

## Authors and Contributors

- **Ethan** – User Authentication & Account Management
- **Brad** – Task Creation, Reminder Class & Priority Management
- **Sean** – Custom Tag System (`tagName`, `tagEmoji`)
- **Clara** – Task Editing, Deletion, Completion UI
- **Kian** – Reminder Logic, Notification Feature Design
- **Team** – Weekly Weather View, Weather-Integrated Calendar, Core Architecture, UI Enhancement

## Features

RainCheck helps users plan tasks around real-world weather. Key features include:

- **Weather-Aware Calendar**:  
  View hourly weather alongside a week-view calendar. Weather remains visible as tasks are added, edited, or removed.
  TODO: Add screenshot

- **Smart Task Management**:  
  Create, edit, and delete tasks with:
    - Priority levels (High, Medium, Low) shown by color
    - Custom tag names and emojis (e.g., `Workout 💪`)
    - Weather info at task time (e.g., ☀️ 23°C)
    - Optional reminders and completion status
  TODO: Add screenshot

- **Reminder Notifications**:  
  Set alerts (10, 30, or 60 mins before) that notify users via email.
  TODO: Add screenshot

- **Custom Tags**:  
  Categorize tasks using dropdown tag names and emojis.
  TODO: Add screenshot

- **Secure User Accounts**:  
  Users sign up and log in to access their saved tasks and personalized forecasts.
  TODO: Add screenshot

- **Location Detection**:  
  Automatically fetches weather for your city via IP-based location.
  TODO: Add screenshot

- **API Integrations**:  
  Uses Visual Crossing for weather, Supabase for data, and a TODO email service.
  TODO: Add final email API name

## Installation

Follow these steps to clone, build, and run RainCheck.

---

### Java Prerequisite

RainCheck requires Java JDK 17 or later to build and run.

To simplify setup, we recommend using an IDE that includes a bundled JDK 17 or higher, such as recent versions of IntelliJ IDEA or Eclipse. This way, you won’t need to install Java separately on your system.

If your IDE does not include a bundled JDK or you want to use a specific Java version, you will need to install Java JDK 17+ manually. 

Throughout this README, we use IntelliJ IDEA which does include a bundled JDK 17 or higher.

### 1. Clone the Repository
Open a terminal and run:
```bash
git clone https://github.com/Ethan-bro/RainCheck.git
cd RainCheck
```
<div align="center">
  <img src="images/clone_project.png" alt="Cloning project terminal" width="550px">
</div>

### 2. Build the Project (using an IDE)
 - Open the project in IntelliJ IDEA or Eclipse (or any other java IDE) and build it there.

<img src="images/build_project_in_IDE.png" alt="build_project_in_IDE">

### 3. Configure Secrets

RainCheck requires API keys to access external services like Supabase and Visual Crossing Weather.

- A sample `config/secrets.json` file is **already included** in the repo to make setup easy.
- You **must** replace `"weather_api_key"` with your own if the app fails to load weather (1000 free API calls/month).

```json
{
  "database_url": "https://jbjoxiauljridpmnunuh.supabase.co",
  "database_anon_key": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "weather_api_key": "{your_own_key_here}"
}
```
<img src="images/secrets_json_example.png" alt="secrets.json file structure"> <!-- TODO -->

> **Why is this file public?**  
> The `secrets.json` file is public in this repo because:
> - Our database and weather keys are used in class for development and demo purposes.
> - We frequently reset the backend, so long-term security isn’t a concern.
> - This simplifies setup for teammates and TAs who are testing or marking the project.

---

> **How should *you* handle secrets?**
> If you're building your own version or using your own backend:
> - **Do NOT commit secrets** to GitHub — add `config/secrets.json` to your `.gitignore`
> - **Create the file locally** and keep it private
> - **Use environment variables** where possible (e.g., `.env` files + a dotenv library)

---

### 4. Run the Application
   Option A: From your IDE
   Run the Main class located at:

css
Copy
Edit
src/main/java/app/Main.java
Option B: From terminal

bash
Copy
Edit
java -cp out app.Main
<img src="images/run_application.png" alt="RainCheck running in terminal or IDE"> <!-- TODO -->

✅ If everything works, RainCheck will launch and display the weekly calendar interface.
❌ If you get any errors, check the Troubleshooting section below.



TODO:
------------------
**System Compatibility:**  
RainCheck is compatible with **Windows** and **Linux** systems. It has been developed and tested primarily on these platforms.
- **macOS** users may experience issues with file paths, Swing rendering, or Java SDK setup and may need to manually configure their environment to match Java 17+ expectations.

**Common Installation Issues & Fixes:**

- 🔁 **Java version mismatch**
  - Symptom: `UnsupportedClassVersionError` or crashes at runtime
  - Fix: Make sure you're using **Java 17 or higher**. Check with:
    ```bash
    java -version
    ```

- 🌐 **No internet connection / API call failure**
  - Symptom: Weather data or user login fails to load
  - Fix: Check your internet access, firewall, or VPN settings. RainCheck needs outbound HTTPS access to Supabase and Visual Crossing Weather APIs.

- 📂 **Incorrect project structure**
  - Symptom: Compilation errors when running `javac`
  - Fix: Ensure you're inside the correct project root and that `src/` contains all `.java` files organized by package.

- 🔑 **Missing or malformed `secrets.json` file**
  - Symptom: Task loading, authentication, or weather retrieval fails silently
  - Fix: Create a `config/secrets.json` file with your Supabase and weather API credentials.

  - ✅ This is the correct structure for `config/secrets.json`:
    ```json
    {
      "database_url": "https://jbjoxiauljridpmnunuh.supabase.co",
      "database_anon_key": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Impiam94aWF1bGpyaWRwbW51bnVoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTIyMDE5MDQsImV4cCI6MjA2Nzc3NzkwNH0.rLCXZN4wuDANPaIy3kU0uxKrhm_Ne3yb2KlLP7lMfBw",
      "weather_api_key": "{weather_api_key}"
    }
    ```

    > ⚠️ Replace `{weather_api_key}` with your personal API key if you encounter weather loading issues due to "too many API requests". This is optional and only needed when the automatic IP-based weather fetch fails.

<!-- TODO: Add screenshot of successful secrets.json setup in IDE -->

