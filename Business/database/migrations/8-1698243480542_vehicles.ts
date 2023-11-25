import BaseSchema from '@ioc:Adonis/Lucid/Schema'

export default class extends BaseSchema {
  protected tableName = 'vehicles'

  public async up () {
    this.schema.createTable(this.tableName, (table) => {
      table.increments('id')
      table.integer('driver_id').unsigned().references('drivers.id').onDelete('CASCADE')
      table.string('license_plate').notNullable().unique()
      table.string('model').notNullable()
      table.integer('capacity').notNullable()
      table.string('name').notNullable()
      table.string('color').notNullable()
      table.double('velocity').notNullable()
      table.integer('status').notNullable()

      /**
       * Uses timestamptz for PostgreSQL and DATETIME2 for MSSQL
       */
      table.timestamp('created_at', { useTz: true })
      table.timestamp('updated_at', { useTz: true })
    })
  }

  public async down () {
    this.schema.dropTable(this.tableName)
  }
}
