import type { HttpContextContract } from '@ioc:Adonis/Core/HttpContext'
import axios from 'axios'
import Env from '@ioc:Adonis/Core/Env'

export default class Security {
  public async handle({ request, response }: HttpContextContract, next: () => Promise<void>) {
    let theRequest = request.toJSON()
    if (theRequest.headers.authorization) {
      let token = theRequest.headers.authorization.replace('Bearer ', '')
      let thePermission: object = {
        route: theRequest.url,
        method: theRequest.method,
      }
      try {
        const result = await axios.post(
          `${Env.get('MS-SECURITY')}/security/permissions-validation`,
          thePermission,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        console.log('La respuesta de ms-security >' + result.status + '<')
        if (result.status == 200) {
          await next()
        } else {
          console.log('no puede ingresar')
          return response.status(401)
        }
      } catch (error) {
        return response.status(401)
      }
    } else {
      return response.status(401)
    }
  }
}
