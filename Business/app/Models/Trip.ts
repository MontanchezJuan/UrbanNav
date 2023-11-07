import { DateTime } from 'luxon'
import {
  BaseModel,
  column,
  hasOne,
  belongsTo,
  BelongsTo,
  HasOne,
  hasMany,
  HasMany,
} from '@ioc:Adonis/Lucid/Orm'
import Driver from './Driver'
import Service from './Service'
import TripPoint from './TripPoint'

export default class Trip extends BaseModel {
  @column({ isPrimary: true })
  public id: number

  @column()
  public origin: number

  @column()
  public destination: number

  @column()
  public driver_id: number

  @column()
  public started_at: DateTime

  @column()
  public finished_at: DateTime

  @column()
  public distance: number

  @column()
  public status: number

  @belongsTo(() => Driver, {
    foreignKey: 'driver_id',
  })
  public driver: BelongsTo<typeof Driver>

  @hasOne(() => Service, {
    foreignKey: 'trip_id',
  })
  public service: HasOne<typeof Service>

  @hasMany(() => TripPoint, {
    foreignKey: 'trip_id',
  })
  public tripPoint: HasMany<typeof TripPoint>

  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime
}
