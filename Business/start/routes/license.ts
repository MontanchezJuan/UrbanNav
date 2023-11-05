// aqui las rutas de los comentarios y calificaciones

import Route from '@ioc:Adonis/Core/Route'
Route.group(() => {
  Route.get('/licenses', 'LicensesController.index')
  Route.post('/licenses', 'LicensesController.store')
  Route.get('/licenses/:id', 'LicensesController.show')
  Route.put('/licenses/:id', 'LicensesController.update')
  Route.delete('/licenses/:id', 'LicensesController.destroy')
}).middleware(['security'])
