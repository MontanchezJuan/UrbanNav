import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Trip from 'App/Models/Trip'

export default class TripsController {
  public async index({ request }: HttpContextContract) {
    const page = request.input('page', 1)
    const perPage = request.input('per_page', 20)
    // eslint-disable-next-line prettier/prettier
    return await Trip.query().preload('driver').preload('service').preload('tripPoint').paginate(page, perPage)
    // metodo para retornar a todos los conductores
    // falta precargar los vehiculos y la licensia
  }
  public async conditionalIndex({ request }: HttpContextContract) {
    // retornar los conductores que cumplan ciertas condiciones
  }
  public async show({ params }: HttpContextContract) {
    return await Trip.query().where('id', params.id).preload('driver').preload('service')
    // metodo para retornar a un conductor con toda su informacion asociada
    //falta precargar los vehiculos y la licensia
  }
  public async conditionalShow() {
    // metodo para retornar a un conductor con la informacion asociada necesaria en las condiciones
  }
  public async store({ request }: HttpContextContract) {
    let body = request.body()
    const trip = await Trip.create(body)
    return trip
    // metodo para crear un registro de conductor en la base de datos
  }
  public async update({ params, request }: HttpContextContract) {
    const body = request.body()
    const trip: Trip = await Trip.findOrFail(params.id)
    trip.origin = body.origin
    trip.destination = body.destination
    trip.driver_id = body.driver_id
    trip.started_at = body.started_at
    trip.finished_at = body.finished_at
    trip.distance = body.distance
    trip.status = body.status
    return trip.save()
    //metodo para actualizar la informacion de un conductor
  }
  public async destroy({ params, response }: HttpContextContract) {
    const trip: Trip = await Trip.findOrFail(params.id)
    response.status(204)
    return trip.delete()
    //metodo para eliminar un registro de conductor, recordar que la eliminacion sera solo una actualizacion de estado
  }
}
