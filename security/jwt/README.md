# Java EE Security 1.0 JWT Example
Sample to demonstrate JWT integration with Java EE Security 1.0 (Soteria RI)

### Sample Users
Username | Password | Roles
--- | --- | ---
payara | fish | ADMIN_ROLE, USER_ROLE
duke | secret | USER_ROLE

### Login EndPoint
http://localhost:8080/security-jwt-example/api/auth/login?name=duke&password=secret&rememberme=false

### Protected REST EndPoint

EndPoint | HTTP Method | Roles Allowed
--- | --- | ---
http://localhost:8080/security-jwt-example/api/sample/read | GET | ANONYMOUS, USER_ROLE, ADMIN_ROLE
http://localhost:8080/security-jwt-example/api/sample/write | POST | USER_ROLE, ADMIN_ROLE
http://localhost:8080/security-jwt-example/api/sample/delete | DELETE | ADMIN_ROLE


#### rememberme=false

Whenever the user wants to access a protected resource, the user agent send the JWT in the Authorization header using the Bearer schema. The content of the header should look like the following:

`Authorization: Bearer <token>`

This is a stateless authentication mechanism as the user state is never saved in server memory. 
The server's protected routes will check for a valid JWT in the Authorization header, and if it's present, the user will be allowed to access protected resources.

#### rememberme=true
Whenever the user wants to access a protected resource, the user agent would automatically include the JWT in the cookie with `JREMEMBERMEID` key. 
It does not require state to be stored on the server because the JWT encapsulates everything the server needs to serve the request.