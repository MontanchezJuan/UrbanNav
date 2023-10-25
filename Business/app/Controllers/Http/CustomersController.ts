import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Customer from 'App/Models/Customer'

export default class CostumersController {
  public async store({ request }: HttpContextContract) {
    let body = request.body()
    const customer = await Customer.create(body)
    return customer
  }

  public async index({ request }: HttpContextContract) {
    const page = request.input('page', 1)
    const perPage = request.input('per_page', 20)
    let customers: Customer[] = await Customer.query().paginate(page, perPage)
    return customers
  }

  public async show({ params }: HttpContextContract) {
    return Customer.findOrFail(params.id)
  }

  public async update({ params, request }: HttpContextContract) {
    const body = request.body()
    const customer: Customer = await Customer.findOrFail(params.id)
    customer.user_id = body.user_id
    customer.status = body.status
    return customer.save()
  }

  public async destroy({ params, response }: HttpContextContract) {
    const customer: Customer = await Customer.findOrFail(params.id)
    response.status(204)
    return customer.delete()
  }
}
