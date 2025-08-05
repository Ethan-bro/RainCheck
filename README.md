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
  <!-- TODO: Add screenshot -->

- **Smart Task Management**:  
  Create, edit, and delete tasks with:
    - Priority levels (High, Medium, Low) shown by color
    - Custom tag names and emojis (e.g., `Workout 💪`)
    - Weather info at task time (e.g., ☀️ 23°C)
    - Optional reminders and completion status
  <!-- TODO: Add screenshot -->

- **Reminder Notifications**:  
  Set alerts (10, 30, or 60 mins before) that notify users via email.
  <!-- TODO: Add screenshot -->

- **Custom Tags**:  
  Categorize tasks using dropdown tag names and emojis.
  <!-- TODO: Add screenshot -->

- **Secure User Accounts**:  
  Users sign up and log in to access their saved tasks and personalized forecasts.
  <!-- TODO: Add screenshot -->

- **Location Detection**:  
  Automatically fetches weather for your city via IP-based location.
  <!-- TODO: Add screenshot -->

- **API Integrations**:  
  Uses Visual Crossing for weather, Supabase for data, and a TODO email service.
  <!-- TODO: Add final email API name -->
