Secure User Management System with JWT
🔹 Objective: Build a Spring Boot API that allows users to register, login, update profiles,
fetch all users, and implement JWT-based authentication.
🔹 Key Topics Covered:
✅ Spring Security
✅ JWT Authentication
✅ Role-Based Access Control (RBAC)
✅ Uploading & Retrieving Profile Pictures(using a bucket preferably s3 or gcp)
✅ CRUD Operations on Users


***Task Requirements***

1. User Registration & Authentication
   ○ Register a new user with username, email, password, and profile
   picture.
   ○ Login using JWT authentication.
   ○ JWT should be required for accessing protected endpoints. Like getAllUsers amd
   editUsers etc

2. User Management
   ○ GET /users → Get a list of all users.
   ○ GET /users/{id} → Get a single user by ID.
   ○ PUT /users/{id} → Update a user's profile.
   ○ DELETE /users/{id} → Delete a user.

3. Profile Picture Handling
   ○ Upload a profile picture as MultipartFile.
   ○ Retrieve a profile picture via a URL

4. Security & Roles
   ○ Implement User and Admin roles(more if you can).
   ○ User can update their own profile but cannot access other users.
   ○ Admin can view and manage all users.