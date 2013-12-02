function () { 
    key = this.ttl; 
    emit( key, {count: 1, ttlSalary: this.pay} ) ;
}