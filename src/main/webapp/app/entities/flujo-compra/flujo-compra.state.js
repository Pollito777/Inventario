(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('flujo-compra', {
            parent: 'entity',
            url: '/flujo-compra',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FlujoCompras'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flujo-compra/flujo-compras.html',
                    controller: 'FlujoCompraController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('flujo-compra-detail', {
            parent: 'entity',
            url: '/flujo-compra/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FlujoCompra'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flujo-compra/flujo-compra-detail.html',
                    controller: 'FlujoCompraDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FlujoCompra', function($stateParams, FlujoCompra) {
                    return FlujoCompra.get({id : $stateParams.id});
                }]
            }
        })
        .state('flujo-compra.new', {
            parent: 'flujo-compra',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flujo-compra/flujo-compra-dialog.html',
                    controller: 'FlujoCompraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                estado: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('flujo-compra', null, { reload: true });
                }, function() {
                    $state.go('flujo-compra');
                });
            }]
        })
        .state('flujo-compra.edit', {
            parent: 'flujo-compra',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flujo-compra/flujo-compra-dialog.html',
                    controller: 'FlujoCompraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FlujoCompra', function(FlujoCompra) {
                            return FlujoCompra.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('flujo-compra', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flujo-compra.delete', {
            parent: 'flujo-compra',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flujo-compra/flujo-compra-delete-dialog.html',
                    controller: 'FlujoCompraDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FlujoCompra', function(FlujoCompra) {
                            return FlujoCompra.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('flujo-compra', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
