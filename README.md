Project Overview
RainCheck is a Java Swing app that helps users plan tasks linked with real-time weather forecasts using the Visual Crossing Weather API. It follows Clean Architecture to keep code modular, testable, and scalable.

Architecture Layers & Responsibilities
Layer / Folder	What it Does	Where to Work / Focus
entities/	Core business data and rules (Task, User, etc.)	Define your data models & critical logic
usecases/	Application logic (interactors handling tasks, weather)	Implement business rules, task & weather processing
interfaces/	Input/output boundaries & gateway interfaces	Define interfaces for interactors, data access, and presenters
controllers/	Receive UI input and call use case interactors	Connect UI actions to business logic
presenters/	Format data for the UI	Prepare output from interactors for display
dataaccess/	Data persistence implementations (file storage)	Read/write tasks to file, implement TaskGateway interface
services/	External API wrappers (Visual Crossing Weather API)	Fetch and parse weather data
views/	Swing UI components	Display tasks, weather info, and interact with user
resources/	Static resources like icons, emojis, configs	Store any non-code assets
Main.java	App entry point	Wire dependencies and start the UI

Development Checklist
1. Setup & Folder Structure
Create folders/files as per project structure.

Add your API key to WeatherApiService.java.

2. Entities
Define Task, User, Tag, and PriorityLevel.

Keep entities free of UI/DB dependencies.

3. Data Access
Implement TaskFileStorage to save/load tasks as JSON or CSV.

Ensure it implements the TaskGateway interface.

4. Services
Build WeatherApiService to call Visual Crossing API and parse data.

5. Use Cases
Write WeatherDisplayInteractor to get weather info per task.

Write TaskManagementInteractor for creating/editing/deleting tasks.

6. Interfaces
Define input/output boundary interfaces for use cases.

Define TaskGateway interface for data access.

7. Controllers & Presenters
Implement controllers to receive UI commands and call interactors.

Implement presenters to format interactor responses for the UI.

8. Views (Swing UI)
Build MainView to display tasks with weather info in a list.

Add UI components for adding/editing tasks (optional).

9. Main Program
In Main.java, wire together all components (controllers, interactors, views, storage).

Load tasks from file on start and display them.

10. Testing
Write unit tests for entities, interactors, and data access classes.

Test API integration with mock data or actual API calls.

How It All Works Together
User interacts with Swing UI (views/), clicking or adding tasks.

UI calls Controllers (controllers/), which trigger Use Cases (usecases/).

Use cases manipulate Entities (entities/) and fetch weather via Services (services/).

Results are sent to Presenters (presenters/) which prepare data for UI display.

Data persistence is handled by Data Access (dataaccess/) classes implementing TaskGateway.