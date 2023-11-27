// aqui las rutas de los servicios

import Route from '@ioc:Adonis/Core/Route'
Route.group(() => {
  Route.get('/services', 'ServicesController.index')
  Route.post('/services', 'ServicesController.store')
  Route.get('/services/:id', 'ServicesController.show')
  Route.put('/services/:id', 'ServicesController.update')
  Route.delete('/services/:id', 'ServicesController.destroy')
})
.middleware(['security'])
