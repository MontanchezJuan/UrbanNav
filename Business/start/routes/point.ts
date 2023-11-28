// aqui las rutas de los comentarios y calificaciones

import Route from '@ioc:Adonis/Core/Route'
Route.group(() => {
  Route.get('/points', 'PointsController.index')
  Route.post('/points', 'PointsController.store')
  Route.get('/points/:id', 'PointsController.show')
  Route.put('/points/:id', 'PointsController.update')
  Route.delete('/points/:id', 'PointsController.destroy')
}).middleware(['security'])
