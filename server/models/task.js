/* Implemented by: Chris and Kaleb */

var SchemaObject = require('schema-object');

module.exports = new SchemaObject({
  name: { type: String, minLength: 1 },
  recurring: Boolean,
  recurringIntervalDays: Number,
  type: { type: String, minLength: 1, enum: [ 'Rotating', 'Assigned', 'Voluntary' ] },
  currentlyAssigned: { type: String, minLength: 1 } /* user ID */
});
