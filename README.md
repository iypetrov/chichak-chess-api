### Overview

### In-Depth Explanation
#### Game Flow
![Overview](docs/game-flow.png)
#### Websockets vs Short & Long Polling
#### Database Schema
![Overview](docs/database-schema.png)
#### API Endpoints
![Overview](docs/api-endpoints.png)
#### Infrastructure Overview
This demo project is deployed on AWS.

#### What Would I Change If This Was A Real Life Product

### When You Run The API Locally
You should create a `.env` file in the project's root directory. This is example content:
```
API_MAJOR_VERSION=1
API_MINOR_VERSION=0
API_PATCH_VERSION=0
AUTH_SECRET=kdjaksdjaskdjlasdjaskldjaskldjaskfjfiafoasfoafpqfjpqwefoewfjoewfjeghsiogjgopsdgsdiog
DB_URL=jdbc:mysql://localhost:3306/chessdb
DB_USER=admin
DB_PASSWORD=admin
```