# NuPay Mandate Service

A Spring Boot based microservice for creating and managing payment mandates.

The application exposes a REST API for mandate creation, stores mandate information in MySQL, and integrates with a NuPay API. During local development, a Dummy NuPay Service is used to simulate the external NuPay API.

## Technologies Used

* Java
* Spring Boot
* Spring Data JPA
* MySQL
* REST API
* Maven
* Docker
* Docker Compose
* Git and GitHub
* Postman

## Architecture

The application consists of three main components:

1. **NuPay Service** - Main Spring Boot application running on port `8082`.
2. **Dummy NuPay Service** - Simulates the external NuPay API and runs on port `8081`.
3. **MySQL** - Stores mandate information.

### Request Flow

```text
Client / Postman
       |
       | POST /api/mandates
       v
+----------------------+
|    NuPay Service     |
|       :8082          |
+----------+-----------+
           |
      +----+----+
      |         |
      v         v
+---------+  +------------------+
|  MySQL  |  |  Dummy NuPay API |
|  :3306  |  |      :8081       |
+---------+  +------------------+
```

### Docker Architecture

All three components run as Docker containers and communicate through the Docker Compose network.

```text
+------------------- Docker Network -------------------+

    nupay-service
        :8082
          |
          +--------> nupay-mysql
          |             :3306
          |
          +--------> dummy-nupay
                        :8081

+-------------------------------------------------------+
```

## Project Structure

```text
nupay_serviceAPI/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── smfg/
│   │   │       └── nupay_serviceAPI/
│   │   │           ├── controller/
│   │   │           ├── service/
│   │   │           ├── repository/
│   │   │           ├── entity/
│   │   │           ├── dto/
│   │   │           └── client/
│   │   │
│   │   └── resources/
│   │       └── application.yaml
│   │
│   └── test/
│
├── Dockerfile
├── docker-compose.yml
├── .dockerignore
├── .gitignore
├── pom.xml
└── README.md
```

### Main Components

#### Controller

The controller receives HTTP requests from clients.

Example:

```text
POST /api/mandates
```

#### Service

The service layer contains the application's business logic.

```text
Controller
    ↓
Service
    ↓
Business Logic
```

#### Repository

The repository layer communicates with MySQL through Spring Data JPA.

```text
Service
    ↓
Repository
    ↓
MySQL
```

#### Entity

The entity represents the database table and its fields.

For example, the `Mandate` entity represents mandate information stored in MySQL.

#### DTO

DTO means **Data Transfer Object**.

DTOs are used to transfer data between the API and application layers.

Examples:

```text
MandateRequest
MandateResponse
NuPayRequest
NuPayResponse
```

#### Client

The client component is responsible for communication with the external NuPay API.

```text
NuPay Service
      |
      ↓
  NuPay Client
      |
      ↓
NuPay API
```

During local development, the Dummy NuPay Service acts as the external API.

## API Endpoint

### Create Mandate

**Method:**

```text
POST
```

**Endpoint:**

```text
http://localhost:8082/api/mandates
```

### Request Example

```json
{
  "sourceRequestID": "REQ-10002",
  "customerId": "CUST-1001",
  "loanNumber": "LN-10001",
  "customerName": "John Doe",
  "customerEmail": "john@example.com",
  "customerMobile": "9876543210",
  "amount": 5000.00,
  "frequency": "MONTHLY",
  "startDate": "2026-10-01",
  "endDate": "2027-10-01",
  "debitDay": 5
}
```

### API Flow

When the client sends a mandate request:

```text
Client
   |
   ↓
POST /api/mandates
   |
   ↓
MandateController
   |
   ↓
MandateService
   |
   +-----------> MySQL
   |
   +-----------> Dummy NuPay API
   |
   ↓
MandateResponse
   |
   ↓
Client
```

## Environment Variables

Database credentials are stored in a local `.env` file.

Example:

```env
MYSQL_ROOT_PASSWORD=root
DB_USERNAME=root
DB_PASSWORD=root
```

The `.env` file is intentionally excluded from Git using `.gitignore`.

**Do not commit real passwords or secrets to GitHub.**

## Running the Application with Docker

### Prerequisites

Install:

* Docker Desktop
* Git

### Start All Services

From the project directory:

```powershell
docker compose up -d
```

This starts:

```text
nupay-service
dummy-nupay
nupay-mysql
```

### Check Running Containers

```powershell
docker ps
```

Expected services:

```text
nupay-service
dummy-nupay
nupay-mysql
```

### Stop Services

```powershell
docker compose down
```

### Rebuild Services

If the source code or Docker configuration changes:

```powershell
docker compose up --build -d
```

## Service Ports

| Service       | Port |
| ------------- | ---: |
| NuPay Service | 8082 |
| Dummy NuPay   | 8081 |
| MySQL         | 3306 |

MySQL is available internally to the Docker network and does not need to be exposed to the Windows host.

## Docker Networking

Docker Compose creates a private network for the services.

The NuPay Service communicates with MySQL using:

```text
mysql:3306
```

The NuPay Service communicates with the Dummy NuPay API using:

```text
dummy-nupay:8081
```

The service names are resolved automatically by Docker's internal DNS.

Therefore, containers should use service names instead of `localhost` for container-to-container communication.

## Git and GitHub

The project is maintained using Git and hosted on GitHub.

Basic workflow:

```powershell
git status

git add .

git commit -m "Your commit message"

git push origin main
```

Sensitive files such as `.env` are excluded using `.gitignore`.

## Testing

The API can be tested using Postman.

Example:

```text
POST http://localhost:8082/api/mandates
```

The request is processed by the NuPay Service, stored in MySQL, and sent to the Dummy NuPay Service for external API simulation.

## Future Improvements

Possible future improvements include:

* Authentication and authorization
* Centralized configuration
* API Gateway integration
* Real NuPay API integration
* Unit and integration testing
* CI/CD pipeline
* Kubernetes deployment
* Monitoring and logging
