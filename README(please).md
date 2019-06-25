# Getting Started

### Goal
The goal is to write a parser in Java that parses web server access log file, loads the log to MySQL and checks if any IP makes more than a certain number of requests for the given duration. 

### Approach
There are a lot of ways to approach this problem with Java. I decided to use Spring and process it as a batch job with JDBC.
Definitely plain vanilla java is more than enough but gotta love Spring :)

** (JPA & Hibernate could be another option - though overkill)

### Deliverables
The following deliverables were requested:

* `parser.jar`: Located in folder 'target'
* source code: Located in folder `src`
* SQL schema: `schema.sql` -> Located in folder 'SQL'
* SQL queries: `queries.sql` -> Located in folder 'SQL'

### Start

* use one of the helper scripts - life should be easy:
    - for Unix based machines: `process.sh`
    - for Windows based machines: `process.bat`


### Assumptions
* `accesslog` property is optional. If not provided analyze the test file
* `threshold` property is optional. If not provided, assumes `200` for `hourly` duration and `500` for `daily` duration

#### Testing
...could be more, but pressured for time