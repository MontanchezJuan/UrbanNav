import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Vehicle from 'App/Models/Vehicle'

export default class VehiclesController {
  public async store({ request, response }: HttpContextContract) {
    try {
      const body = request.body()
      const theVehicle = await Vehicle.create(body)
      return response
        .status(201)
        .json({ message: 'Vehículo creado exitosamente', data: theVehicle })
    } catch (error) {
      console.error(error)
      return response
        .status(500)
        .json({ message: 'Error al crear el vehículo', data: error.message })
    }
  }
  //Get
  public async index({ request, response }: HttpContextContract) {
    try {
      const page = request.input('page', 1)
      const perPage = request.input('per_page', 20)
      let vehicles: Vehicle[] = await Vehicle.query().preload('driver').paginate(page, perPage)
      if (vehicles && vehicles.length > 0) {
        return response
          .status(200)
          .json({ mensaje: 'registros de vehículos encontrados', data: vehicles })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontraron registros de vehículos', data: vehicles })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la busqueda de vehículos', data: error })
    }
  }

  public async show({ params, response }: HttpContextContract) {
    try {
      let vehicle: Vehicle | null = await Vehicle.query()
        .where('id', params.id)
        .preload('driver')
        .first()
      if (vehicle != null) {
        return response
          .status(200)
          .json({ mensaje: 'registro del vehículo encontrado', data: vehicle })
      } else {
        return response
          .status(404)
          .json({ mensaje: 'No se encontro registro del vehículo', data: vehicle })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la busqueda del vehículo', data: error })
    }
    
    // Update
    public async update({ params, request, response }: HttpContextContract) {
        try {
            const body = request.body()
            let theVehicle: Vehicle = await Vehicle.findOrFail(params.id)
                theVehicle.driver_id = body.driver_id
                theVehicle.license_plate= body.license_plate
                theVehicle.model= body.model
                theVehicle.capacity= body.capacity
                theVehicle.color= body.color
                theVehicle.velocity= body.velocity
                theVehicle.status= body.status
            // Guardar en la base de datos el registro actualizado
            await theVehicle.save()
            console.log("guardado", theVehicle);
            return response.status(200).json({ message: 'Vehículo actualizado exitosamente', data: theVehicle })

        } catch (error) {
            console.error(error);
            return response.status(500).json({ message: 'Error al actualizar el vehículo', data: error.message })
        }
    }
  }

    // Delete
    public async destroy({ params, response }: HttpContextContract) {
        try {    
          const vehicle: Vehicle = await Vehicle.findOrFail(params.id)
          if (vehicle) {
            vehicle.delete()
            return response.status(200).json({ mensaje: 'vehículo eliminado', data: vehicle })
          } else {
            return response
              .status(400)
              .json({ mensaje: 'no se encuentra el vehículo a eliminar', data: vehicle })
          }
        } catch (error) {
          return response
            .status(500)
            .json({ mensaje: 'Error en la eliminacion del vehículo', data: error })
        }
      }

}
