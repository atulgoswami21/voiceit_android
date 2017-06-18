# VoiceIt Android SDK

For more information on VoiceIt and its features, see [the website](http://voiceit.tech) and [getting started docs](https://siv.voiceprintportal.com/getstarted.jsp)

* [Getting Started](#getting-started)
* [Usage](#usage)
* [API Calls](#api-calls)
  * [Initialization](#initialization)
  * [Create User](#create-user)
  * [Get User](#get-user)
  * [Delete User](#delete-user)
  * [Create Enrollment](#create-enrollment)
  * [Get Enrollments](#get-enrollments)
  * [Delete Enrollment](#delete-enrollment)
  * [Authentication](#authentication)

## Getting Started

This is an SDK that lets you integrate voice authentication inside your android apps using VoiceIt's API 1.0

To use this SDK in your Android Project, if you haven't already, please Sign Up for a free **Developer Id** at [http://voiceit.tech](https://siv.voiceprintportal.com/getDeveloperIDTile.jsp). Then follow the installation instructions below.

## Usage

To user the Android SDK add the following to your root build.gradle at the end of repositories
```Groovy
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Then add the VoiceIt Android SDK as a dependency in the android/app/build.gradle
```Groovy
dependencies {
        compile 'com.github.voiceittech:voiceit_android:1.0.0'
}
```

### Permissions

In order to use VoiceIt inside of your Android apps make sure you have the following permissions enabled inside of your [AndroidManifest.xml](https://developer.android.com/guide/topics/manifest/manifest-intro.html) file, that let the app access internet, recording, and give it the ability to write to external storage.

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

## API Calls

Here are code snippets that show you how you can call the Various VoiceIt API Calls inside of your Cordova Project JavaScript Files.

### Initialization

Initialize a reference to VoiceIt inside the Activity where you would like the authentication to take place

```java
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import io.voiceit.voiceit.VoiceIt;

final VoiceIt myVoiceIt = new VoiceIt("DEVELOPER_ID_HERE");
```

### Create User

To create a new user call the createUser function like this with the following parameters: developerId, userId, password(not encrypted, just in text form, the plugin encrypts the password using SHA256 for you)

```java
myVoiceIt.createUser("developerUserId","d0CHipUXOk", new JsonHttpResponseHandler() {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        System.out.println("JSONResult : " + response.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
        if (errorResponse != null) {
            System.out.println("JSONResult : " + response.toString());
        }
    }

});
```
### Get User

To get an existing user call the getUser function like this with the following parameters: developerId, userId, password(not encrypted, just in text form, the plugin encrypts the password using SHA256 for you)


```java
myVoiceIt.getUser("developerUserId","d0CHipUXOk", new JsonHttpResponseHandler() {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        System.out.println("JSONResult : " + response.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
        if (errorResponse != null) {
            System.out.println("JSONResult : " + response.toString());
        }
    }

});
```
### Delete User

To delete an existing user call the deleteUser function like this with the following parameters: developerId, userId, password(not encrypted, just in text form, the plugin encrypts the password using SHA256 for you)

```java
myVoiceIt.deleteUser("developerUserId","d0CHipUXOk", new JsonHttpResponseHandler() {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        System.out.println("JSONResult : " + response.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
        if (errorResponse != null) {
            System.out.println("JSONResult : " + response.toString());
        }
    }

});
```
### Create Enrollment

To create a new enrollment template for the specified user profile use the createEnrollment function like this with the following parameters: developerId, userId, password(not encrypted, just in text form the plugin encrypts the password using SHA256 for you) and content language.

Please Note: Unlike other wrappers, this createEnrollment function actually has recording inbuilt, it records the user saying their VoicePrint phrase for 5 seconds and then makes the Create Enrollment API call to send that audio file as an enrollment.

The recorder starts recording as soon as the createEnrollment function is called, it is also recommended that you either provide a visual or auditory indication to the user before the recording is about to start for example "playing a beep".

```java
myVoiceIt.createEnrollment("developerUserId","d0CHipUXOk", "en-US", new JsonHttpResponseHandler() {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        System.out.println("JSONResult : " + response.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
        if (errorResponse != null) {
            System.out.println("JSONResult : " + response.toString());
        }
    }

});
```
### Get Enrollments

To get a list of the existing enrollments simply call the getEnrollments method for the specific user like this with the following parameters: developerId, userId, password(not encrypted, just in text form, the plugin encrypts the password using SHA256 for you)

```java
myVoiceIt.getEnrollments("developerUserId","d0CHipUXOk", new JsonHttpResponseHandler() {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        System.out.println("JSONResult : " + response.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
        if (errorResponse != null) {
            System.out.println("JSONResult : " + response.toString());
        }
    }

});
```
### Delete Enrollment

To delete an enrollment simply call the deleteEnrollment method for the specific user like this with the following parameters: developerId, userId, password(not encrypted, just in text form, the plugin encrypts the password using SHA256 for you), and enrollmentId

```java
myVoiceIt.deleteEnrollment("developerUserId","d0CHipUXOk", 123456, new JsonHttpResponseHandler() {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        System.out.println("JSONResult : " + response.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
        if (errorResponse != null) {
            System.out.println("JSONResult : " + response.toString());
        }
    }

});
```

### Authentication

To authentication a specified user profile use the authentication function like this with the following parameters: developerId, userId, password(not encrypted, just in text form the plugin encrypts the password using SHA256 for you) and content language.

Please Note: Unlike other wrappers, this authentication function actually has recording inbuilt, it records the user saying their VoicePrint phrase for 5 seconds and then makes the Authentication API call with the audio file.

The recorder starts recording as soon as the authentication function is called, it is also recommended that you either provide a visual or auditory indication to the user before the recording is about to start for example "playing a beep".

```java
myVoiceIt.authentication("developerUserId","d0CHipUXOk", "en-US", new JsonHttpResponseHandler() {
    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        System.out.println("JSONResult : " + response.toString());
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse){
        if (errorResponse != null) {
            System.out.println("JSONResult : " + response.toString());
        }
    }

});
```
