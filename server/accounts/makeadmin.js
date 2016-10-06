/* Implemented by: Chris */

/*
  POST /setAdmin

  with body: currentUserId, userId, setAdmin
*/

var User = require('../models/user.js');
var jwt = require('jsonwebtoken');
var crypto = require('crypto');
var rand = require('csprng');
var utilities = require('../utilities.js');

module.exports = function(req, res) {
  var currentUserId = req.body.currentUserId;
  var userId = req.body.userId;
  var setAdmin = (req.body.setAdmin == 'true' || req.body.setAdmin == '1') ? true : false;

  if (!currentUserId) {
    res.json({
      success: false,
      message: 'Please supply a currentUserId.'
    });
  }
  else if (!userId) {
    res.json({
      success: false,
      message: 'Please supply a userId.'
    });
  }
  else {
    utilities.checkUserIsAdmin(currentUserId, function(result) {
      if (result) {
        User.update({ '_id': userId }, { $set { 'admin': setAdmin } }, undefined, function(err, res) {
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
          message: 'currentUserId must be an admin of their household.'
        });
      }
    });
  }
}
