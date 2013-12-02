db["employees"].ensureIndex({"title":"text"})

db.collection.runCommand( "text", { search: <string>,
    filter: <document>,
    project: <document>,
    limit: <number>,
    language: <string> } )
    
db.employees.runCommand( "text", { search: "senior" } )
