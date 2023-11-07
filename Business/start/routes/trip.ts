// aqui las rutas de los viajes

import Route from '@ioc:Adonis/Core/Route'
Route.group(() => {
  Route.get('/trips', 'TripsController.index')
  Route.post('/trips', 'TripsController.store')
  Route.get('/trips/:id', 'TripsController.show')
  Route.put('/trips/:id', 'TripsController.update')
  Route.delete('/trips/:id', 'TripsController.destroy')
})
//.middleware(['security'])
