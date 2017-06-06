(function() {
    'use strict';

    angular
        .module('21PointsApp')
        .factory('Blood_pressureSearch', Blood_pressureSearch);

    Blood_pressureSearch.$inject = ['$resource'];

    function Blood_pressureSearch($resource) {
        var resourceUrl =  'api/_search/blood-pressures/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
