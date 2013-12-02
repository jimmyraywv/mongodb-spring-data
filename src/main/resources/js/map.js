var map = function() {
	emit(this.title, {
		pay : this.salary
	});
};

var reduce = function(key, values) {
	var sum = 0;
	var ave = 0;
	var count = 0;
	values.forEach(function(doc) {
		count++;
		sum += doc.salary;
	});
	return {
		salary : sum/count
	};
};

db.employees.mapReduce(
        mapFunction,
        reduceFunction,
        { out: { inline: "map_reduce_example" } }
      )