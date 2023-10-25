import { DateTime } from 'luxon'
import {
  BaseModel,
  BelongsTo,
  HasMany,
  HasOne,
  belongsTo,
  column,
  hasMany,
  hasOne,
} from '@ioc:Adonis/Lucid/Orm'
import CommentandRating from './CommentandRating'
import Customer from './Customer'
import Trip from './Trip'
import Bill from './Bill'

export default class Service extends BaseModel {
  @column({ isPrimary: true })
  public id: number

  @column()
  public customer_id: number

  @column()
  public trip_id: number

  @column()
  public price: number

  @column()
  public status: number

  @belongsTo(() => Customer, {
    foreignKey: 'customer_id',
  })
  customer: BelongsTo<typeof Customer>

  @belongsTo(() => Trip, {
    foreignKey: 'trip_id',
  })
  trip: BelongsTo<typeof Trip>

  @hasOne(() => Bill, {
    foreignKey: 'service_id',
  })
  bill: HasOne<typeof Bill>

  @hasMany(() => CommentandRating, {
    foreignKey: 'service_id',
  })
  commentsAndRatings: HasMany<typeof CommentandRating>

  @column()
  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime
}
