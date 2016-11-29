// Implemented by Kaleb
var SchemaObject = require('schema-object');

module.exports = new SchemaObject({
  name: { type: String, minLength: 1 },
});
