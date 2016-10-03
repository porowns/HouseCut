var mongoose = require('mongoose');
var Schema = mongoose.Schema;

module.exports = mongoose.model('Household', new Schema({
  houseHoldName: String,
	houseHoldPassword: String,
	joinCode: String
}));
