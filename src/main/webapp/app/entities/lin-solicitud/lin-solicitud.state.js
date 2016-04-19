(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lin-solicitud', {
            parent: 'entity',
            url: '/lin-solicitud',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'LinSolicituds'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lin-solicitud/lin-solicituds.html',
                    controller: 'LinSolicitudController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('lin-solicitud-detail', {
            parent: 'entity',
            url: '/lin-solicitud/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'LinSolicitud'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lin-solicitud/lin-solicitud-detail.html',
                    controller: 'LinSolicitudDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'LinSolicitud', function($stateParams, LinSolicitud) {
                    return LinSolicitud.get({id : $stateParams.id});
                }]
            }
        })
        .state('lin-solicitud.new', {
            parent: 'lin-solicitud',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lin-solicitud/lin-solicitud-dialog.html',
                    controller: 'LinSolicitudDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                cantidadSolicitada: null,
                                cantidadAutorizada: null,
                                cantidadComprada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lin-solicitud', null, { reload: true });
                }, function() {
                    $state.go('lin-solicitud');
                });
            }]
        })
        .state('lin-solicitud.edit', {
            parent: 'lin-solicitud',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lin-solicitud/lin-solicitud-dialog.html',
                    controller: 'LinSolicitudDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LinSolicitud', function(LinSolicitud) {
                            return LinSolicitud.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lin-solicitud', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lin-solicitud.delete', {
            parent: 'lin-solicitud',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lin-solicitud/lin-solicitud-delete-dialog.html',
                    controller: 'LinSolicitudDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LinSolicitud', function(LinSolicitud) {
                            return LinSolicitud.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lin-solicitud', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
