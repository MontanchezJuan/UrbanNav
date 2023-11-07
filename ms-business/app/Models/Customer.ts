import { DateTime } from 'luxon'
import { BaseModel, HasMany, column, hasMany } from '@ioc:Adonis/Lucid/Orm'
import Service from './Service'

export default class Customer extends BaseModel {
  @column({ isPrimary: true })
  public id: number

  @column()
  public user_id: string

  @column()
  public status: number

  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime

  @hasMany(() => Service, {
    foreignKey: 'service_id',
  })
  services: HasMany<typeof Service>
  // @hasMany(() => Contact, {
  //   foreignKey: 'customer_id',
  // })
  // contacts: HasMany<typeof Contact>
}
