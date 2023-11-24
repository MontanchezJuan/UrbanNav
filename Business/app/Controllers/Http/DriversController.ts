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
      let theRequest = request.toJSON()
      let token = theRequest.headers.authorization
      if (drivers && drivers.length > 0) {
        await Promise.all(
          drivers.map(async (driver) => {
            let userResponse = await axios.get(
              `${Env.get('MS-SECURITY')}/users/${driver.user_id}`,
              {
                headers: {
                  Authorization: token,
                },
              }
            )
            console.log('llega')
            driver.user = userResponse.data
            console.log(driver.toJSON())
          })
        )
        return response.status(200).json({
          mensaje: 'registros de conductores encontrados',
          data: drivers,
        })
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
  public async show({ request, params, response }: HttpContextContract) {
    try {
      let driver: Driver | null = await Driver.query()
        .where('id', params.id)
        .preload('trips')
        .preload('vehicles')
        .preload('license')
        .first()
      let theRequest = request.toJSON()
      let token = theRequest.headers.authorization
      if (driver != null) {
        axios
          .get(`${Env.get('MS-SECURITY')}/users/${driver.user_id}`, {
            headers: {
              Authorization: token,
            },
          })
          .then((response) => {
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
      let theRequest = request.toJSON()
      let token = theRequest.headers.authorization
      const body = request.body()
      let user: Driver = (
        await axios.get(`${Env.get('MS-SECURITY')}/users/${body.user_id}`, {
          headers: {
            Authorization: token,
          },
        })
      ).data

      let conflictDriver: Driver | null = await Driver.query()
        .where('user_id', body.user_id)
        .first()
      if (user && conflictDriver == null) {
        const driver = await Driver.create(body)
        return response.status(200).json({ mensaje: 'registro del conductor creado', data: driver })
      } else if (conflictDriver != null) {
        return response
          .status(409)
          .json({ mensaje: 'ya hay un conductor asociado al usuario referenciado', data: body })
      } else {
        return response
          .status(400)
          .json({ mensaje: 'no se encontro al usuario referenciado', data: body })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la creacion del conductor', data: error })
    }
  }
  public async update({ params, request, response }: HttpContextContract) {
    try {
      let newDriver = request.body()
      let actualDriver: Driver = await Driver.findOrFail(params.id)
      actualDriver.is_active = newDriver.is_active
      actualDriver.status = newDriver.status
      if (actualDriver.user_id != newDriver.user_id) {
        if (
          (await Driver.query().where('driver_id', newDriver.user_id).first()) == null &&
          (await axios.get(`${Env.get('MS-SECURITY')}/users/${newDriver.user_id}`)).status == 200
        ) {
          actualDriver.user_id = newDriver.user_id
          actualDriver.user = await axios.get(
            `${Env.get('MS-SECURITY')}/users/${newDriver.user_id}`
          )
          actualDriver.save()
          return response.status(200).json({ mensaje: 'conductor actualizado', data: actualDriver })
        } else {
          if ((await Driver.query().where('driver_id', newDriver.user_id).first()) != null) {
            return response.status(400).json({
              mensaje: 'ya hay un conductor asociado al usuario referenciado',
              data: actualDriver,
            })
          } else {
            return response
              .status(400)
              .json({ mensaje: 'no existe el usuario referenciado', data: actualDriver })
          }
        }
      } else {
        actualDriver.user_id = newDriver.user_id
        actualDriver.user = await axios.get(`${Env.get('MS-SECURITY')}/users/${newDriver.user_id}`)
        actualDriver.save()
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la actualizacion del conductor', data: error })
    }
  }
  public async destroy({ params, response }: HttpContextContract) {
    try {
      const driver: Driver = await Driver.findOrFail(params.id)
      if (driver) {
        driver.delete()
        return response.status(200).json({ mensaje: 'conductor eliminado', data: driver })
      } else {
        return response
          .status(400)
          .json({ mensaje: 'no se encuentra el conductor a eliminar', data: driver })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la eliminacion del conductor', data: error })
    }
  }
}
