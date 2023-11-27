import { DateTime } from 'luxon'
import { BaseModel, BelongsTo, belongsTo, column } from '@ioc:Adonis/Lucid/Orm'
import Driver from './Driver'

export default class Vehicle extends BaseModel {
  @column({ isPrimary: true })
  public id: number

  @column()
  public driver_id: number
  @belongsTo(() => Driver, {
    foreignKey: 'driver_id',
  })
  driver: BelongsTo<typeof Driver>

  @column()
  public license_plate: string

  @column()
  public model: string

  @column()
  public capacity: number
  
  @column()
  public color: string

  @column()
  public velocity: number

  @column()
  public status: number

  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime
}