# Rollback Manager

Run Services: 

```
git clone https://gitlab.com/smartfoxpro_repo/study/done/rollback-manager.git
cd rollback-manager
mvn clean package
cd eureka-server
java -jar target/eureka-server-1.0-SHAPSHOT.jar

//open new window terminal or cmd
// from folder 'rollback-manager' run 
cd config-server
java -jar target/config-server-1.0-SHAPSHOT.jar

//open new window terminal or cmd
// from folder 'rollback-manager' run 
cd gateway
java -jar target/gateway-1.0-SHAPSHOT.jar

//open new window terminal or cmd
// from folder 'rollback-manager' run 
cd rollback-service
java -jar target/rollback-service-1.0-SHAPSHOT.jar

//open new window terminal or cmd
// from folder 'rollback-manager' run 
cd user-service
java -jar target/user-service-1.0-SHAPSHOT.jar
```

####Create user       
Open Postman - send POST request http://localhost:8081/user/save?txId=1  
(txId - number transaction)
For Example:

```
{
    "name" : "aaa",
    "email" : "aaa@gmail.com"
}
```

####Update user      
Open Postman - send POST request http://localhost:8081/user/update?txId=1  
(txId - number transaction)
For Example:

```
{
    "id" : "8dfacfb3-2bcc-4a59-bea4-ebf90d066ed3",
    "name" : "aaa",
    "email" : "aaa@gmail.com"
}
```
Save users until an error occurs.  
When an error occurs, we see that the database has rolled back.
 

