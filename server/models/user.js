/* Implemented by Chris */

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

module.exports = mongoose.model('User', new Schema({
    email: String,
    displayName: String,
    hashed_password: String,
    salt: String,
    householdId: String,
    id: String, /* used for password changing, the real id is '_id' */
    temp_str: String /* used for password changing */
}));
