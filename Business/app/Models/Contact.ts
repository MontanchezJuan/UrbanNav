import { DateTime } from 'luxon'
import { BaseModel, BelongsTo, belongsTo, column } from '@ioc:Adonis/Lucid/Orm'
import Customer from './Customer'

export default class Contact extends BaseModel {
  @column({ isPrimary: true })
  public id: number

  @column()
  public customer_id: number
  
  @column()
  public name: string

  @column()
  public email: string

  @column()
  public phone_number: string

  @column()
  public is_emergy_contact: boolean

  @column()
  public status: number

  @belongsTo(() => Customer, {
    foreignKey: 'customer_id',
  })
  public customer: BelongsTo<typeof Customer>

  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime
}
