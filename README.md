# 📄 Evision

## Project Overview

**Evision** is a modular, scalable Spring Boot application for:

- **File Indexing:** Efficiently reads and indexes extremely large text files (millions of words) using chunking for memory safety.
- **Similarity Detection:** Computes file similarity using both **Redis** and **Elasticsearch** (leveraging the Strategy pattern).
- **RESTful APIs:** Exposes endpoints for file generation, indexing, and similarity queries.
- **Robust Error Handling:** Centralized, global exception handling provides unified API responses.

---

## 🗂️ Main Modules & Responsibilities

### 1. `ApiResponse`
- A standardized wrapper for API responses: includes `timestamp`, `status`, `message`, and generic `data`.

### 2. `config`
- **RedisConfig:** Sets up the Redis client for connecting to Redis instances.

### 3. `Controller`
- **FileIndexingController:** Endpoints to index files into Elasticsearch or Redis.
- **FileGeneratorController:** APIs for generating sample files with random/common words.
- **FileSimilarityController:** APIs for similarity computation using the selected engine.

### 4. `Dto`
- **FileSimilarityResult:** DTO for similarity comparison results.

### 5. `GlobalException`
- **GlobalExceptionHandler:** Centralized exception handler that returns consistent `ApiResponse` JSON on errors.

### 6. `Models`
- **FileDocument:** Model for storing files in Elasticsearch (single-chunk or chunked).

### 7. `Repositories`
- **FileDocumentRepository:** Spring Data ES repository for file documents.

### 8. `Services/FileGenerator`
- **FileGeneratorService:** Business logic for generating files (random/controlled/common words).

### 9. `Similarity`
- **EsSimilarityStrategy:** Uses Elasticsearch search/scoring for similarity.
- **FileSimilarityFactory:** Chooses which engine (Redis/ES) to use at runtime.
- **RedisJaccardSimilarity:** Uses Jaccard coefficient (based on Redis sets) for fast, memory-safe similarity.

### 10. `utils`
- **FileReaderUtil:** Reads files efficiently (line-by-line, chunked) to prevent memory issues.

### 11. `DemoApplication`
- Spring Boot application entry point.

---

## 🔄 Application Flow

### 1. **File Generation**
- Call API to generate files (you set number of words).
- Files are saved under `resources/Files`.

### 2. **File Indexing**
- Index files to **Elasticsearch** (as documents) or **Redis** (as sets of words).
- Reads files in chunks for memory efficiency.

### 3. **Similarity Query**
- API accepts two file names + engine (Redis/Elasticsearch).
- Strategy chosen at runtime:
  - **Elasticsearch:** Uses “More Like This” query or custom scoring.
  - **Redis:** Loads file words as sets, calculates Jaccard similarity.
- Returns results as `ApiResponse<FileSimilarityResult>`.

### 4. **Error Handling**
- All errors are handled globally by `GlobalExceptionHandler` using `@ControllerAdvice`.

---

## 🏗️ Key Design Patterns Used

- **Strategy Pattern:**  
  `FileSimilarityStrategy` interface with ES and Redis implementations.

- **Factory Pattern:**  
  `FileSimilarityFactory` selects similarity engine.

- **Global Exception Handler:**  
  `@ControllerAdvice` for unified error handling.

- **Repository Pattern:**  
  For ES storage via `FileDocumentRepository`.

---

## 🚀 Using Redis Set Operations for Jaccard Similarity: Efficient, Scalable, and Memory-Safe

### Why Redis is a Perfect Fit for Jaccard Similarity

- **In-Memory Computation:**  
  Redis stores all sets in RAM, enabling ultra-fast set ops.

- **Built-In Set Operations:**  
  Commands like `SINTER` (intersection), `SUNION` (union), and `SCARD` (count) are natively supported.

- **No Java Memory Overhead:**  
  Heavy computations done inside Redis, not in the Java process.

- **Scalable:**  
  Redis handles sets with millions of words efficiently.

---

### How Jaccard Similarity Works with Redis

The **Jaccard similarity** between two files (represented as Redis sets of unique words) is calculated as:

\[
\text{Jaccard}(A, B) = \frac{|A \cap B|}{|A \cup B|}
\]

Where:  
- \( A \), \( B \) = Redis sets of unique words from each file  
- \( |A \cap B| \) = Number of words both files share  
- \( |A \cup B| \) = Total unique words in both files

**Redis Commands Used:**
- `SINTER` to get intersection
- `SUNION` to get union
- `SCARD` to count elements

**All operations are performed in Redis (no Java memory bottleneck)!**

---

## 📚 Example API Usage

- `POST /api/generate-file?wordCount=5000000`  
  → Generates a file with 5 million words.
- `POST /api/index?engine=redis&fileName=file1.txt`  
  → Indexes file in Redis.
- `GET /api/similarity?file1=file1.txt&file2=file2.txt&engine=redis`  
  → Calculates Jaccard similarity in Redis.

---

## ⚠️ Notes & Best Practices

- Do **not** upload files larger than 100MB directly to GitHub. Use [Git LFS](https://git-lfs.github.com/) or cloud storage.
- For extremely large files, always use chunked reading and avoid loading all content into memory.
- Redis and Elasticsearch can be run locally using `docker-compose.yml` provided.

---

## 🛠️ Setup

1. **Clone Repo**  
   `git clone ...`

2. **Run Docker Compose**  
   `  docker-compose up -d `


   **IMPORTANT:**  
Ensure you build the project to get the JAR file before running `docker-compose up`.  
Use:
```bash
mvn clean package
