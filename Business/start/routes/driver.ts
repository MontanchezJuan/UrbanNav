// aqui las rutas de los conductores

import Route from '@ioc:Adonis/Core/Route'

Route.group(() => {
  Route.get('/drivers', 'DriversController.index')
  Route.post('/drivers', 'DriversController.store')
  Route.get('/drivers/:id', 'DriversController.show')
  Route.put('/drivers/:id', 'DriversController.update')
  Route.delete('/drivers/:id', 'DriversController.destroy')
})
// .middleware(['security'])
