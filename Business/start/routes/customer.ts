// aqui las rutas de los comentarios y calificaciones

import Route from '@ioc:Adonis/Core/Route'
Route.group(() => {
  Route.get('/customers', 'CustomersController.index')
  Route.post('/customers', 'CustomersController.store')
  Route.get('/customers/:id', 'CustomersController.show')
  Route.put('/customers/:id', 'CustomersController.update')
  Route.delete('/customers/:id', 'CustomersController.destroy')
})
// .middleware(['security'])
