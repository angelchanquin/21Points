(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('BloodPressureDeleteController',BloodPressureDeleteController);

    BloodPressureDeleteController.$inject = ['$uibModalInstance', 'entity', 'BloodPressure'];

    function BloodPressureDeleteController($uibModalInstance, entity, BloodPressure) {
        var vm = this;

        vm.blood_pressure = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BloodPressure.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
