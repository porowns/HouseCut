var mongoose = require('mongoose');
var Schema = mongoose.Schema;

module.exports = mongoose.model('User', new Schema({
    temp_str: String,
    email: String,
    displayName: String,
    hashed_password: String,
    salt: String,
    id: String
}));
