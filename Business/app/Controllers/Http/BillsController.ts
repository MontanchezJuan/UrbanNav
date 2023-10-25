import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Bill from 'App/Models/Bill'

export default class BillsController {
  public async store({ request }: HttpContextContract) {
    let body = request.body()
    const bill = await Bill.create(body)
    return bill
  }

  public async index({ request }: HttpContextContract) {
    const page = request.input('page', 1)
    const perPage = request.input('per_page', 20)
    let bills: Bill[] = await Bill.query().paginate(page, perPage)
    return bills
  }

  public async show({ params }: HttpContextContract) {
    return Bill.findOrFail(params.id)
  }

  public async update({ params, request }: HttpContextContract) {
    const body = request.body()
    const bill: Bill = await Bill.findOrFail(params.id)
    bill.service_id = body.service_id
    bill.credit_card_id = body.credit_card_id
    bill.status = body.status
    return bill.save()
  }

  public async destroy({ params, response }: HttpContextContract) {
    const bill: Bill = await Bill.findOrFail(params.id)
    response.status(204)
    return bill.delete()
  }
}
