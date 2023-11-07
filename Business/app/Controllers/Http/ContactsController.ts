import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Contact from 'App/Models/Contact'

export default class ContactsController {
  public async index({ request }: HttpContextContract) {
    const page = request.input('page', 1)
    const perPage = request.input('per_page', 20)
    return await Contact.query().paginate(page, perPage)
  }

  public async show({ params }: HttpContextContract) {
    return await Contact.query().where('id', params.id)
  }

  public async store({ request }: HttpContextContract) {
    let body = request.body()
    const contact = await Contact.create(body)
    return contact
  }

  public async update({ params, request }: HttpContextContract) {
    const body = request.body()
    let contact: Contact = await Contact.findOrFail(params.id)
    contact.name = body.name
    contact.email = body.email
    contact.phone_number = body.phone_number
    contact.is_emergy_contact = body.is_emergy_contact
    contact.status = body.estatus
    return contact.save()
  }

  public async destroy({ params, response }: HttpContextContract) {
    const contact: Contact = await Contact.findOrFail(params.id)
    response.status(204)
    return contact.delete()
  }
}
