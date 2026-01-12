# PowerTracker: Kotlin Student Coding Competition Submission

## About The Project

PowerTracker is a Kotlin Multiplatform app designed to help users with prepaid electricity meters track their energy consumption, manage their balance, and gain insights into their usage patterns. The app is built with a focus on a shared codebase for both Android and iOS, leveraging the power of KMP for a consistent and maintainable solution.

For a detailed look at my background, the project's inception, and the technologies used, please see the full essay.

➡️ **[Read the Full Essay](./ESSAY.md)**

## Key Features

- **Real-Time Balance Tracking:** See your electricity balance in both kWh and local currency.
- **Automated Usage Simulation:** Intelligently simulates energy consumption to provide a dynamic look at the remaining balance.
- **AI-Powered Insights:** Uses a Supabase Edge Function to forecast when credit will run out.
- **Historical Data and Trends:** View token purchase history and daily usage trends.
- **Multi-Meter Support:** Add and manage multiple prepaid meters.
- **Cross-Platform:** A single shared codebase for both Android and iOS.

## Demo Video

*(A short 3-5 minute video demonstrating the app's features is highly recommended. You can link to it here.)*

[Link to your demo video here]

## Installation & Setup Instructions

To get the PowerTracker app up and running, follow these steps:

1.  **Clone the Repository:**

    ```bash
    git clone [Your GitHub Repository URL]
    ```

2.  **Create a `local.properties` File:**

    In the root directory of the project, create a new file named `local.properties` and add your Supabase URL and Key:

    ```properties
    supabase.url=YOUR_SUPABASE_URL
    supabase.key=YOUR_SUPABASE_ANON_KEY
    ```

3.  **Run the App:**

    #### Android

    1.  Open the project in Android Studio.
    2.  Wait for the Gradle sync to complete.
    3.  Select the `composeApp` run configuration.
    4.  Choose an Android device or emulator and click the "Run" button.

    #### iOS

    1.  Ensure you have Xcode installed on your macOS machine.
    2.  Open a terminal and navigate to the project's root directory.
    3.  Run the following command to prepare the Xcode workspace:
        ```bash
        ./gradlew :composeApp:podInstall
        ```
    4.  Open the generated `iosApp/iosApp.xcworkspace` file in Xcode.
    5.  Select an iOS simulator or a connected device.
    6.  Click the "Run" button in Xcode.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
