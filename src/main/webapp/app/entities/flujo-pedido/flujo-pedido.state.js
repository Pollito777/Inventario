(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('flujo-pedido', {
            parent: 'entity',
            url: '/flujo-pedido',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FlujoPedidos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flujo-pedido/flujo-pedidos.html',
                    controller: 'FlujoPedidoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('flujo-pedido-detail', {
            parent: 'entity',
            url: '/flujo-pedido/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FlujoPedido'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/flujo-pedido/flujo-pedido-detail.html',
                    controller: 'FlujoPedidoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FlujoPedido', function($stateParams, FlujoPedido) {
                    return FlujoPedido.get({id : $stateParams.id});
                }]
            }
        })
        .state('flujo-pedido.new', {
            parent: 'flujo-pedido',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flujo-pedido/flujo-pedido-dialog.html',
                    controller: 'FlujoPedidoDialogController',
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
                    $state.go('flujo-pedido', null, { reload: true });
                }, function() {
                    $state.go('flujo-pedido');
                });
            }]
        })
        .state('flujo-pedido.edit', {
            parent: 'flujo-pedido',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flujo-pedido/flujo-pedido-dialog.html',
                    controller: 'FlujoPedidoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FlujoPedido', function(FlujoPedido) {
                            return FlujoPedido.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('flujo-pedido', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('flujo-pedido.delete', {
            parent: 'flujo-pedido',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/flujo-pedido/flujo-pedido-delete-dialog.html',
                    controller: 'FlujoPedidoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FlujoPedido', function(FlujoPedido) {
                            return FlujoPedido.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('flujo-pedido', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
