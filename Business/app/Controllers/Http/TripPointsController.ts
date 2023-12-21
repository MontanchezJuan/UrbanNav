import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Database from '@ioc:Adonis/Lucid/Database'
import TripPoint from 'App/Models/TripPoint'

export default class TripPointsController {
  public async index({ request, response }: HttpContextContract) {
    try {
      const page = request.input('page', 1)
      const perPage = request.input('per_page', 20)
      let trip_points: TripPoint[] = await TripPoint.query().paginate(page, perPage)
      if (trip_points && trip_points.length > 0) {
        return response
          .status(200)
          .json({ mensaje: 'registros de puntos de viaje encontrados', data: trip_points })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontraron registros de puntos de viaje', data: trip_points })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la busqueda de puntos de viaje', data: error })
    }
  }

  public async show({ params, response }: HttpContextContract) {
    try {
      let trip_point: TripPoint | null = await TripPoint.query().where('id', params.id).first()
      if (trip_point != null) {
        return response
          .status(200)
          .json({ mensaje: 'registro del punto de viaje encontrado', data: trip_point })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontro registro del punto de viaje', data: trip_point })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la busqueda del punto de viaje', data: error })
    }
  }

  public async store({ request, response }: HttpContextContract) {
    try {
      const body = request.body()
      const tripPoint = await TripPoint.create(body)
      return response
        .status(201)
        .json({ message: 'punto de viaje creado exitosamente', data: tripPoint })
    } catch (error) {
      return response
        .status(500)
        .json({ message: 'Error al crear el punto de viaje', data: error.message })
    }
  }

  public async update({ params, request, response }: HttpContextContract) {
    try {
      const body = request.body()
      let trip_point: TripPoint = await TripPoint.findOrFail(params.id)
      trip_point.trip_id = body.trip_id
      trip_point.point_id = body.point_id
      trip_point.index = body.index
      trip_point.status = body.status
      await trip_point.save()
      return response
        .status(200)
        .json({ message: 'punto de viaje actualizado exitosamente', data: trip_point })
    } catch (error) {
      return response
        .status(500)
        .json({ message: 'Error al actualizar el punto de viaje', data: error.message })
    }
  }

  public async destroy({ params, response }: HttpContextContract) {
    try {
      const trip_point: TripPoint = await TripPoint.findOrFail(params.id)
      if (trip_point) {
        trip_point.delete()
        return response.status(200).json({ mensaje: 'punto de viaje eliminado', data: trip_point })
      } else {
        return response
          .status(400)
          .json({ mensaje: 'no se encuentra el punto de viaje a eliminar', data: trip_point })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la eliminacion del punto de viaje', data: error })
    }
  }

  public async getRoute({ request, response }: HttpContextContract) {
    try {
      let body = request.body()
      let origin: number = body.origin
      let destination: number = body.destination
      const data = await Database.from('trip')
      return response.status(500).json({ mensaje: 'viajes', data })
    } catch (error) {}
  }
}
