/* Implemented by: Chris and Kaleb */

var SchemaObject = require('schema-object');


module.exports = new SchemaObject({
  name: String,
  recurring: Boolean,
  recurringIntervalDays: Number,
  type: String /* "Rotating", "Assigned", "Voluntary" */
  currentlyAssigned: Number /* user ID */
});
