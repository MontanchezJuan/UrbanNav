import { DateTime } from 'luxon'
import { BaseModel, HasMany, column, hasMany, hasOne } from '@ioc:Adonis/Lucid/Orm'
import Trip from './Trip'

export default class Driver extends BaseModel {
  @column({ isPrimary: true })
  public id: number

  @column()
  public user_id: string

  @column()
  public is_active: boolean

  @column()
  public status: number

  // relacion 1 a 1 con licencia
  /**
   * @hasOne(() => License,{
   * foreignkey: "id_driver"})
   * license : HasOne<typeof License>
   */

  // relacion 1 a N con vehiculos
  /**
   * @hasMany(() => Vehicle,{
   * foreignkey: "id_driver"})
   * vehicles : HasMany<typeof Vehicle>
   */

  @hasMany(() => Trip, {
    foreignKey: 'driver_id',
  })
  trips: HasMany<typeof Trip>

  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime
}
