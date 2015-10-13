mongodb-spring-data

-Dspring.profiles.active=local

#Create user in MongoDB
use admin

db.createUser(
  {
    user: "jimmy",
    pwd: "password",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
  }
)

#YAML Config File
#/usr/local/etc/mongod.conf
systemLog:
  destination: file
  path: /usr/local/var/log/mongodb/mongo.log
  logAppend: true
storage:
  dbPath: /usr/local/var/mongodb
net:
  port: 29009
  bindIp: 127.0.0.1
  http: 
      enabled: true
      JSONPEnabled: true
      RESTInterfaceEnabled: true