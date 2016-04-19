(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('entidad-publica', {
            parent: 'entity',
            url: '/entidad-publica',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'EntidadPublicas'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entidad-publica/entidad-publicas.html',
                    controller: 'EntidadPublicaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('entidad-publica-detail', {
            parent: 'entity',
            url: '/entidad-publica/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'EntidadPublica'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/entidad-publica/entidad-publica-detail.html',
                    controller: 'EntidadPublicaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'EntidadPublica', function($stateParams, EntidadPublica) {
                    return EntidadPublica.get({id : $stateParams.id});
                }]
            }
        })
        .state('entidad-publica.new', {
            parent: 'entidad-publica',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entidad-publica/entidad-publica-dialog.html',
                    controller: 'EntidadPublicaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                direccion: null,
                                tipo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('entidad-publica', null, { reload: true });
                }, function() {
                    $state.go('entidad-publica');
                });
            }]
        })
        .state('entidad-publica.edit', {
            parent: 'entidad-publica',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entidad-publica/entidad-publica-dialog.html',
                    controller: 'EntidadPublicaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EntidadPublica', function(EntidadPublica) {
                            return EntidadPublica.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('entidad-publica', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('entidad-publica.delete', {
            parent: 'entidad-publica',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/entidad-publica/entidad-publica-delete-dialog.html',
                    controller: 'EntidadPublicaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EntidadPublica', function(EntidadPublica) {
                            return EntidadPublica.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('entidad-publica', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
