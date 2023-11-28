import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Point from 'App/Models/Point'

export default class PointsController {
  public async store({ request, response }: HttpContextContract) {
    try {
      const body = request.body()
      const point = await Point.create(body)
      return response.status(201).json({ message: 'punto creado exitosamente', data: point })
    } catch (error) {
      console.error(error)
      return response.status(500).json({ message: 'Error al crear el punto', data: error.message })
    }
  }

  public async index({ request, response }: HttpContextContract) {
    try {
      const page = request.input('page', 1)
      const perPage = request.input('per_page', 20)
      let points: Point[] = await Point.query().paginate(page, perPage)
      if (points && points.length > 0) {
        return response
          .status(200)
          .json({ mensaje: 'registros de puntos encontrados', data: points })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontraron registros de puntos', data: points })
      }
    } catch (error) {
      return response.status(500).json({ mensaje: 'Error en la busqueda de puntos', data: error })
    }
  }

  public async show({ params, response }: HttpContextContract) {
    try {
      let point: Point | null = await Point.query().where('id', params.id).first()
      if (point != null) {
        return response.status(200).json({ mensaje: 'registro del punto encontrado', data: point })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontro registro del punto', data: point })
      }
    } catch (error) {
      return response.status(500).json({ mensaje: 'Error en la busqueda del punto', data: error })
    }
  }

  public async update({ params, request, response }: HttpContextContract) {
    try {
      const body = request.body()
      let point: Point = await Point.findOrFail(params.id)
      point.latitude = body.latitude
      point.longitude = body.longitude
      point.status = body.status
      await point.save()
      return response.status(200).json({ message: 'punto actualizado exitosamente', data: point })
    } catch (error) {
      return response
        .status(500)
        .json({ message: 'Error al actualizar el punto', data: error.message })
    }
  }

  public async destroy({ params, response }: HttpContextContract) {
    try {
      const point: Point = await Point.findOrFail(params.id)
      if (point) {
        point.delete()
        return response.status(200).json({ mensaje: 'punto eliminado', data: point })
      } else {
        return response
          .status(400)
          .json({ mensaje: 'no se encuentra el punto a eliminar', data: point })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la eliminacion del punto', data: error })
    }
  }
}
