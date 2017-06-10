(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('Blood_pressureDetailController', Blood_pressureDetailController);

    Blood_pressureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Blood_pressure', 'User'];

    function Blood_pressureDetailController($scope, $rootScope, $stateParams, previousState, entity, Blood_pressure, User) {
        var vm = this;

        vm.bloodPressure = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('21PointsApp:bloodPressureUpdate', function(event, result) {
            vm.bloodPressure = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
