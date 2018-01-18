## GameSparks Android SDK Example ##
This is intended to demonstrate one approach to using the GameSparks Android SDK in an Android Application.

### How to Build ###
 1. Clone the example project.
 2. Inside the root directory of the project, we'll need to create a keystore.properties file. This file will hold our secret api key information. An example of this file is given below.
 3. Open the project with Android Studio.

#### keystore.properties ####
```text
GameSparksApiKey="<Api Key given in the portal>"
GameSparksApiCredential="device"
GameSparksApiSecret="<Api Secret given in the portal>"
```

### Notes ###
 * The GameSparks Portal can be found [here](https://portal2.gamesparks.net/).
 * Details of the SDK usage can be found in our [Android SDK](https://docs.gamesparks.com/sdk-center/android.html) Documentation.