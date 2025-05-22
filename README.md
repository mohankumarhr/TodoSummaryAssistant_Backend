# 🚀 Todo Summary Assistant - Backend

This is the backend service for the **Todo Summary Assistant** project.  
Built using **Spring Boot (Java 21)** and connected to a **MySQL database**, it powers the APIs consumed by the React frontend.

---

## 🌐 Live Frontend

🔗 [https://todosummaryapp.netlify.app/](https://todosummaryapp.netlify.app/)

> ⚠️ **Note**: The backend is hosted on [Render](https://render.com) using a free plan.  
> The **first request after inactivity may take 2–3 minutes** to respond while the server spins up.

---

## 🛠️ Backend Setup (Spring Boot)

### Prerequisites

- **Java 21**
- **Maven**
- **MySQL** (local or cloud-hosted like [Railway](https://railway.app))

---

### ✅ Configure `application-dev.properties`

Create or edit the file `src/main/resources/application-dev.properties` with the following content:

```properties
spring.datasource.url=YOUR_DATABASE_URL
spring.datasource.username=YOUR_DATABASE_USERNAME
spring.datasource.password=YOUR_DATABASE_PASSWORD

GEMINI_API_KEY=YOUR_GEMINI_API_KEY
SLACK_WEBHOOK_URL=YOUR_SLACK_WEBHOOK_URL

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

📦 Install Dependencies
Maven will automatically download and manage all required dependencies.

▶️ Start the Development Server
mvn spring-boot:run
Once running, the backend server will be available at:
📍 http://localhost:8080
