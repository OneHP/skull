angular.module('services',[])
angular.module('controllers',['services'])
angular.module('skull',['ngRoute','services','controllers'])