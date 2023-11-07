import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Service from 'App/Models/Service'

export default class ServicesController {
  public async index({ request }: HttpContextContract) {
    const page = request.input('page', 1)
    const perPage = request.input('per_page', 20)
    return await Service.query()
      .preload('trip')
      .preload('customer')
      .preload('bill')
      //.preload('commentsAndRatings')
      .paginate(page, perPage)
    // metodo para retornar a todos los conductores
    // falta precargar los vehiculos y la licensia
  }
  public async conditionalIndex({ request }: HttpContextContract) {
    // retornar los conductores que cumplan ciertas condiciones
  }
  public async show({ params }: HttpContextContract) {
    return await Service.query()
      .where('id', params.id)
      .preload('trip')
      .preload('customer')
      .preload('bill')
    //.preload('commentsAndRatings')
    // metodo para retornar a un conductor con toda su informacion asociada
    //falta precargar los vehiculos y la licensia
  }
  public async conditionalShow() {
    // metodo para retornar a un conductor con la informacion asociada necesaria en las condiciones
  }
  public async store({ request }: HttpContextContract) {
    let body = request.body()
    const service = await Service.create(body)
    return service
    // metodo para crear un registro de conductor en la base de datos
  }
  public async update({ params, request }: HttpContextContract) {
    const body = request.body()
    const service: Service = await Service.findOrFail(params.id)
    service.customer_id = body.customer_id
    service.trip_id = body.trip_id
    service.price = body.price
    service.status = body.status
    return service.save()

    //metodo para actualizar la informacion de un conductor
  }
  public async destroy({ params, response }: HttpContextContract) {
    const service: Service = await Service.findOrFail(params.id)
    response.status(204)
    return service.delete()
    //metodo para eliminar un registro de conductor, recordar que la eliminacion sera solo una actualizacion de estado
  }
}
