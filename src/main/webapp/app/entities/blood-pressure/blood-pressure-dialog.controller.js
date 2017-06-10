(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .controller('BloodPressureDialogController', BloodPressureDialogController);

    BloodPressureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BloodPressure', 'User'];

    function BloodPressureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BloodPressure, User) {
        var vm = this;

        vm.blood_pressure = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.blood_pressure.id !== null) {
                BloodPressure.update(vm.blood_pressure, onSaveSuccess, onSaveError);
            } else {
                BloodPressure.save(vm.blood_pressure, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('21PointsApp:blood_pressureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
