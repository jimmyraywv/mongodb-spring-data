use HRDB
db.system.user.find()
db.addUser({user:"jimmy", pwd:"password", roles:["readWrite"]})
