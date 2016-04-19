(function() {
    'use strict';

    angular
        .module('inventarioApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('proyecto', {
            parent: 'entity',
            url: '/proyecto',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Proyectos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/proyecto/proyectos.html',
                    controller: 'ProyectoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('proyecto-detail', {
            parent: 'entity',
            url: '/proyecto/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Proyecto'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/proyecto/proyecto-detail.html',
                    controller: 'ProyectoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Proyecto', function($stateParams, Proyecto) {
                    return Proyecto.get({id : $stateParams.id});
                }]
            }
        })
        .state('proyecto.new', {
            parent: 'proyecto',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proyecto/proyecto-dialog.html',
                    controller: 'ProyectoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                noControl: null,
                                modalidad: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('proyecto', null, { reload: true });
                }, function() {
                    $state.go('proyecto');
                });
            }]
        })
        .state('proyecto.edit', {
            parent: 'proyecto',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proyecto/proyecto-dialog.html',
                    controller: 'ProyectoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Proyecto', function(Proyecto) {
                            return Proyecto.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('proyecto', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('proyecto.delete', {
            parent: 'proyecto',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/proyecto/proyecto-delete-dialog.html',
                    controller: 'ProyectoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Proyecto', function(Proyecto) {
                            return Proyecto.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('proyecto', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
