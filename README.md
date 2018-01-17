## GameSparks Android SDK Example ##
This is intended to demonstrate one approach to using the GameSparks Android SDK in an Android Application.

### How to Build ###
 1. Clone the example project.
 2. Inside the root directory of the project, we'll need to create a keystore.properties file. This file will hold our secret api key information. An example of this file is given below.
 3. In order to be able to read these keystore values defined above, we'll need to modify our build.gradle file to include these. A sample of this file is given below.

#### keystore.properties Example ####
```text
GameSparksApiKey="<Api Key given in the portal>"
GameSparksApiCredential="device"
GameSparksApiSecret="<Api Secret given in the portal>"
```

#### build.gradle Example ####
```text
apply plugin: 'com.android.application'
// Keystore secrets
def keystorePropertiesFile = rootProject.file("keystore.properties")
def keystoreProperties = new Properties()
keystoreProperties.load(new FileInputStream(keystorePropertiesFile))
android {
    compileSdkVersion 25
    buildToolsVersion '26.0.2'
    defaultConfig {
        applicationId "com.example.test1"
        minSdkVersion 11
        targetSdkVersion 25
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.txt'
        }
        applicationVariants.all { variant ->
            variant.buildConfigField "String", "GAMESPARKS_API_KEY", keystoreProperties['GameSparksApiKey']
            variant.buildConfigField "String", "GAMESPARKS_API_SECRET", keystoreProperties['GameSparksApiSecret']
            variant.buildConfigField "String", "GAMESPARKS_API_CREDENTIAL", keystoreProperties['GameSparksApiCredential']
        }
    }
}
dependencies {
    compile 'com.android.support:support-v4:23.1.1'
    compile 'com.android.support:appcompat-v7:23.1.1'
    compile 'com.gamesparks.sdk:gamesparks-android-client-sdk:0.4.4'
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    compile project(path: ':gamesparks-android-client-sdk')
}
```

### Notes ###
 * The GameSparks Portal can be found [here](https://portal2.gamesparks.net/).
 * Details of the SDK usage can be found in our [Android SDK](https://docs.gamesparks.com/sdk-center/android.html) Documentation.