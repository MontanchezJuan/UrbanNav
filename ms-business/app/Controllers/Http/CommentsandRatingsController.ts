import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import CommentandRating from 'App/Models/CommentandRating'

export default class CommentsandRatingsController {
  public async index({ request }: HttpContextContract) {
    const page = request.input('page', 1)
    const perPage = request.input('per_page', 20)
    return await CommentandRating.query().preload('service').paginate(page, perPage)
    // metodo para retornar a todos los comentarios y ratings
  }
  public async conditionalIndex({ request }: HttpContextContract) {
    // retornar los comentarios y ratings que cumplan ciertas condiciones
  }
  public async show({ params }: HttpContextContract) {
    return await CommentandRating.query().where('id', params.id).preload('service')
    // metodo para retornar a un comentario y rating con toda su informacion asociada
  }
  public async conditionalShow() {
    // metodo para retornar a un comentario y rating con la informacion asociada necesaria en las condiciones
  }
  public async store({ request }: HttpContextContract) {
    let body = request.body()
    const commentandRating = await CommentandRating.create(body)
    return commentandRating
    // metodo para crear un registro de comentario y rating en la base de datos
  }
  public async update({ params, request }: HttpContextContract) {
    const body = request.body()
    const commentandRating: CommentandRating = await CommentandRating.findOrFail(params.id)
    commentandRating.service_id = body.service_id
    commentandRating.sender_id = body.sender_id
    commentandRating.receptor_id = body.receptor_id
    commentandRating.description = body.description
    commentandRating.rating = body.rating
    commentandRating.status = body.status
    return commentandRating.save()

    //metodo para actualizar la informacion de un comentario y rating
  }
  public async destroy({ params, response }: HttpContextContract) {
    const commentandRating: CommentandRating = await CommentandRating.findOrFail(params.id)
    response.status(204)
    return commentandRating.delete()
    //metodo para eliminar un registro de comentario y rating, recordar que la eliminacion sera solo una actualizacion de estado
  }
}
