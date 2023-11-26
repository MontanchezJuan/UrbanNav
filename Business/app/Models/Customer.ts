import { DateTime } from 'luxon'
import { BaseModel, HasMany, column, hasMany } from '@ioc:Adonis/Lucid/Orm'
import Service from './Service'
import Contact from './Contact'

export default class Customer extends BaseModel {
  @column({ isPrimary: true })
  public id: number

  @column()
  public user_id: string
  @column()
  public user: any

  @column()
  public status: number

  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime

  @hasMany(() => Contact, {
    foreignKey: 'customer_id',
  })
  public contacts: HasMany<typeof Contact>

  @hasMany(() => Service, {
    foreignKey: 'customer_id',
  })
  public services: HasMany<typeof Service>
}
