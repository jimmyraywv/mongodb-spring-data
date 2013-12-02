function( key, value ){
	value.avg = Math.round(100*(value.ttlSalary / value.count))/100;
  	return value;
}