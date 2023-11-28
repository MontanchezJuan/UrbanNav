import { DateTime } from 'luxon'
import { BaseModel, ManyToMany, column, manyToMany } from '@ioc:Adonis/Lucid/Orm'
import Trip from './Trip'

export default class Point extends BaseModel {
  @column({ isPrimary: true })
  public id: number

  @column()
  public latitude: number

  @column()
  public longitude: number

  @column()
  public status: number

  @manyToMany(() => Trip, {
    pivotTable: 'trip_points',
    pivotForeignKey: 'trip_id',
    pivotRelatedForeignKey: 'point_id',
  })
  public trips: ManyToMany<typeof Trip>

  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime
}
