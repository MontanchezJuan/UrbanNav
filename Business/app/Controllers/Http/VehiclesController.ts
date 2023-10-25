import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Vehicle from 'App/Models/Vehicle';

export default class VehiclesController {
    //Create
    public async store({request}:HttpContextContract){
        let body = request.body();
        const theVehicle = await Vehicle.create(body); //el await le dice al store para que espere mientras ejecuta
        return theVehicle;

    }
    //Get
    public async index({ request }: HttpContextContract) {
        const page = request.input('page', 1);
        const perPage = request.input("per_page", 20);
        let vehicles:Vehicle[]= await Vehicle.query().paginate(page, perPage)
        return vehicles;
    }
    
    public async show({ params }: HttpContextContract) {
        return Vehicle.query().where("id",params.id)
    }
}
