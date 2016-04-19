(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('material', {
            parent: 'entity',
            url: '/material',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Materials'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/material/materials.html',
                    controller: 'MaterialController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('material-detail', {
            parent: 'entity',
            url: '/material/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Material'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/material/material-detail.html',
                    controller: 'MaterialDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Material', function($stateParams, Material) {
                    return Material.get({id : $stateParams.id});
                }]
            }
        })
        .state('material.new', {
            parent: 'material',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/material/material-dialog.html',
                    controller: 'MaterialDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                codigo: null,
                                descripcion: null,
                                codigoFabrica: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('material', null, { reload: true });
                }, function() {
                    $state.go('material');
                });
            }]
        })
        .state('material.edit', {
            parent: 'material',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/material/material-dialog.html',
                    controller: 'MaterialDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Material', function(Material) {
                            return Material.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('material', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('material.delete', {
            parent: 'material',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/material/material-delete-dialog.html',
                    controller: 'MaterialDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Material', function(Material) {
                            return Material.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('material', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
