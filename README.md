# PowerTracker: Kotlin Student Coding Competition Submission

## About The Project

PowerTracker is a Kotlin Multiplatform app designed to help users with prepaid electricity meters track their energy consumption, manage their balance, and gain insights into their usage patterns. The app is built with a focus on a shared codebase for both Android and iOS, leveraging the power of KMP for a consistent and maintainable solution.

For a detailed look at my background, the project's inception, and the technologies used, please see the full essay.

➡️ **[Read the Full Essay](./ESSAY.md)**

## Demo Video

A short video demonstrating the app's features can be found here:

➡️ **[Link to your demo video]** *(Please replace this with the actual link to your video)*

## Key Features

- **Real-Time Balance Tracking:** See your electricity balance in both kWh and local currency.
- **Automated Usage Simulation:** Intelligently simulates energy consumption to provide a dynamic look at the remaining balance.
- **AI-Powered Insights:** Uses a secure Supabase Edge Function to forecast when credit will run out.
- **Historical Data and Trends:** View token purchase history and daily usage trends.
- **Multi-Meter Support:** Add and manage multiple prepaid meters.
- **Cross-Platform:** A single shared codebase for both Android and iOS.

## How to Run the App

This project connects to a live Supabase backend for the database, user authentication, and AI features. No special setup is required.

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/JDKHGA/PowerTracker.git 
    ```

2.  **Open and Run in Android Studio:**
    - Open the cloned project in the latest version of Android Studio.
    - Wait for Gradle to sync.
    - Select an Android device or emulator.
    - Select the `composeApp` run configuration and click the ▶️ **Run** button.

## Backend & Database

- The app is backed by a **Supabase** project, which handles the database, authentication, and the AI serverless function.
- The AI feature securely calls the Gemini API via a Supabase Edge Function. The API key is stored as a secret in the Supabase project and is never exposed to the client.
- The complete database structure can be reviewed in the `supabase/schema.sql` file.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
