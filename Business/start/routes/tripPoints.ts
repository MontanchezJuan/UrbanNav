// aqui las rutas de los viajes

import Route from '@ioc:Adonis/Core/Route'
Route.group(() => {
  Route.get('/trip-points', 'TripPointsController.index')
  Route.post('/trip-points', 'TripPointsController.store')
  Route.get('/trip-points/:id', 'TripPointsController.show')
  Route.put('/trip-points/:id', 'TripPointsController.update')
  Route.delete('/trip-points/:id', 'TripPointsController.destroy')
}).middleware(['security'])
