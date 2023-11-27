// aqui las rutas de los comentarios y calificaciones

import Route from '@ioc:Adonis/Core/Route'
Route.group(() => {
  Route.get('/commentsandratings', 'CommentsandRatingsController.index')
  Route.post('/commentsandratings', 'CommentsandRatingsController.store')
  Route.get('/commentsandratings/:id', 'CommentsandRatingsController.show')
  Route.put('/commentsandratings/:id', 'CommentsandRatingsController.update')
  Route.delete('/commentsandratings/:id', 'CommentsandRatingsController.destroy')
})
.middleware(['security'])
