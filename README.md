# <p align="center"><b>RainCheck</b></p>
<p align="center"><sub><i>A Desktop Task Planner with Weather Integration</i></sub></p>

> ⚠️ Note: All screenshots in this README were taken during development using placeholder (fake) weather data. 
> The demo video (can be found under [Usage](#usage)) showcases the app fetching and displaying real weather data. 
> While some UI elements may differ slightly from the final version, the core features and layout remain the same.

## Table of Contents

- [Project Summary](#project-summary)
- [Authors and Contributors](#authors-and-contributors)
- [Features](#features)
- [Installation](#installation)
- [Troubleshooting](#troubleshooting)
- [Common Installation Issues & Fixes](#common-installation-issues--fixes)
- [Common IntelliJ IDEA Issues & Fixes](#common-intellij-idea-issues--fixes)
- [Usage](#usage)
- [License](#license)
- [Feedback](#feedback)
- [Contributing](#contributing)

## Project Summary

**RainCheck** is a desktop task planner built with [Java Swing](https://docs.oracle.com/javase/tutorial/uiswing/) that helps users organize their week by combining their personal schedule with live hourly weather forecasts.

Most traditional task planners do not incorporate weather data, but RainCheck bridges this gap by integrating forecast data directly into a visual week-view calendar. This helps users make informed decisions about when to schedule outdoor activities, avoid weather disruptions, and better prioritize tasks.

Users can create, edit, and delete tasks — each with customizable priority levels, tag names and emojis, reminders, and embedded weather info. All tasks persist across sessions and are tied to secure personal accounts.

RainCheck is useful for anyone who wants to manage time more effectively while staying aware of changing weather conditions that could affect their plans.

## Authors and Contributors

- **Ethan** – *User Authentication & Account Management*
- **Brad** – *Task Creation, Reminder Class & Priority Management*
- **Sean** – *Custom Tag System, Tag CRUD (create, read, update, delete)*
- **Clara** – *Task Editing, Deletion, Completion UI*
- **Kian** – *Reminder Logic, Notification Feature Design*
- **Team** – *Weekly Weather View, Weather-Integrated Calendar, Core Architecture, UI Enhancement*

## Features

RainCheck helps users plan tasks around real-world weather. Key features include:

##### 1. Weather-Aware Calendar
 - View daily weather for a week-view calendar. 
 - Weather remains visible as tasks are added, edited, or removed.
 - View hourly weather per task by clicking on the task box in the calendar at any point during the program.

<p align="center">
  <img src="images/feature_weather_calendar.png" alt="Weather-aware calendar in RainCheck" width="500px">
</p>

##### 2. Smart Task Management
Create, edit, and delete tasks with:

- **Priority levels (High, Medium, Low) shown by color**
- **Custom tag names and emojis (e.g., Workout 💪)**
- **Weather info at task time (e.g., ☀️ 23°C)**
- **Optional reminders and completion status**

Click on the image below to play the video on YouTube!

<p align="center">
  <a href="https://www.youtube.com/watch?v=v_BPRbQwGrI&ab_channel=RainCheck" target="_blank">
    <img src="images/TaskCRUDytVidThumbnail.png" width="50%">
  </a>
</p>

##### 3. Reminder Notifications
<p align="center">
  Set alerts (10, 30, or 60 mins before) that notify users via email.
</p>

<p align="center">
  <img src="images/feature_reminder_notifications_addTask config.png" alt="Reminder notification addTask config" width="50%">
  <img src="images/example_gmail_notification.png" alt="Gmail notification example from RainCheck" width="50%">
</p>

##### 4. Custom Tags
Create/Edit/Delete tasks using dropdown tag names and emojis.
Click on the image below to play the video on YouTube!

<p align="center">
  <a href="https://www.youtube.com/watch?v=DM75gLs1ITU&ab_channel=RainCheck" target="_blank">
    <img src="images/TagCRUDytVidThumbnail.png" width="50%">
  </a>
</p>

##### 5. Secure User Accounts
Users sign up and log in to access their saved tasks and personalized forecasts.

<p align="center">
  <img src="images/feature_user_login.png" alt="User login and signup in RainCheck" width="500px">
</p>

## Installation

Follow these steps to clone, build, and run RainCheck.

### Java Prerequisite

RainCheck requires [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher to build and run.

To simplify setup, we recommend using an IDE that includes a bundled JDK 17 or higher, such as recent versions of IntelliJ IDEA or Eclipse. This way, you won’t need to install Java separately on your system.

If your IDE does not include a bundled JDK or you want to use a specific Java version, you will need to install Java JDK 17+ manually. 

Throughout this README, we use IntelliJ IDEA which does include a bundled JDK 17 or higher.

### 1. Clone the Repository
Open a terminal and run:
```bash
git clone https://github.com/Ethan-bro/RainCheck.git
cd RainCheck
```
<p align="center">
  <img src="images/clone_project.png" alt="Cloning project terminal" width="550px">
</p>

### 2. Configure Secrets

RainCheck requires API keys to access external services like Supabase and Visual Crossing Weather.

- A sample `config/secrets.json` file is **already included** in the repo to make setup easy.
- You **must** replace `"weather_api_key"` with your own if the app fails to load weather (1000 free API calls/month).

```json
{
  "database_url": "https://jbjoxiauljridpmnunuh.supabase.co",
  "database_anon_key": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Impiam94aWF1bGpyaWRwbW51bnVoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTIyMDE5MDQsImV4cCI6MjA2Nzc3NzkwNH0.rLCXZN4wuDANPaIy3kU0uxKrhm_Ne3yb2KlLP7lMfBw",
  "weather_api_key": "{your_own_key_here}",
  "email_username": "your_email@gmail.com",
  "email_password": "sixteenletterpass"
}
```
> Note:
> `email_username` must be a valid email address (Avoid school or work emails, as they may block automated emails — use a personal email instead.)
> `email_password` must be exactly 16 letters, no numbers, no spaces, no special characters.
---
> **Why is this file public?**  
> The `secrets.json` file is public in this repo because:
> - Our database and weather keys are used in class for development and demo purposes.
> - We frequently reset the backend, so long-term security isn’t a concern.
> - This simplifies setup for teammates and TAs who are testing or marking the project.
> **How should *you* handle secrets?**
> If you're building your own version or using your own backend:
> - **Do NOT commit secrets** to GitHub — add `config/secrets.json` to your `.gitignore`
> - **Create the file locally** and keep it private
> - **Use environment variables** where possible (e.g., `.env` files + a dotenv library)

### 3. Run the Project (using an IDE)
Open the project in IntelliJ IDEA, Eclipse, or your preferred Java IDE and run the `Main` class.

IntelliJ may prompt you to configure the JDK if it's not set up yet.  
To do this:

1. Go to `File` → `Project Structure` → `Project`
2. Under `Project SDK`, click the dropdown and select `Download JDK`
3. Choose your desired JDK version (Java 17 or later) and install it
4. Click `Apply` and then `OK` after download completes

<p align="center">
  <img src="images/intellij_setup_jdk.png" alt="IntelliJ Project Structure SDK setup dialog" width="400px">
</p>

After the SDK is set up, simply run the `Main.java` class to launch RainCheck.
> `Main.java` is located at `RainCheck/src/main/java/app/Main.java`.

At the time of writing this readme, the program runs successfully and looks like: 

<p align="center">
  <img src="images/run_application.png" alt="RainCheck running in IDE" width="500px">
</p>

> ✅ **If everything works:** RainCheck will launch and display the weekly calendar interface.
>
> ❌ **If you get errors:** Check the [Troubleshooting](#troubleshooting) and [Common Installation Issues & Fixes](#common-installation-issues--fixes) sections below.

## Troubleshooting

## System Compatibility
RainCheck is compatible with:
- Windows - fully supported
- macOS - fully supported
- Linux - may work but untested (check Java 17+ configuration)

Note for macOS/Linux: Verify IDE uses Java 17+ with matching compiler

## Common Installation Issues & Fixes

### Git Clone Fails

Symptom: When running
```bash
git clone https://github.com/Ethan-bro/RainCheck.git
```
You receive the error:
```bash
fatal: repository 'https://github.com/Ethan-bro/RainCheck.git/' not found
```
Fixes:
1. Verify the repository URL:
 - Check for typos in the URL
 - Remove any extra characters like quotes or trailing slashes
 - Verify the repository exists and is public
 - Correct URL should be: https://github.com/Ethan-bro/RainCheck.git
2. Check your internet connection:
 - Ensure you have stable internet access
 - Try accessing ```github.com``` in your browser
3. Try SSH cloning instead:
```bash
git clone git@github.com:Ethan-bro/RainCheck.git
```

> Notes:
> First-time GitHub users may need to set up Git credentials
> Corporate networks might block Git operations

### Java SDK Missing

Symptom: 'No SDK' errors in IntelliJ <br />
Fix:
1. File > Project Structure > Project
2. Set SDK to JDK 17 (or higher). Download if missing
3. Apply changes

For detailed JDK setup instructions with visual guidance, see: <br />
[Setting Up Java JDK/SDK](#3-run-the-project-using-an-ide)

### `secrets.json` Problems

Symptom: Signup/Login and weather retrieval features don't work  
Fix: Ensure the file exists in `config/secrets.json` with the following structure:

```json
{
  "database_url": "https://jbjoxiauljridpmnunuh.supabase.co",
  "database_anon_key": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Impiam94aWF1bGpyaWRwbW51bnVoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTIyMDE5MDQsImV4cCI6MjA2Nzc3NzkwNH0.rLCXZN4wuDANPaIy3kU0uxKrhm_Ne3yb2KlLP7lMfBw",
  "weather_api_key": "{your_own_key_here}",
  "email_username": "your_email@gmail.com",
  "email_password": "sixteenletterpass"
}
```
For more info, see the [Configure Secrets](#2-configure-secrets) section.
---
If you experience issues with weather data not loading correctly, check the `useFakeData` variable in
`src/main/java/data_access/WeatherApiService.java`. <br />

This variable controls whether RainCheck fetches real weather data from the API or uses fake (mock) data for testing and offline development.
 - When `useFakeData` is set to `false` (default), RainCheck fetches live weather data using your API key from `secrets.json`.
 - When set to `true`, the app uses pre-defined fake weather data, which is useful for faster testing or when offline.
Make sure to set this variable according to your needs and verify that your API key in secrets.json is valid to avoid errors.

## Common IntelliJ IDEA Issues & Fixes
### 1. **Project Build or Compilation Errors**
**Symptom:** Errors during build or run, even though the code seems correct.
**Fix:** Follow this tutorial to resolve common build issues in IntelliJ IDEA:
[How to fix common errors in IntelliJ](https://www.youtube.com/watch?v=hbXsdKGG0Pg&ab_channel=BitsNBytes)

### 2. **Running the Application: Errors or Blank Screen**
**Symptom:** Application doesn't start or shows a blank screen.
**Fix:** Ensure all configurations are correct and dependencies are properly set up. <br />
Refer to the tutorials above for detailed guidance.

---

## Usage

> For the demo below, real weather data is fetched from the API to showcase the UI and weather-aware features. 
> You can toggle between fake and real data by modifying the `useFakeData` variable in:
> `src/main/java/data_access/WeatherApiService.java`.

Modify the `useFakeData` variable as follows:

```java
private static final boolean useFakeData = true; // or false
```

This allows you to easily switch between mock data (for testing/dev) and live weather API responses.

Click on the image below to play the video on YouTube!

<p align="center">
  <a href="https://www.youtube.com/watch?v=w0pAS-CPcy0&ab_channel=RainCheck" target="_blank">
    <img src="images/raincheckDemoYtThumbnail.png" width="50%">
  </a>
</p>

## License

This project is licensed under the [MIT License](LICENSE).

You are free to use, modify, and distribute this software for personal or commercial use, provided that you include the original copyright and license.

For more details, see the [LICENSE](LICENSE) file in this repository.

## Feedback

We welcome feedback to improve RainCheck!

If you encounter bugs, usability issues, or have feature suggestions, please let us know via this form:  
[📋 Submit Feedback](https://forms.gle/f4TtfUXz5E6zRzN46)

### What counts as valid feedback?
- Bug reports (UI errors, features not working, crashes)
- Feature suggestions (e.g., new task filters, calendar views)
- UI/UX improvement ideas
- Accessibility or usability issues

### What to expect when submitting feedback:
- We review feedback weekly during development cycles
- Critical bugs or UI issues may be prioritized
- We may follow up if more info is needed

## Contributing

Want to contribute to RainCheck? We appreciate your interest!

### How to contribute

1. **Fork** this repository using the top-right corner of the GitHub page.
2. **Clone** your forked repo:
  ```bash
     git clone https://github.com/Ethan-bro/RainCheck.git
  ```
3. Create a new branch:
  ```bash
  git checkout -b your-feature-name
  ```
4. Make your changes, test them locally, then push:
  ```bash
  git push origin your-feature-name
  ```
5. Submit a pull request (PR) with a clear description of what you changed and why.

### Guidelines for Pull Requests
- Keep PRs focused and **small** — ideally, changes should address a single feature or fix and be under 200 lines of code to facilitate easier review. Avoid mixing unrelated changes in one PR.
- Clearly describe your changes in the PR
- Follow the existing folder structure and naming conventions
- Check that your code builds and runs before submitting
- Respect the project's purpose and scope

### Review Process
- We review all PRs manually
- Small changes (e.g., UI fixes) may be merged quickly
- Larger or architectural changes may require more time and discussion
- We reserve the right to reject contributions that:
  - Conflict with the project's vision
  - Violate license terms
  - Introduce unnecessary complexity