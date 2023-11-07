import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import License from 'App/Models/License'

export default class LicensesController {
  public async store({ request }: HttpContextContract) {
    let body = request.body()
    const license = await License.create(body)
    return license
  }

  public async index({ request }: HttpContextContract) {
    const page = request.input('page', 1)
    const perPage = request.input('per_page', 20)
    let licenses: License[] = await License.query().paginate(page, perPage)
    return licenses
  }

  public async show({ params }: HttpContextContract) {
    return License.findOrFail(params.id)
  }

  public async update({ params, request }: HttpContextContract) {
    const body = request.body()
    const license: License = await License.findOrFail(params.id)
    license.driver_id = body.driver_id
    license.expiration_date = body.expiration_date
    license.description = body.description
    license.type = body.type
    license.status = body.status
    return license.save()
  }

  public async destroy({ params, response }: HttpContextContract) {
    const license: License = await License.findOrFail(params.id)
    response.status(204)
    return license.delete()
  }
}
