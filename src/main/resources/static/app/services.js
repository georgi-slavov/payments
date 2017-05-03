(function(angular) {
	var PaymentFactory = function($resource) {
		return $resource('/payments/:id', {
			id : '@id'
		}, {
			update : {
				method : "PUT"
			},
			remove : {
				method : "DELETE"
			}
		});
	};

	PaymentFactory.$inject = [ '$resource' ];
	angular.module("myApp.services").factory("Payment", PaymentFactory);
}(angular));