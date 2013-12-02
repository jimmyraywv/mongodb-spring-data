function(key, values) {
	var data = {count: 0, ttlSalary: 0};

	values.forEach(function(doc) {
			data.ttlSalary += doc.ttlSalary; 
      		data.count += doc.count;			
	});

	return data;
};