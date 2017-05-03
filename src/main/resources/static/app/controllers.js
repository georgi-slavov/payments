(function(angular) {
	var AppController = function($scope, Payment) {
		Payment.query(function(response) {
			$scope.payments = response ? response : [];
		});

		$scope.addPayment = function(description, amount) {
			new Payment({
				amount : amount,
				description : description
			}).$save(function(payment) {
				$scope.payments.push(payment);
			});
			$scope.newPayment = "";
		};

		$scope.updatePayment = function(payment) {
			payment.$update();
		};

		$scope.deletePayment = function(payment) {
			payment.$remove(function() {
				$scope.payments.splice($scope.payments.indexOf(payment), 1);
			});
		};
	};

	AppController.$inject = [ '$scope', 'Payment' ];
	angular.module("myApp.controllers").controller("AppController",
			AppController);
}(angular));