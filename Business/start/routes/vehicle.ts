// aqui las rutas de los vehicles

import Route from '@ioc:Adonis/Core/Route'

Route.group(() => {
  Route.post('/vehicles', 'VehiclesController.store')
  Route.get('/vehicles', 'VehiclesController.index')
  Route.get('/vehicles/:id', 'VehiclesController.show')
  Route.put('/vehicles/:id', 'VehiclesController.update')
  Route.delete('/vehicles/:id', 'VehiclesController.destroy')
}).middleware(['security'])
