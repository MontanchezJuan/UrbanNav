import { DateTime } from 'luxon'
import { BaseModel, HasMany, belongsTo, column, hasMany } from '@ioc:Adonis/Lucid/Orm'
import CommentandRating from './CommentandRating'

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

  /**
   * @belongsTo(() => Customer,{
   * foreignkey: "customer_id"})
   * customer: BelongsTo<typeof Customer>
   */
  
  /**
   * @belongTo(() => Trip,{
   * foreignkey: "trip_id"})
   * trip: belongsTo<typeof Trip>
   */

  /**
   * @hasOne(() => Bill,{
   * foreignkey: "service_id"})
   * bill: HasOne<typeof Bill>
   */

  @hasMany(() => CommentandRating,{
    foreignKey: "service_id"
  })
  commentsAndRatings: HasMany<typeof CommentandRating>
  

  @column()
  @column.dateTime({ autoCreate: true })
  public createdAt: DateTime

  @column.dateTime({ autoCreate: true, autoUpdate: true })
  public updatedAt: DateTime
}
