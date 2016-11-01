var mongoose = require('mongoose');
var Schema = mongoose.Schema;

module.exports = mongoose.model('Household', new Schema({
  houseHoldName: String,
	hashed_password: String,
  salt: String,
  id: String,
  temp_str: String,
  HouseholdMembers: Array,
  taskList: Array
}));
