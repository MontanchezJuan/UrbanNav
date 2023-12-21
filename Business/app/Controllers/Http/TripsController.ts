import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Database from '@ioc:Adonis/Lucid/Database'
import Driver from 'App/Models/Driver'
import Trip from 'App/Models/Trip'
import TripPoint from 'App/Models/TripPoint'

export default class TripsController {
  public async index({ request, response }: HttpContextContract) {
    try {
      const page = request.input('page', 1)
      const perPage = request.input('per_page', 20)
      let trips: Trip[] = await Trip.query()
        .preload('driver')
        .preload('service')
        .preload('points')
        .paginate(page, perPage)
      if (trips && trips.length > 0) {
        return response
          .status(200)
          .json({ mensaje: 'registros de viajes encontrados', data: trips })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontraron registros de viajes', data: trips })
      }
    } catch (error) {
      return response.status(500).json({ mensaje: 'Error en la busqueda de viajes', data: error })
    }
  }
  public async show({ params, response }: HttpContextContract) {
    try {
      let trip: Trip | null = await Trip.query()
        .where('id', params.id)
        .preload('driver')
        .preload('service')
        .preload('points')
        .first()
      if (trip != null) {
        return response.status(200).json({ mensaje: 'registro del viaje encontrado', data: trip })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontro registro del viaje', data: trip })
      }
    } catch (error) {
      return response.status(500).json({ mensaje: 'Error en la busqueda del viaje', data: error })
    }
  }
  public async store({ request, response }: HttpContextContract) {
    try {
      const body = request.body()
      const trip = await Trip.create(body)
      return response.status(201).json({ message: 'viaje creado exitosamente', data: trip })
    } catch (error) {
      console.error(error)
      return response.status(500).json({ message: 'Error al crear el viaje', data: error.message })
    }
  }
  public async bkstore(service, body, response) {
    try {
      let points = body.trippoints
      let driver = await this.chooseDriver(points.origin)

      if (driver != null) {
        body.trip.driver_id = driver.id
      }
      const trip = await Trip.create(body.trip)
      service.trip_id = trip.id
      return trip
    } catch (error) {
      console.error(error)
      return response.status(500).json({ message: 'Error al crear el viaje', data: error.message })
    }
  }
  public async update({ params, request, response }: HttpContextContract) {
    try {
      const body = request.body()
      const trip: Trip = await Trip.findOrFail(params.id)
      trip.driver_id = body.driver_id
      trip.started_at = body.started_at
      trip.finished_at = body.finished_at
      trip.distance = body.distance
      trip.status = body.status
      trip.save()
      return response.status(200).json({ message: 'Viaje actualizado exitosamente', data: trip })
    } catch (error) {
      return response
        .status(500)
        .json({ message: 'Error al actualizar el viaje', data: error.message })
    }
  }
  public async destroy({ params, response }: HttpContextContract) {
    try {
      const trip: Trip = await Trip.findOrFail(params.id)
      if (trip) {
        trip.delete()
        return response.status(200).json({ mensaje: 'viaje eliminado', data: trip })
      } else {
        return response
          .status(400)
          .json({ mensaje: 'no se encuentra el viaje a eliminar', data: trip })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la eliminacion del viaje', data: error })
    }
  }

  // public async getRoute({ request, response }: HttpContextContract) {
  //   try {
  //     let body = request.body()
  //     let origin: number = body.origin
  //     let destination: number = body.destination
  //     const data = await Database.from('trip')
  //     return response.status(500).json({ mensaje: 'viajes', data })
  //   } catch (error) {}
  // }

  public async chooseDriver(origin): Promise<Driver | null> {
    const Haversine = (lat1, lon1, lat2, lon2) => {
      const R = 6371 // Radio de la Tierra en kilómetros
      // Convertir de grados a radianes
      const toRadians = (angle) => (angle * Math.PI) / 180
      lat1 = toRadians(lat1)
      lon1 = toRadians(lon1)
      lat2 = toRadians(lat2)
      lon2 = toRadians(lon2)
      // Diferencias de latitud y longitud
      const dlat = lat2 - lat1
      const dlon = lon2 - lon1
      // Calcular la distancia usando la fórmula de Haversine
      const a = Math.sin(dlat / 2) ** 2 + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlon / 2) ** 2
      const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
      const distance = R * c
      return distance
    }
    let minDistance: number | null = null
    let bestDriver: Driver | null = null
    let activeDrivers: Driver[] = await Driver.query().where('is_active', true)
    activeDrivers.forEach((driver) => {
      const distance = Haversine(
        driver.latitude,
        driver.longitude,
        origin.latitude,
        origin.longitude
      )
      if (minDistance != null) {
        if (distance <= minDistance) {
          minDistance = distance
          bestDriver = driver
        }
      } else {
        minDistance = distance
        bestDriver = driver
      }
    })
    console.log(minDistance)

    return bestDriver
  }
}
