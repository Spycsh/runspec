# RunSpec
*specify and specialize your running plan*

> Sihan Chen sihanc@kth.se  
Yuehao Sui yuehao@kth.se  
Zidi Chen zidi@kth.se

Our project is divided into three parts:

## Running Data producer

The sensor should track following `real-time` data on an Android phone or some intelligent wristband.
 * userId
 * longitude
 * latitude
 * altitude (optional)
 * timestamp
 * stepCount
 * distance
 * heartRate (optional)
 
The above data should be sent to server and analyzed on the server and through some calculation, some advice will be feedback to the frondend clients.

Considering the voluminous real-time messages produced from different runners. We plan to use **Kafka** as a message queue to keep the messages. In that case, the server can consume the message one by one and exert calculation on the data to offer reasonable advices to the runners. 


## Running Data processor

During the calculation, we plan to use **Spark** as the distributed computing component because of its powerful processing capacity of streaming data.

We will firstly judge whether it is appropriate to run. The initial idea is to get the air quality data from [API](https://aqicn.org/city/sweden/stockholm-lilla-essingen/) with the latitude and longitude produced by the sensor. Then, we can research some papers and code the relationship between a healthy run and factors such as AQI (air quality, PM2.5), humidity, temperature, heart rate. For example, if we find a user's region has serious air quality (PM2.5 index > 100), then we will write in the advice as a feedback to the user. Also the server may calculate the speed and distance and return the result to frontend.

We also use Spark to store the voluminous data into the MongoDB database (Maybe Redis will be used to cache the data). From the database runners can extract the running history (optional). Of course a login function must be provided to differentiate between different users.

## Android Dashboard
An android App will be created to read the data from the sensor in the mobile phone and give the user a operating interface and also display the feedback. When the user wants to start running, he/she may first click a query button and it will send the runner's data to the backend to process and give feedback to the runner that whether it is appropriate to run based on the weather of current location. After the user start to run, for every 10 seconds, the user data will be sent to the backend and processed and the feedback will show the speed and the total distance that the user has runned. When the user end running, he will choose to store or delete this trip data in the database.





 