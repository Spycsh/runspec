# RunSpec
*specialize your running plan*

Sihan Chen sihanc@kth.se  
Yuehao Sui yuehao@kth.se  
Zidi Chen zidi@kth.se

## Workflow

android (open app)
-> adviser (weather and air information)
-> android (display weather and air information)
-> android (start running) 
-> producer (kafka collects data) 
-> processor (process data: how hot the predefined spots are) [display in statisticsboard] 
-> android (display steps, length and heat of predefined spots)

## Planed API:
`adviser`: one api for weather and air info
`producer`: pass data to kafka



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





 
