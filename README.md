# GitBuff

## Authors

- Sam Koehler
- Edwin Zeng
- Humzah Ahmed
- Aahir Chakraborty-Saha
- Amirmahadi Hassanpour

## Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [License](#license)
- [Feedback](#feedback)
- [Contributing](#contributing)

---
### Purpose

A fitness management app that gives/teaches users workouts and meal plans tailored to their specific needs, analyzes users' progress on fitness goals over time, 
and allows users to share that progress with others. The application is intended for individuals who want a centralized way to manage their fitness routines, nutrition, and progress.

GitBuff helps solve the difficulties individuals may face when managing multiple aspects of their fitness journey at once. Tracking progress, managing nutrition, and recording workouts can all be done on one platform. GitBuff also includes a calendar feature that allows users to organize and schedule their workouts in advance, helping them stay consistent with their fitness routine. In addition, progress visualizations allow users to view their fitness and nutrition data over time and better understand how they are progressing toward their goals.

Because of this, GitBuff is especially intended for individuals who want a convenient and structured way to improve their fitness. It is very useful for users who want direction and guidance with workouts and nutrition while also being able to track their progress toward their fitness goals.

---

## Features

### Feature 1 — Workout Logging

Log exercises performed during workouts to track workout progress and duration. Exercises can be recorded as either:
- Weightlifting: includes sets, reps, and weight.
- Cardio: includes distance.

Workout entries can be edited or deleted after they have been logged.

![Feature 1 Screenshot](path/to/image.png)

### Feature 2 — Profile-Based Workout Plans

The generated plan takes user profile into consideration:
- Available equipment
- Time available per workout
- Number of days available for working out
- Fitness goal

![Feature 2 Screenshot](path/to/image.png)

### Feature 3 — Meal Logging

Log meals eaten each day by manually entering nutritional information or searching for foods for a baseline of their initial nutritional values. Users can adjust the quantity and nutrition of each food item as needed.
The application displays nutritional totals for each meal, including calories, protein, carbohydrates, and fat.

![Feature 3 Screenshot](path/to/image.png)

### Feature 4 — Meal Recommendations

Receive meal recommendations based on the user's profile data, including dietary restrictions, height, and weight.

![Feature 4 Screenshot](path/to/image.png)

### Feature 5 — Profile and Share Progress

Manage profile information, track activity level and fitness goals (e.g. cut, bulk, maintain), and share fitness progress with other users.

![Feature 5 Screenshot](path/to/image.png)

### Feature 6 —  

View recommended workout plans and logged meals in an in-app calendar and sync them with Google Calendar.

![Feature 6 Screenshot](path/to/image.png)

### Feature 7 — Dashboard Visualization

View a visual summary of workout and nutrition progress through charts and other progress metrics

![Feature 7 Screenshot](path/to/image.png)

---

## Installation

### Prerequisites

* **Java 17+ JDK**
* **Maven** (or use IntelliJ's bundled Maven — no separate installation required)
* **Git**

### 1. Clone the Repository

```bash
git clone https://github.com/Raggant5/GitBuff.git
cd GitBuff
```

### 2. Download Dependencies

Ensure the project directory is pointed at the repository root.

Import the project as a **Maven project** using the `pom.xml`. IDEs such as IntelliJ IDEA will automatically detect the `pom.xml` and configure the project.

This downloads and configures dependencies such as:

* OkHttp
* JFreeChart
* Google Calendar Client

### 3. Configure API Keys

API keys are **optional**. The application will run without them by using fallback/mock data for the affected features.

Create a `.env` file in the project root, at the same level as `pom.xml`:

```env
GEMINI_API_KEY=your_key_here
SPOONACULAR_API_KEY=your_key_here
NUTRITION_API_KEY=your_key_here
```

The `.env` file is never committed to the repository and is excluded through `.gitignore`.

### 4. Configure Google Calendar Sync

Google Calendar synchronization is an **optional feature**.

To enable it:

1. Obtain a `credentials.json` file containing your Google OAuth client credentials from Google Cloud Console.
2. Ensure the **Google Calendar API** is enabled for the associated Google Cloud project.
3. Place the file at:

```text
src/main/resources/credentials.json
```

User email addresses must be manually added to the Google Cloud project as test users to authorize Calendar access.

If `credentials.json` exists but the user has not yet authorized the application, the first Calendar action will open a browser window for OAuth authorization.

If the user declines or closes the authorization window, the application follows the same failure path as when `credentials.json` is unavailable, and the failure is displayed appropriately in the view.

### 5. Run the Application

Run:

```text
app.Main
```

located at:

```text
src/main/java/app/Main.java
```

## Running the JAR

The application can also be run from the packaged JAR:

```bash
java -jar GitBuff-1.0-SNAPSHOT.jar
```

### API Keys

The API keys in `.env` are **not bundled into the JAR**. They are read from the filesystem at runtime.

Therefore, when running the JAR, place the `.env` file in the **working directory from which the JAR is executed**:

```text
.env
GitBuff-1.0-SNAPSHOT.jar
```

For example:

```bash
java -jar GitBuff-1.0-SNAPSHOT.jar
```

If `.env` is not present, the application will still run. The Gemini, Spoonacular, and Nutrition API features will use their respective fallback/mock data instead of crashing.

### Google Calendar Credentials

`credentials.json` is bundled as a JAR resource during packaging. Therefore, JAR recipients **do not need to manually provide the `credentials.json` file**.

However, the user's Google account must still be authorized according to the Google Cloud project's OAuth configuration before Calendar synchronization can be used.


## Feedback

> We welcome feedback on the application, including bug reports, feature suggestions, and usability feedback.

Feedback can be submitted through our [**Feedback Form**](https://docs.google.com/forms/d/e/1FAIpQLSdLqkp0E_zSTOKUOojNN7_6Zn8LMysItFsH9yk7WXaEKcF-2A/viewform?usp=publish-editor).

When submitting feedback, please provide enough information for us to understand the issue or suggestion. For bug reports, include the steps taken, the expected behaviour, and the actual behaviour whenever possible.

Feedback will be reviewed by the project team and used to identify bugs, improve existing features, and prioritize potential improvements. Submission of feedback does not guarantee that a requested feature will be implemented.

## Contributing

1. Fork the repository (or create a branch, if you have write access).
2. Make your changes on a feature branch (e.g. `feature/<short-description>`).
3. Run `mvn test` and ensure Checkstyle passes before opening a pull request.
4. Open a pull request against `main` with a clear description of what changed and why.
5. At least one other contributor reviews and approves the PR before it is merged.
   
## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
