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
    return Customer.query().where('id', params.id)
  }
}
