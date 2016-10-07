/* Implemented by: Chris */

/*
  POST /setadmin

  with body: { userId: String, setAdmin: (1|true)|(0|false), token: String }
*/

var User = require('../models/user.js');
var jwt = require('jsonwebtoken');
var jwtDecode = require('jwt-decode');
var crypto = require('crypto');
var rand = require('csprng');

module.exports = function(req, res) {
  var token = req.body.token;
  var decoded = jwtDecode(token);
  var currentUserId = decoded.id;
  var currentUserIsAdmin = decoded.admin;
  var userId = req.body.userId;
  var setAdmin = (req.body.setAdmin == 'true' || req.body.setAdmin == '1') ? true : false;

  if (!currentUserId) {
    res.json({
      success: false,
      message: 'Please supply a valid token.'
    });
  }
  else if (!userId) {
    res.json({
      success: false,
      message: 'Please supply a userId.'
    });
  }
  else {
    if (currentUserIsAdmin) {
      User.update({ '_id': userId }, { $set: { 'admin': setAdmin } }, undefined, function(err, res) {
        if (err) {
          throw err;
        }
        if (res && res.ok) {
          res.json({
            success: true,
            message: 'userId' + userId + ' admin set to ' + setAdmin
          });
        }
        else {
          res.json({
            success: false,
            message: res.writeErrors.errmsg
          });
        }
      });
    }
    else {
      res.json({
        success: false,
        message: 'currentUser must be an admin of their household.'
      });
    }
  }
}
