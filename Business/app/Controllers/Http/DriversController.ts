import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Env from '@ioc:Adonis/Core/Env'
import Driver from 'App/Models/Driver'
import axios from 'axios'

export default class DriversController {
  public async index({ request, response }: HttpContextContract) {
    try {
      const page = request.input('page', 1)
      const perPage = request.input('per_page', 20)
      let drivers: Driver[] = await Driver.query()
        .preload('trips')
        .preload('license')
        .preload('vehicles')
        .paginate(page, perPage)
      if (drivers && drivers.length > 0) {
        drivers.forEach((driver) => {
          axios.get(`${Env.get('MS-SECURITY')}/users/${driver.user_id}`).then((response) => {
            driver.user = response.data
          })
        })
        return response
          .status(200)
          .json({ mensaje: 'registros de conductores encontrados', data: drivers })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontraron registros de conductores', data: drivers })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la busqueda de conductores', data: error })
    }
  }
  public async show({ params, response }: HttpContextContract) {
    try {
      let driver: Driver | null = await Driver.query()
        .where('id', params.id)
        .preload('trips')
        .preload('vehicles')
        .preload('license')
        .first()
      if (driver != null) {
        axios.get(`${Env.get('MS-SECURITY')}/users/${driver.user_id}`).then((response) => {
          if (driver) {
            driver.user = response.data
          }
        })
        return response
          .status(200)
          .json({ mensaje: 'registro del conductor encontrado', data: driver })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontro registro del conductor', data: driver })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la busqueda del conductor', data: error })
    }
  }
  public async store({ request, response }: HttpContextContract) {
    try {
      const body = request.body()
      let user = await (await axios.get(`${Env.get('MS-SECURITY')}/users/${body.user_id}`)).data
      if (user) {
        const driver = await Driver.create(body)
        return response.status(200).json({ mensaje: 'registro del conductor creado', data: driver })
      } else {
        return response.status(400).json({ mensaje: 'no se encontro al usuario', data: body })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la creacion del conductor', data: error })
    }
  }
  public async update({ params, request }: HttpContextContract) {
    try {
      // seguir aca
    } catch (error) {}
    const body = request.body()
    const driver: Driver = await Driver.findOrFail(params.id)
    driver.is_active = body.is_active
    driver.status = body.status
    return driver.save()

    //metodo para actualizar la informacion de un conductor
  }
  public async destroy({ params, response }: HttpContextContract) {
    const driver: Driver = await Driver.findOrFail(params.id)
    response.status(204)
    return driver.delete()
    //metodo para eliminar un registro de conductor, recordar que la eliminacion sera solo una actualizacion de estado
  }
}
