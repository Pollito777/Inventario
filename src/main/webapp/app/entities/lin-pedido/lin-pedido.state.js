(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lin-pedido', {
            parent: 'entity',
            url: '/lin-pedido',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'LinPedidos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lin-pedido/lin-pedidos.html',
                    controller: 'LinPedidoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('lin-pedido-detail', {
            parent: 'entity',
            url: '/lin-pedido/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'LinPedido'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lin-pedido/lin-pedido-detail.html',
                    controller: 'LinPedidoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'LinPedido', function($stateParams, LinPedido) {
                    return LinPedido.get({id : $stateParams.id});
                }]
            }
        })
        .state('lin-pedido.new', {
            parent: 'lin-pedido',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lin-pedido/lin-pedido-dialog.html',
                    controller: 'LinPedidoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cantidadPedido: null,
                                cantidadAutorizada: null,
                                cantidadDespachada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lin-pedido', null, { reload: true });
                }, function() {
                    $state.go('lin-pedido');
                });
            }]
        })
        .state('lin-pedido.edit', {
            parent: 'lin-pedido',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lin-pedido/lin-pedido-dialog.html',
                    controller: 'LinPedidoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LinPedido', function(LinPedido) {
                            return LinPedido.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lin-pedido', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lin-pedido.delete', {
            parent: 'lin-pedido',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lin-pedido/lin-pedido-delete-dialog.html',
                    controller: 'LinPedidoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LinPedido', function(LinPedido) {
                            return LinPedido.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lin-pedido', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
