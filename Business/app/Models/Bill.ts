import { DateTime } from 'luxon'
import { BaseModel, BelongsTo, belongsTo, column } from '@ioc:Adonis/Lucid/Orm'
import Service from './Service'

export default class Bill extends BaseModel {
  @column({ isPrimary: true })
  public id: number

  @column()
  public service_id: number
  @belongsTo(() => Service, {
    foreignKey: 'service_id',
  })
  service: BelongsTo<typeof Service>

  @column()
  public credit_card_id: string

  @column()
  public status: number

  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime
}
