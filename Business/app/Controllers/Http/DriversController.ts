import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Driver from 'App/Models/Driver'

export default class DriversController {
  public async index({ request }: HttpContextContract) {
    const page = request.input('page', 1)
    const perPage = request.input('per_page', 20)
    return await Driver.query().preload('trips').paginate(page, perPage)
    // metodo para retornar a todos los conductores
    // falta precargar los vehiculos y la licensia
  }
  public async conditionalIndex({ request }: HttpContextContract) {
    // retornar los conductores que cumplan ciertas condiciones
  }
  public async show({ params }: HttpContextContract) {
    return await Driver.query().where('id', params.id).preload('trips')
    // metodo para retornar a un conductor con toda su informacion asociada
    //falta precargar los vehiculos y la licensia
  }
  public async conditionalShow() {
    // metodo para retornar a un conductor con la informacion asociada necesaria en las condiciones
  }
  public async store({ request }: HttpContextContract) {
    let body = request.body()
    const driver = await Driver.create(body)
    return driver
    // metodo para crear un registro de conductor en la base de datos
  }
  public async update({ params, request }: HttpContextContract) {
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
