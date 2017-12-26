var app = angular.module('StockApp', ['ui.grid','ui.grid.pagination']);

app.filter('currencyFilter', function () {
    return function (value) {
        return value.amount+ ' '+ value.currency;
    }
});

app.controller('StockCtrl', ['$scope','StockService',
    function ($scope, StockService) {
        var paginationOptions = {
            pageNumber: 1,
            pageSize: 5,
        sort: null
        };

    StockService.getStocks(
      paginationOptions.pageNumber,
      paginationOptions.pageSize).success(function(data){
        $scope.gridOptions.data = data.content;
        $scope.gridOptions.totalItems = data.totalElements;
      });

    $scope.gridOptions = {
        paginationPageSizes: [5, 10, 20],
        paginationPageSize: paginationOptions.pageSize,
        enableColumnMenus:false,
    useExternalPagination: true,
        columnDefs: [
           { name: 'Stock', field: 'name' },
           { name: 'Price', field: 'price', cellFilter: 'currencyFilter' },
           { name: 'Last Update', field: 'updatedAt', cellFilter: 'date : \'dd-MM-yyyy HH:mm\'' }
        ],
        onRegisterApi: function(gridApi) {
           $scope.gridApi = gridApi;
           gridApi.pagination.on.paginationChanged(
             $scope,
             function (newPage, pageSize) {
               paginationOptions.pageNumber = newPage;
               paginationOptions.pageSize = pageSize;
               StockService.getStocks(newPage,pageSize)
                 .success(function(data){
                   $scope.gridOptions.data = data.content;
                   $scope.gridOptions.totalItems = data.totalElements;
                 });
            });
        }
    };
}]);

app.service('StockService',['$http', function ($http) {

    function getStocks(pageNumber,size) {
        pageNumber = pageNumber > 0?pageNumber - 1:0;
        return $http({
          method: 'GET',
            url: 'api/stocks?page='+pageNumber+'&size='+size
        });
    }
    return {
        getStocks: getStocks
    };
}]);