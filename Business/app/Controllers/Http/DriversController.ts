import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Driver from 'App/Models/Driver';

export default class DriversController {
    public async index({ request }: HttpContextContract){
        let body = request.body();
        let drivers:Driver[] = await Driver.query();
        for (const driver of drivers){
            if( body.withVehicles == true){
                // no funciona hasta que no se descomente en le modelo la relacion de vehiculos
                await driver.load("vehicles")
            }
            if( body.withTrips == true){
                await driver.load("trips")
            }
            if( body.withLicense == true){
                // no funciona hasta que no se descomente en le modelo la relacion de licensia
                await driver.load("license")
            }
        }
        // metodo para retornar a todos los conductores
    }
    public async conditionalList({ request }: HttpContextContract){
        let body = request.body();
        let drivers:Driver[] = await Driver.query().where();
        // retornar los conductores que cumplan ciertas condiciones
    }
    public async show(){
        // metodo para retornar a un conductor con toda su informacion asociada
    }    
    public async conditionalShow(){
        // metodo para retornar a un conductor con la informacion asociada necesaria en las condiciones
    }
    public async store(){
        // metodo para crear un registro de conductor en la base de datos
    }
    public async update(){
        //metodo para actualizar la informacion de un conductor
    }
    public async destroy(){
        //metodo para eliminar un registro de conductor, recordar que la eliminacion sera solo una actualizacion de estado
    }
}
