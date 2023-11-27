// aqui las rutas de los comentarios y calificaciones

import Route from '@ioc:Adonis/Core/Route'
Route.group(() => {
  Route.get('/bills', 'BillsController.index')
  Route.post('/bills', 'BillsController.store')
  Route.get('/bills/:id', 'BillsController.show')
  Route.put('/bills/:id', 'BillsController.update')
  Route.delete('/bills/:id', 'BillsController.destroy')
})
.middleware(['security'])
