# Auto Mechanics
Auto Mechanics is an Android app that helps travelers find nearby mechanics or tow trucks available within a 3km radius when their vehicle breaks down during a journey, offering on-demand roadside assistance in one tap. It is a replica of careem/Uber app but for roadside assistance. The platform includes an admin side for verification, monitoring, and safety oversight.

## What This App Does
- Helps users find mechanics and towing shops in unfamiliar areas
- Matches customers with nearby service providers within a 3 km radius
- Supports customer, mechanic, towing shop, and admin roles
- Includes verification and monitoring from the admin panel
- Provides emergency/safety buttons across modules

## Modules
- Customer, Mechanic, and Towing Shops app: `Cust,Mehcanic,Tow Shops Module/rec`
- Admin app: `Automechanics Admin Module/rec`

## Tech Stack
- Android (Java, Gradle, Android Studio)
- Firebase (Auth, Realtime Database, Storage, Analytics)
- Google Maps API + Location Services

## Prerequisites
- Android Studio (Giraffe or newer recommended)
- JDK 8 or newer
- A Firebase project with Authentication enabled
- A Firebase project with Realtime Database enabled
- A Firebase project with Storage enabled
- A Google Maps API key with Maps SDK for Android enabled

## Local Setup From Scratch
1. Clone the repository.
2. Open the customer/mechanic/towing project in Android Studio at `Cust,Mehcanic,Tow Shops Module/rec`.
3. Open the admin project in a separate Android Studio window at `Automechanics Admin Module/rec`.
4. Add Firebase config files.
5. Add your Google Maps API key.
6. Sync Gradle for both projects.
7. Run each app on an emulator or physical device.

## Exact Files You Must Update
Firebase configuration:
- Customer/Mechanic/Towing app: `Cust,Mehcanic,Tow Shops Module/rec/app/google-services.json`
- Admin app: `Automechanics Admin Module/rec/app/google-services.json`

Google Maps API key:
- Replace the value in `Cust,Mehcanic,Tow Shops Module/rec/app/src/main/AndroidManifest.xml`.
- Look for `com.google.android.geo.API_KEY`.

3 km radius logic:
- Distance checks are hardcoded in `Cust,Mehcanic,Tow Shops Module/rec/app/src/main/java/com/example/map/MapsActivity.java`.
- Distance checks are hardcoded in `Cust,Mehcanic,Tow Shops Module/rec/app/src/main/java/com/example/map/MechMapsActivity.java`.
- The radius value is also stored as a string `"3000"` in `Cust,Mehcanic,Tow Shops Module/rec/app/src/main/java/com/example/map/account_deactivate_towing.java`.
- The radius value is also stored as a string `"3000"` in `Cust,Mehcanic,Tow Shops Module/rec/app/src/main/java/com/example/map/account_deactivation_mechanic.java`.
- The radius value is also stored as a string `"3000"` in `Cust,Mehcanic,Tow Shops Module/rec/app/src/main/java/com/example/map/payment_mech.java`.
- The radius value is also stored as a string `"3000"` in `Cust,Mehcanic,Tow Shops Module/rec/app/src/main/java/com/example/map/payment_towing.java`.

## How To Run
Customer/Mechanic/Towing app entry activity:
- `splashScreen` in package `com.example.map`

Admin app entry activity:
- `adminlogin` in package `com.example.rec`

## Permissions
Customer/Mechanic/Towing app permissions:
- Location (coarse, fine, background)
- Internet
- Camera
- External storage read/write
- Phone call

Admin app permissions:
- External storage read

## Project Structure Notes
- Each module is a standalone Android app with its own `app/` and Gradle configuration.
- Firebase configuration is required in both modules.

