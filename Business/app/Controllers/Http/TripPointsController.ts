import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import TripPoint from 'App/Models/TripPoint'

export default class TripPointsController {
  public async index({ request }: HttpContextContract) {
    const page = request.input('page', 1)
    const perPage = request.input('per_page', 2)
    return await TripPoint.query().paginate(page, perPage)
  }

  public async show({ params }: HttpContextContract) {
    return await TripPoint.query().where('id', params.id)
  }

  public async store({ request }: HttpContextContract) {
    let body = request.body()
    const trip = await TripPoint.create(body)
    return trip
  }

  public async update({ params, request }: HttpContextContract) {
    const body = request.body()
    const trip: TripPoint = await TripPoint.findOrFail(params.id)
    trip.name = body.name
    trip.latitude = body.latitude
    trip.longitude = body.longitude
    trip.status = body.status
    return trip.save()
  }

  public async destroy({ params, response }: HttpContextContract) {
    const trip: TripPoint = await TripPoint.findOrFail(params.id)
    response.status(204)
    return trip.delete()
  }
}
