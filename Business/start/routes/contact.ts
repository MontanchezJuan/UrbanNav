import Route from '@ioc:Adonis/Core/Route'
Route.group(() => {
  Route.get('/contacts', 'ContactsController.index')
  Route.post('/contacts', 'ContactsController.store')
  Route.get('/contacts/:id', 'ContactsController.show')
  Route.put('/contacts/:id', 'ContactsController.update')
  Route.delete('/contacts/:id', 'ContactsController.destroy')
})
.middleware(['security'])