# PowerTracker

A cross-platform application built with Kotlin Multiplatform that helps users track their electricity meter balance and consumption.

## 🌟 Features

*   **Real-time Balance Tracking:** Always know your current meter balance.
*   **Token Management:** Easily add new tokens to top-up your meter.
*   **Consumption Insights:** Visualize your electricity usage with insightful charts and summaries.
*   **Meter Management:** Add and manage multiple electricity meters.
*   **User Authentication:** Secure login and registration for users.
*   **Cross-Platform:** A single codebase for both Android and iOS.

## 🛠️ Technologies Used

*   **Kotlin Multiplatform:** For sharing code between Android and iOS.
*   **Jetpack Compose:** For building the user interface for both platforms.
*   **Supabase:** As the backend for database, authentication, and serverless functions.
*   **Ktor:** As the HTTP client for network requests.
*   **Koala Plot:** For creating the consumption charts.
*   **Compose Navigation:** For navigating between screens.
*   **Multiplatform Settings:** For persisting simple data locally.

## 🚀 Setup and Installation

Follow these steps to get the project up and running.

### Prerequisites

*   Android Studio (latest version recommended)
*   A Supabase account.
*   For iOS: a Mac with Xcode installed.

### Steps

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/JDKHGA/PowerTracker.git
    ```

2.  **Set up Supabase Project:**
    *   Go to [supabase.com](https://supabase.com) and create a new project.
    *   Once your project is created, navigate to the **SQL Editor**.
    *   Open the `supabase/schema.sql` file from this repository.
    *   Copy the entire contents of the file, paste it into the SQL Editor, and click **Run**. This will create the necessary tables and database structure.

3.  **Add Your Supabase Credentials:**
    *   In your Supabase project settings, go to the **API** section to find your **Project URL** and **anon public key**.
    *   In the root folder of this project, create a new file named `local.properties`.
    *   Add your credentials to the file like this:
      ```properties
      supabase.url=YOUR_SUPABASE_URL
      supabase.key=YOUR_SUPABASE_ANON_KEY
      ```

4.  **Open and Run the Project:**
    *   Open the cloned project in Android Studio.
    *   The IDE may ask you to sync the project with Gradle. Allow it to sync. This will generate necessary files.
    *   Select the `composeApp` (for Android) or `iosApp` (for iOS) run configuration.
    *   Click the **Run** button.

## 🎬 Demo

*[Optional but recommended: Insert a link to a short screencast video demonstrating your project's main features.]*
