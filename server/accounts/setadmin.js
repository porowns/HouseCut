/* Implemented by: Chris */

/*
  POST /setadmin

  with body:
  {
    userId: String,
    setAdmin: (1|true)|(0|false),
    token: String
  }

  Give or take admin privileges from a user.


  Only admins can call this, and the target user must be
  a roommate of the calling user.

  If a user is the sole admin of a household, they're not allowed
  to remove themselves as an admin.
*/

var User = require('../models/user.js');
var jwt = require('jsonwebtoken');
var jwtDecode = require('jwt-decode');
var crypto = require('crypto');
var rand = require('csprng');
var utilities = require('./../utilities.js');

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
    if (!currentUserIsAdmin) {
      res.json({
        success: false,
        message: 'currentUser must be an admin of their household.'
      });
      return;
    }
    utilities.checkUsersAreInSameHousehold(currentUserId, userId, function(hh) {
      if (hh) {
        /* if we're setting, no need to check for number of admins */
        if (setAdmin == "true" || setAdmin == "1") {
          setUserAdminAndResponse(userId, true, res);
          return;
        }
        /*
          if we're unsetting, we need to check to make sure user
          isn't the sole admin of their household
        */
        if (setAdmin == "false" || setAdmin == "0") {
          utilities.getNumAdmins(hh, function(num) {
            if (num == 1) {
              res.json({
                success: false,
                message: "Sole admin of a household can't unset themselves as an admin."
              });
            }
            else {
              setUserAdminAndResponse(userId, false, res);
              return;
            }
          });
        }
        /* input error */
        res.json({
          success: false,
          message: "setAdmin must be 'true', '1', 'false', or '0'."
        });
      }
      else {
        res.json({
          success: false,
          message: 'Current user must be in the same household as userId.'
        });
      }
    });
  }
}

var setUserAdmin = function(userId, setAdmin, callback) {
  User.update({ '_id': userId }, { $set: { 'admin': setAdmin } }, undefined, function(err, res) {
    if (err) {
      throw err;
    }
    callback(err, res);
  });
}

var setUserAdminAndResponse = function(userId, setAdmin, res, callback) {
  setUserAdmin(userId, setAdmin, function(err, r) {
    if (err) {
      throw err;
    }
    if (r && r.ok) {
      res.json({
        success: true,
        message: 'userId' + userId + ' admin set to ' + setAdmin
      });
      callback(true);
    }
    else {
      res.json({
        success: false,
        message: res.writeErrors.errmsg
      });
      callback(false);
    }
  });
}
