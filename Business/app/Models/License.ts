import { DateTime } from 'luxon'
import { BaseModel, column } from '@ioc:Adonis/Lucid/Orm'

export default class License extends BaseModel {
  @column({ isPrimary: true })
  public id: number

  @column()
  public driver_id: number

  @column()
  public expiration_date: DateTime

  @column()
  public description: string

  @column()
  public type: string

  @column()
  public status: number

  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime
}
