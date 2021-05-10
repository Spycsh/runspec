# RunSpec

*specialize your running*

Sihan Chen sihanc@kth.se  
Yuehao Sui yuehao@kth.se  
Zidi Chen zidi@kth.se

![architecture](arch.JPG)

## Workflow

android (open app)

⇨ adviser (weather and air information, hot spots)

⇨ android (display weather and air information)

⇨ android (start running) 

⇨ producer (kafka collects data) 

⇨ processor (process data: how hot the predefined spots are) [display in statisticsboard] 

⇨ android (display steps, length and checklist of predefined spots)

## API:

> One tripId for one run.

- `http://localhost:8082/adviser/info`

    - POST
    
    - two parameters(not JSON): longitude, latitude
    
    - in `adviser` module

    - return a JSON string with weather and air information

- `http://localhost:8182/producer/runningData`
 
    - POST

    - JSON string: "longitude", "latitude", "tripId", "userId"

- `http://localhost:8182/producer/returnTripData`

    - POST
    
    - JSON string: "userId", "tripId"
    
    - return POIs (with latitude, name, pOIId, radius, longitude) which current user passes by in the current trip in ***JSON***

- `http://localhost:8182/producer/hotSpot`

    - POST

    > no param: we still use post here because we might need to post latitude and longitude to check the city for searching hottest spots ***in the future***.

    - return 5 top hottest POIs in ***JSON*** (with latitude, name, pOIId, radius, longitude, count)

## To run
This project is compatible with Java 11 and above.

1. start Kafka

one terminal: 
```
C:\kafka\bin\windows>zookeeper-server-start.bat ..\..\config\zookeeper.properties
```

another terminal:
```
C:\kafka\bin\windows>kafka-server-start.bat ..\..\config\server.properties
```

2. start mongodb

```
C:\MongoDB\bin>mongod --dbpath=C:\MongoDB\data\db 
```

3. run the project

**Please install `mvn` tool firstly.**

* adviser:
```shell
cd ./adviser
mvn spring-boot:run
```

* statisticsboard: 
```shell
cd ./statisticsboard
mvn spring-boot:run
```

* processor: 
```shell
cd ./processor
mvn exec:java
```

* producer: 
```shell
cd ./producer
mvn exec:java
```

## JMeter Load test
Here we can use JMeter to test the concurrency performance. For example, we set the default location to be KTH Kista as our testing POI data as follows:

```json
{
	"longitude": 17.94948, 
	"latitude": 59.40498, 
	"tripId": ${__Random(100,900,tripId)}, 
	"userId": ${__Random(1000,90000,userId)}
}

```

In JMeter, create a `ThreadGroup` with 200 threads (users) in 1 loop. In the `HTTP Request` enter the above json in the request body. Meanwhile, in the `HTTP Header Manager`, specify the header `Content-Type` to be `application/json;charset=UTF-8`.

## To do

- [x] basic infrastructure, producer+processor (maven), adviser+statisticsboard (maven springboot), android (gradle)
- [x] runner data REST api with reslet
- [x] connect Kafka producer, processor, specify json (de)serializer
- [x] spark filter/store runner data stream into mongo
- [x] POI points popularity measurement with POI id, user id and trip id
- [x] backend statistics board, subscribe messages with stomp.js, map display with leaflet.js
- [x] change packages dependencies to adapt to Java 11
- [x] add api for weather and air
- [x] add api for top five spots
- [x] add api to return the pois which the users passed by
- [x] android get data from backend
- [x] android UI demo
- [ ] user login
- [ ] running trace history
- [ ] history of spots that a user has passed by

[comment]: <> (Our project is to create a running App which records and displays real-time runners' running data &#40;produced by the built-in sensor of a mobile phone&#41; on his/her mobile phone and and offer appropriate running advices based on the running data. The main technology stack involves but is not limitted to Android, Kafka, Spark, MongoDB. The implementation can be divided into three parts:)

[comment]: <> (## Running Data producer)

[comment]: <> (The sensor should track following `real-time` data on an Android phone or some intelligent wristband.)

[comment]: <> ( * userId)

[comment]: <> ( * longitude)

[comment]: <> ( * latitude)

[comment]: <> ( * altitude &#40;optional&#41;)

[comment]: <> ( * timestamp)

[comment]: <> ( * stepCount)

[comment]: <> ( * distance)

[comment]: <> ( * heartRate &#40;optional&#41;)
 
[comment]: <> (The above data should be sent to server and analyzed on the server and through some calculation, some advice will be feedback to the frondend clients.)

[comment]: <> (Considering the voluminous real-time messages produced from different runners. We plan to use **Kafka** as a message queue to keep the messages. In that case, the server can consume the message one by one and exert calculation on the data to offer reasonable advices to the runners. )


[comment]: <> (## Running Data processor)

[comment]: <> (During the calculation, we plan to use **Spark** as the distributed computing component because of its powerful processing capacity of streaming data.)

[comment]: <> (We will firstly judge whether it is appropriate to run. The initial idea is to get the air quality data from [API]&#40;https://aqicn.org/city/sweden/stockholm-lilla-essingen/&#41; with the latitude and longitude produced by the sensor. Then, we can research some papers and code the relationship between a healthy run and factors such as AQI &#40;air quality, PM2.5&#41;, humidity, temperature, heart rate. For example, if we find a user's region has serious air quality &#40;PM2.5 index > 100&#41;, then we will write in the advice that it is not suitable to run at that time as a feedback to the user. Also the server may calculate the speed and distance and return the result to frontend.)

[comment]: <> (We also use Spark to store the voluminous data into the MongoDB database &#40;Maybe Redis will be used to cache the data&#41;. From the database runners can extract the running history &#40;optional&#41;. Of course a login function must be provided to differentiate between different users.)



[comment]: <> (## Android Dashboard)

[comment]: <> (An android App will be created to read the data from the sensor in the mobile phone and give the user a operating interface and also display the feedback. When the user wants to start running, he/she may first click a query button and it will send the runner's data to the backend to process and give feedback to the runner that whether it is appropriate to run based on the weather of current location. After the user start to run, for every 10 seconds &#40;may be adjusted in real development&#41;, the user data will be sent to the backend and processed and the feedback will show the speed and the total distance that the user has runned. When the user end running, he can choose to store or delete this trip data in the database.)





 
