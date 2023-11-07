import { DateTime } from 'luxon'
import { BaseModel, BelongsTo, belongsTo, column } from '@ioc:Adonis/Lucid/Orm'
import Trip from './Trip'

export default class TripPoint extends BaseModel {
  @column({ isPrimary: true })
  public id: number

  @column()
  public name: string

  @column()
  public latitude: number

  @column()
  public longitude: number

  @column()
  public status: number

  @belongsTo(() => Trip, {
    foreignKey: 'trip_id',
  })
  public trip: BelongsTo<typeof Trip>

  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime
}
