# AnSu-IAM Java Client

Java client for our AnSu-IAM service (published as part of our Web Services course).

## Introduction

This project is a Java client for consuming our IAM REST API.

It is preferred that you use this client (if you plan on using our AnSu-IAM Service) as it shall make it easier to use our services without having to muck about with trying to manually send HTTP requests to the server.

This project was done as part of our Web Services elective at PES University, Bangalore, India.

## Tutorial

Using the client should be simple to anyone with basic Java know-how.

Let us first understand the exceptions that we include as part of this client.

### Exceptions

It is important that you understand the exceptions that are part of using the client.

There are 3 custom exception classes (found under `com.ansu.iam.client.exception`)

+ `IAMClientErrorException`: Thrown usually when the client does not have internet connectivity or is unable to establish connection with the server.
+ `IAMInputException`: Thrown when the input to the client's methods are invalid (`null` or `length 0` or `0`)
+ `IAMServerErrorException`: Thrown when the server response indicates an error (more on this later), usually the parameters are invalid in some way (already exists or does not exist).

But before we continue to learn how to _use_ the client, I'd like to explain the response that you _should_ get from the client methods.

### The `IAMResponse` class

If all goes well and the request is sent successfully, then you should get a `IAMResponse` object as response.

This class is a pretty simple JavaBean with the following 4 methods:

```java
public String getStatus()
public String getPayload()
public void setStatus(String status)
public void setPayload(String payload)
```

This is inline with our actual server response which is a JSON object that looks like this:

```json
{
    "status": "...",
    "payload": "..."
}
```

You can retrieve the appropriate fields by calling the corresponding getter (`get*()`). Usually, you'll only have to use the `getPayload()` method as the actual response from the server (the information) is under this field.

You may not get a `IAMResponse` object under certain conditions:

+ Network connectivity issues
+ Input to client methods are invalid (see above)
+ The token string sent for validation is invalid

The first two reasons are undertandable and easy to resolve, but the third is understandably confusing. More information shall be provided on this later.

Now, let's finally begin learning how to use the client to _actually_ send requests to the server.

### Using the client

You can use the client to perform the following actions:

+ Register an application and get a `appId`
+ Get the `appId` given the application name
+ Update the token validity period for an application (in seconds)
+ Register a user (`username` and `password`) under an application (`appId`)
+ Delete a user given the `uid` and the `appId`
+ Authenticate a user (with `appId`, `uid` and `password`) and get a token string
+ Check the validity of a `token`
+ Delete (invalidate) a `token`

To use the cient, first instantiate an object of the client like so:

```java
IAMClient client = new IAMClient();
```

#### Application based requests

Register an application:

```java
String appName = ...;
IAMResponse response = client.registerApplication(appName);
```

Get an appId:

```java
String appName = ...;
IAMResponse response = client.getAppId(appName);
```

Update token validity period.

```java
String appId = ...;
int seconds = ...;
IAMResponse response = client.updateApplicationTime(appId, seconds);
```

#### User based requests:

Register a user:

```java
String appId = ...;
String name = ...;
String password = ...; // Evidently, be *very* careful with this variable
IAMResponse response = client.registerUser(appId, name, password);
```

Delete a user:

```java
String appId = ...;
String uid = ...;
IAMResponse response = client.deleteUser(appId, nuid);
```

#### Token based requests:

Generate a token:

```java
String appId = ...;
String uid = ...;
String password = ...; // Evidently, be *very* careful with this variable
IAMResponse response = client.generateToken(appId, uid, password);
```

Validate a token:

```java
String appId = ...;
String tokenString = ...;
IAMResponse response = client.validateToken(appId, tokenString);
```

Note: `!Warning!` If the token is valid and request goes through successfully, then the payload should contain a "Valid" message if the token is valid. However if the token is invalid or expired (which is also considered as being invalid) then a `IAMServerErrorException` will be thrown. This is still under development. `IAMServerErrorException` can still be thrown under normal circumstances. Care must be taken to differentiate the two cases as of this point in time. Work around under development.

Delete (invalidate) token:

```java
String appId = ...;
String tokenString = ...;
IAMResponse response = client.deleteToken(appId, tokenString);
```

Note: `!Warning!` If the token is valid and request goes through successfully, then the payload should contain a "Success" message if the token is valid. However if the token is invalid then a `IAMServerErrorException` will be thrown. This is still under development. `IAMServerErrorException` can still be thrown under normal circumstances. Care must be taken to differentiate the two cases as of this point in time. Work around under development.
