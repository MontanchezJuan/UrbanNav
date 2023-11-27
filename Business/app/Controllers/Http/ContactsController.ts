import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import Contact from 'App/Models/Contact'

export default class ContactsController {
  public async index({ request, response }: HttpContextContract) {
    try{
      const page = request.input('page', 1)
      const perPage = request.input('per_page', 20)
      let contacts: Contact[]= await Contact.query()
      .preload('customer')
      .paginate(page, perPage)
      if(contacts && contacts.length > 0){
      return response
      .status(200)
      .json({ mensaje: 'registros de contactos encontrados', data: contacts })
      } else {
      return response
        .status(404)
        .json({ mensaje: 'No se encontraron registros de contactos', data: contacts })
    }
    }catch (error){
        return response
        .status(500)
        .json({ mensaje: 'Error en la busqueda de contactos', data: error 
        })
    }
  }

  public async show({ params, response }: HttpContextContract) {
      try{
          let contact: Contact | null = await Contact.query()
          .where('id', params.id)
          .preload('customer')
          .first()
          if(contact != null){
              return response
              .status(200)
              .json({ mensaje: 'registro del contacto encontrado', data: contact })
          } else {
              return response
              .status(404)
              .json({ mensaje: 'No se encontro registro del contacto', data: contact }) 
          }
        }catch (error){
            return response
            .status(500)
            .json({ mensaje: 'Error en la busqueda del contacto', data: error })
        }
  }

  public async store({ request, response }: HttpContextContract) {
    try {
      const body = request.body();
      const theContact = await Contact.create(body);
      return response.status(201).json({ message: 'Contacto creado exitosamente', data: theContact });
  } catch (error) {
      console.error(error);
      return response.status(500).json({ message: 'Error al crear el Contacto', data: error.message });
  }
  }

  public async update({ params, request, response }: HttpContextContract) {
    try {
      const body = request.body()
      let contact: Contact = await Contact.findOrFail(params.id)
          contact.customer_id = body.customer_id
          contact.name = body.name
          contact.email = body.email
          contact.phone_number = body.phone_number
          contact.is_emergy_contact = body.is_emergy_contact
          contact.status = body.estatus
      // Guardar en la base de datos el registro actualizado
      await contact.save()
      return response.status(200).json({ message: 'Contacto actualizado exitosamente', data: contact })
    } catch (error) {
        console.error(error);
        return response.status(500).json({ message: 'Error al actualizar el Contacto', data: error.message })
    }
  }

  public async destroy({ params, response }: HttpContextContract) {
    try {    
      const contact: Contact = await Contact.findOrFail(params.id)
      if (contact) {
        contact.delete()
        return response.status(200).json({ mensaje: 'Contacto eliminado', data: contact })
      } else {
        return response
          .status(400)
          .json({ mensaje: 'no se encuentra el Contacto a eliminar', data: contact })
      }
    } catch (error) {
      return response
        .status(500)
        .json({ mensaje: 'Error en la eliminacion del Contacto', data: error })
    }
  }
}

