import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import License from 'App/Models/License'

export default class LicensesController {
  public async store({ request, response }: HttpContextContract) {
    try {
      const body = request.body();
      const theLicense = await License.create(body);
      return response.status(201).json({ message: 'Licensia creada exitosamente', data: theLicense });
  } catch (error) {
      console.error(error);
      return response.status(500).json({ message: 'Error al crear la Licensia', data: error.message });
  }
}

  public async index({ request, response }: HttpContextContract) {
    try{
      const page = request.input('page', 1)
      const perPage = request.input('per_page', 20)
      let licenses: License[]= await License.query()
      .preload('driver')
      .paginate(page, perPage)
      if(licenses && licenses.length > 0){
      return response
      .status(200)
      .json({ mensaje: 'registros de licencias encontrados', data: licenses })
      } else {
      return response
        .status(404)
        .json({ mensaje: 'No se encontraron registros de licencias', data: licenses })
    }
    }catch (error){
        return response
        .status(500)
        .json({ mensaje: 'Error en la busqueda de licencias', data: error 
        })
    }
  }

  public async show({ params, response }: HttpContextContract) {
    try{
      let license: License | null = await License.query()
      .where('id', params.id)
      .preload('driver')
      .first()
      if(license != null){
          return response
          .status(200)
          .json({ mensaje: 'registro de las licencias encontrado', data: license })
      } else {
          return response
          .status(404)
          .json({ mensaje: 'No se encontro registro de las licencias', data: license }) 
      }
    }catch (error){
        return response
        .status(500)
        .json({ mensaje: 'Error en la busqueda de las licencias', data: error })
    }
  }

  public async update({ params, request, response }: HttpContextContract) {
    try {
      const body = request.body()
      let license: License = await License.findOrFail(params.id)
          license.driver_id = body.driver_id
          license.expiration_date = body.expiration_date
          license.description = body.description
          license.type = body.type
          license.status = body.status
      // Guardar en la base de datos el registro actualizado
      await license.save()
      return response.status(200).json({ message: 'Licensia actualizada exitosamente', data: license })
    } catch (error) {
        console.error(error);
        return response.status(500).json({ message: 'Error al actualizar la licensia', data: error.message })
    }
  }

  public async destroy({ params, response }: HttpContextContract) {
    try {    
      const license: License = await License.findOrFail(params.id)
      if (license) {
        license.delete()
        return response.status(200).json({ mensaje: 'Licensia eliminada', data: license })
      } else {
        return response
          .status(400)
          .json({ mensaje: 'no se encuentra la Licensia a eliminar', data: license })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la eliminacion de la Licensia', data: error })
    }
  }
}
