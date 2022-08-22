[//]: # (GetAll Request)
curl --location --request GET 'http://localhost:8080/topjava_war_exploded/rest/meals'

[//]: # (Create Request)
curl --location --request POST 'http://localhost:8080/topjava_war_exploded/rest/meals' \
--header 'Content-Type: application/json' \
--data-raw '{
"id":null,
"dateTime":"2022-08-22T20:46:28",
"description":"1000",
"calories":1000
}'

[//]: # (Get Request)
curl --location --request GET 'http://localhost:8080/topjava_war_exploded/rest/meals/100003'

[//]: # (Update Request)
curl --location --request PUT 'http://localhost:8080/topjava_war_exploded/rest/meals/100015' \
--header 'Content-Type: application/json' \
--data-raw '{
"id":100015,
"dateTime":"2022-08-22T20:46:28",
"description":"updated",
"calories":1001
}'

[//]: # (Delete Request)
curl --location --request DELETE 'http://localhost:8080/topjava_war_exploded/rest/meals/100015'

[//]: # (GetBetween Request)
curl --location --request GET 'http://localhost:8080/topjava_war_exploded/rest/meals/between?startDate=2020-01-30&endDate=2020-01-30&startTime&endTime'