(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('solicitud-compra', {
            parent: 'entity',
            url: '/solicitud-compra',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SolicitudCompras'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/solicitud-compra/solicitud-compras.html',
                    controller: 'SolicitudCompraController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('solicitud-compra-detail', {
            parent: 'entity',
            url: '/solicitud-compra/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'SolicitudCompra'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/solicitud-compra/solicitud-compra-detail.html',
                    controller: 'SolicitudCompraDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'SolicitudCompra', function($stateParams, SolicitudCompra) {
                    return SolicitudCompra.get({id : $stateParams.id});
                }]
            }
        })
        .state('solicitud-compra.new', {
            parent: 'solicitud-compra',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/solicitud-compra/solicitud-compra-dialog.html',
                    controller: 'SolicitudCompraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numeroSolicitud: null,
                                justificacion: null,
                                autorizaId: null,
                                ordenId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('solicitud-compra', null, { reload: true });
                }, function() {
                    $state.go('solicitud-compra');
                });
            }]
        })
        .state('solicitud-compra.edit', {
            parent: 'solicitud-compra',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/solicitud-compra/solicitud-compra-dialog.html',
                    controller: 'SolicitudCompraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SolicitudCompra', function(SolicitudCompra) {
                            return SolicitudCompra.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('solicitud-compra', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('solicitud-compra.delete', {
            parent: 'solicitud-compra',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/solicitud-compra/solicitud-compra-delete-dialog.html',
                    controller: 'SolicitudCompraDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SolicitudCompra', function(SolicitudCompra) {
                            return SolicitudCompra.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('solicitud-compra', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
