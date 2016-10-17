var Household = require('../models/household');
var User = require('../models/user.js');

// To Depcryt Household Password
var jwt = require('jsonwebtoken');
var jwtDecode = require('jwt-decode');
var crypto = require('crypto');
var rand = require('csprng');

module.exports = function (req, res) {

var token = req.body.token;
var houseHoldName = req.body.houseHoldName;
var houseHoldPassword = req.body.houseHoldPassword;

// Getting User ID from Input
var decoded = jwtDecode(token);
var currentUserId = decoded.id;

// Access User
User.findOne({ '_id' : currentUserId}, function(err, household) {

}); // End of User.findOne  
} // End of Function
