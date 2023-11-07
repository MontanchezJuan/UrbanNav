import BaseSchema from '@ioc:Adonis/Lucid/Schema'

export default class extends BaseSchema {
  protected tableName = 'trips'

  public async up() {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id')
      table.integer('origin_id').unsigned().notNullable()
      table.integer('destination_id').unsigned().notNullable()
      table
        .integer('driver_id')
        .unsigned()
        .references('drivers.id')
        .onDelete('CASCADE')
        .notNullable()
      table.dateTime('started_at')
      table.dateTime('finished_at')
      table.double('distance')
      table.integer('status')
      table.timestamp('created_at', { useTz: true })
      table.timestamp('updated_at', { useTz: true })
    })
  }

  public async down() {
    this.schema.dropTable(this.tableName)
  }
}
