import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Customer from 'App/Models/Customer'
import axios from 'axios'
import Env from '@ioc:Adonis/Core/Env'

export default class CostumersController {
  public async store({ request, response }: HttpContextContract) {
    try {
      let theRequest = request.toJSON()
      let token = theRequest.headers.authorization
      const body = request.body()
      let user
      try {
        user = (
          await axios.get(`${Env.get('MS-SECURITY')}/users/${body.user_id}`, {
            headers: {
              Authorization: token,
            },
          })
        ).data
      } catch (error) {
        user = null
      }
      let conflictCustomer: Customer | null = await Customer.query()
        .where('user_id', body.user_id)
        .first()
      if (user && conflictCustomer == null) {
        const customer = await Customer.create(body)
        return response.status(200).json({ mensaje: 'registro del cliente creado', data: customer })
      } else if (conflictCustomer != null) {
        return response
          .status(409)
          .json({ mensaje: 'ya hay un cliente asociado al usuario referenciado', data: body })
      } else {
        return response
          .status(400)
          .json({ mensaje: 'no se encontro al usuario referenciado', data: body })
      }
    } catch (error) {
      return response.status(500).json({ mensaje: 'Error en la creacion del cliente', data: error })
    }
  }

  public async index({ request, response }: HttpContextContract) {
    try {
      const page = request.input('page', 1)
      const perPage = request.input('per_page', 20)
      console.log('llega')
      let customers: Customer[] = await Customer.query()
        .preload('contacts')
        .preload('services')
        .paginate(page, perPage)
      console.log('llega')
      let theRequest = request.toJSON()
      let token = theRequest.headers.authorization
      if (customers && customers.length > 0) {
        await Promise.all(
          customers.map(async (customer) => {
            try {
              let userResponse = await axios.get(
                `${Env.get('MS-SECURITY')}/users/${customer.user_id}`,
                {
                  headers: {
                    Authorization: token,
                  },
                }
              )
              customer.user = userResponse.data.data
            } catch (error) {
              customer.user = null
            }
          })
        )
        return response.status(200).json({
          mensaje: 'registros de clientes encontrados',
          data: customers,
        })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontraron registros de clientes', data: customers })
      }
    } catch (error) {
      return response.status(500).json({ mensaje: 'Error en la busqueda de clientes', data: error })
    }
  }

  public async show({ params, request, response }: HttpContextContract) {
    try {
      let customer: Customer | null = await Customer.query()
        .where('id', params.id)
        .preload('contacts')
        .preload('services')
        .first()
      let theRequest = request.toJSON()
      let token = theRequest.headers.authorization
      if (customer != null) {
        try {
          let userResponse = await axios.get(
            `${Env.get('MS-SECURITY')}/users/${customer.user_id}`,
            {
              headers: {
                Authorization: token,
              },
            }
          )
          customer.user = userResponse.data.data
        } catch (error) {
          customer.user = null
        }
        return response
          .status(200)
          .json({ mensaje: 'registro del cliente encontrado', data: customer })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontro registro del cliente', data: customer })
      }
    } catch (error) {
      return response.status(500).json({ mensaje: 'Error en la busqueda del cliente', data: error })
    }
  }

  public async update({ params, request, response }: HttpContextContract) {
    try {
      let newCustomer = request.body()
      let actualCustomer: Customer = await Customer.findOrFail(params.id)
      actualCustomer.status = newCustomer.status
      if (actualCustomer.user_id != newCustomer.user_id) {
        let theRequest = request.toJSON()
        let token = theRequest.headers.authorization
        let conflictCustomer: Customer | null = await Customer.query()
          .where('user_id', newCustomer.user_id)
          .first()
        let userResponse
        try {
          userResponse = await axios.get(`${Env.get('MS-SECURITY')}/users/${newCustomer.user_id}`, {
            headers: {
              Authorization: token,
            },
          })
        } catch (error) {
          userResponse = null
        }
        if (conflictCustomer == null && userResponse != null) {
          actualCustomer.user_id = newCustomer.user_id
          try {
            actualCustomer.user = await axios.get(
              `${Env.get('MS-SECURITY')}/users/${newCustomer.user_id}`,
              {
                headers: {
                  Authorization: token,
                },
              }
            )
          } catch (error) {
            actualCustomer.user = null
          }
          actualCustomer.save()
          return response.status(200).json({ mensaje: 'cliente actualizado', data: actualCustomer })
        } else {
          console.log('llega')
          if ((await Customer.query().where('user_id', newCustomer.user_id).first()) != null) {
            return response.status(400).json({
              mensaje: 'ya hay un cliente asociado al usuario referenciado',
              data: actualCustomer,
            })
          } else {
            return response
              .status(400)
              .json({ mensaje: 'no existe el usuario referenciado', data: actualCustomer })
          }
        }
      } else {
        actualCustomer.save()
        return response.status(200).json({ mensaje: 'cliente actualizado', data: actualCustomer })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la actualizacion del cliente', data: error })
    }
  }

  public async destroy({ params, response }: HttpContextContract) {
    try {
      const customer: Customer = await Customer.findOrFail(params.id)
      if (customer) {
        customer.delete()
        return response.status(200).json({ mensaje: 'cliente eliminado', data: customer })
      } else {
        return response
          .status(400)
          .json({ mensaje: 'no se encuentra el cliente a eliminar', data: customer })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la eliminacion del cliente', data: error })
    }
  }
}
