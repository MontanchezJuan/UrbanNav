import { DateTime } from 'luxon'
import {
  BaseModel,
  column,
  hasOne,
  belongsTo,
  BelongsTo,
  HasOne,
  manyToMany,
  ManyToMany,
} from '@ioc:Adonis/Lucid/Orm'
import Driver from './Driver'
import Service from './Service'
import Point from './Point'

export default class Trip extends BaseModel {
  @column({ isPrimary: true })
  public id: number

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

  @manyToMany(() => Point, {
    pivotTable: 'trip_points',
    pivotForeignKey: 'point_id',
    pivotRelatedForeignKey: 'trip_id',
  })
  public points: ManyToMany<typeof Point>

  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime
}
