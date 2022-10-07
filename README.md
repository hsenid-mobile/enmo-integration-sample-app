# Sample Application Using Enmo Content Broadcast API

## Pre-requisites

* This sample application is written using Spring Boot Framework and Maven. 
* You need following installed and configured in your machine
  * Java 17 - https://adoptium.net/download
  * Maven 3.6+ - https://maven.apache.org/download.cgi

## How to run

* Run `mvn spring-boot:run`. This will start the application in port `8080`.
* You can trigger content publishing using simple `CURL` command.
```
curl http://localhost:8080/test
```
## Sample Code

* Refer `hms.enmo.examples.EnmoConnector` to see how to initiate API call to Enmo.
* API Authentication is achieved via Spring WebClient. Configuration for authentication can be found in `hms.enmo.examples.Oauth2ClientConfig`
* WebClient configuration is at `hms.enmo.examples.WebClientConfig`
* All the configuration properties can be found in `application.yml`

---

## API Documentation

* Import the Postman collection `Enmo Public API.postman_collection.json` to quickly test the API.

### Authentication

Enmo uses OAuth-2 for authentication. You can get access token by using OAuth-2 password based authentication mechanism.
Following is a sample CURL command.

```
curl \
  -d "client_id=content-wizard-web" \
  -d "username=testuser" \
  -d "password=testpassword" \
  -d "grant_type=password" \
  "https://clacks.hsenidmobile.com/auth/realms/clacks/protocol/openid-connect/token"
```

#### Authenticate Request

Authentication request is a HTTP POST request with `application/x-www-form-urlencoded` body.

* URL: `https://clacks.hsenidmobile.com/auth/realms/clacks/protocol/openid-connect/token`
* Headers: 

```
Content-Type: application/x-www-form-urlencoded
```

##### Body parameters

| Param Name | Description  | Sample Value |
| ---------- | ------------ | ------------ |
| client_id  | Authentication Client Id. Use the given sample value. | content-wizard-web |
| username   | SP authentication username to Enmo Content Wizard | test-sp |
| password   | SP authentication password to Enmo Content Wizard | testpassword |
| grant_type | OAuth 2 grant type. We use `password` grant type | password |

Eg:

```
client_id=content-wizard-web&username=sunimali&password=1234&grant_type=password
```


### Authentication Response

##### Response parameters

| Param Name | Description                                                                                  | Sample Value |
| ---------- |----------------------------------------------------------------------------------------------| ------------ |
|  access_token | Authentication token to be used with enmo API calls                                          | |
| refresh_token | Token to be used to get refreshed authentication token                                       ||
| expires_in | Authentication token expiry time in secons. You need to update the token with refresh token. | 300 |
| refresh_expires_in | Refresh token expiry time in secons                                                          | 300 |
| 

Eg: 

```json
{
  "access_token": "eyJhbGciOiJSUz...yIgK9uDA",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJIUzI1Ni...8N4HyqprJZCkrQ",
  "token_type": "bearer",
  "not-before-policy": 0,
  "session_state": "06c4989b-5ce6-432a-95af-cc7fc4f4aa9d",
  "scope": "profile email"
}
```

#### Refresh Token Flow

* URL: `https://clacks.hsenidmobile.com/auth/realms/clacks/protocol/openid-connect/token`
* Header 

```
Content-Type: application/x-www-form-urlencoded;charset=UTF-8
Accept: application/json
Authorization: Basic Y29udGVudC13aXphcmQtd2ViOg==
```
##### Body parameters

| Param Name | Description                                              | Sample Value  |
| ---------- |----------------------------------------------------------|---------------|
| refresh_token  | Refresh token from previous password authentication flow |               |
| grant_type | OAuth 2 refresh token grant type.                        | refresh_token |

Response to this API call will be same as the response for password authentication flow.


### Content Publish API

Content publish API is developed using GraphQL. To publish content you have to use `createContent` mutation. 


* URL : `https://clacks.hsenidmobile.com/portal-api/graphql`
* API Spec: Refer the [documentation](enmo_content_publish_api_spec/index.html)
* GraphQL Schema: [enmo_content_publish_api_schema.graphql](enmo_content_publish_api_schema.graphql)

