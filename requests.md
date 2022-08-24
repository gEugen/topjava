# _Requests_
___
*all presented commands can be copied and pasted into the terminal command line, run, after which the results will 
be obtained*
___
## _1. MealRestController Curl Requests_
### _1.1. Get all user meals with GET Request_
[//]: # (GetAll Request)
> curl -L -X GET "http://localhost:8080/topjava_war_exploded/rest/meals"

### _1.2. Create new meal with POST Request_
[//]: # (Create Request)
> curl -L -X POST "http://localhost:8080/topjava_war_exploded/rest/meals" --header "Content-Type: application/json" --data-raw "{\"id\":null, \"dateTime\":\"2022-02-02T18:18:00\", \"description\":\"Created dinner\", \"calories\":2000}"

### _1.3. Get user meal by meal ID with GET Request_
[//]: # (Get Request)
> curl -L -X GET "http://localhost:8080/topjava_war_exploded/rest/meals/100003"

### _1.4. Update user meal by meal ID with PUT Request_
[//]: # (Update Request)
> curl -L -X PUT "http://localhost:8080/topjava_war_exploded/rest/meals/100003" --header "Content-Type: application/json" --data-raw "{\"id\":100003,\"dateTime\":\"2020-01-30T10:10:00\", \"description\":\"Updated breakfast\", \"calories\":200}"

### _1.5. Delete user meal by meal ID with DELETE Request_
[//]: # (Delete Request)
> curl -L -X DELETE "http://localhost:8080/topjava_war_exploded/rest/meals/100006"

### _1.6. Get user meals with separate filtering by time/date with GET Request_
[//]: # (GetBetween Request)
> curl -L -X GET "http://localhost:8080/topjava_war_exploded/rest/meals/between?startDate=2020-01-30&endDate=2020-01-31&startTime=10:00&endTime=13:00"