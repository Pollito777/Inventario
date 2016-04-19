(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pedido', {
            parent: 'entity',
            url: '/pedido',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Pedidos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pedido/pedidos.html',
                    controller: 'PedidoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('pedido-detail', {
            parent: 'entity',
            url: '/pedido/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Pedido'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pedido/pedido-detail.html',
                    controller: 'PedidoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Pedido', function($stateParams, Pedido) {
                    return Pedido.get({id : $stateParams.id});
                }]
            }
        })
        .state('pedido.new', {
            parent: 'pedido',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pedido/pedido-dialog.html',
                    controller: 'PedidoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numeroPedido: null,
                                justificacion: null,
                                autorizaId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pedido', null, { reload: true });
                }, function() {
                    $state.go('pedido');
                });
            }]
        })
        .state('pedido.edit', {
            parent: 'pedido',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pedido/pedido-dialog.html',
                    controller: 'PedidoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pedido', function(Pedido) {
                            return Pedido.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pedido', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pedido.delete', {
            parent: 'pedido',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pedido/pedido-delete-dialog.html',
                    controller: 'PedidoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pedido', function(Pedido) {
                            return Pedido.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('pedido', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
