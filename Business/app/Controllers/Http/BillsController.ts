import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Bill from 'App/Models/Bill'

export default class BillsController {
  public async store({ request, response }: HttpContextContract) {
    try {
      const body = request.body()
      const bill = await Bill.create(body)
      return response.status(201).json({ message: 'Factura creada exitosamente', data: bill })
    } catch (error) {
      console.error(error)
      return response.status(500).json({
        message: 'Error al crear la factura',
        data: error.mensaje,
      })
    }
  }

  public async index({ request, response }: HttpContextContract) {
    try {
      const page = request.input('page', 1)
      const perPage = request.input('per_page', 20)
      let bills: Bill[] = await Bill.query().paginate(page, perPage)
      if (bills && bills.length > 0) {
        return response
          .status(200)
          .json({ mensaje: 'Registros de facturas encontrados', data: bills })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontraron registros de facturas', data: bills })
      }
    } catch (error) {
      return response.status(500).json({ mensaje: 'Error en la facturas', data: error })
    }
  }

  public async show({ params, response }: HttpContextContract) {
    try {
      let bill: Bill | null = await Bill.query().where('id', params.id)
      if (bill != null) {
        return response
          .status(200)
          .json({ mensaje: 'registro de la factura encontrado', data: bill })
      } else {
        return response.status(404).json({ mensaje: 'No se encontro registro de la factura' })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la busqueda de la factura', data: error })
    }
  }

  public async update({ params, request, response }: HttpContextContract) {
    try {
      const body = request.body()
      const bill: Bill = await Bill.findOrFail(params.id)
      bill.service_id = body.service_id
      bill.credit_card_id = body.credit_card_id
      bill.status = body.status
      await bill.save()
      return response.status(200).json({ message: 'Factura actualizada exitosamente', data: bill })
    } catch (error) {
      return response
        .status(500)
        .json({ message: 'Error al actualizar la factura', data: error.message })
    }
  }

  public async destroy({ params, response }: HttpContextContract) {
    try {
      const bill: Bill = await Bill.findOrFail(params.id)
      if (bill != null) {
        bill.delete()
        return response.status(200).json({ mensaje: 'Factura eliminada', data: bill })
      } else {
        return response
          .status(400)
          .json({ mensaje: 'No se encuentra la facutura a eliminar', data: bill })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la eliminacion de la factura', data: error })
    }
  }
}
