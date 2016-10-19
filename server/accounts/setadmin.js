/* Implemented by: Chris */

/*
  POST /setadmin

  with body:
  {
    userId: String (defaults to current userId),
    setAdmin: ('true'|'false'),
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
  var userId = req.body.userId || currentUserId;

  var setAdmin;
  if (req.body.hasOwnProperty('setAdmin') &&
      (req.body.setAdmin === 'true' || req.body.setAdmin === 'false')) {
    setAdmin = (req.body.setAdmin === 'true') ? true : false;
  }
  else {
    res.json({
      success: false,
      message: "'setAdmin' must be 'true' or 'false''."
    });
    return;
  }

  if (!currentUserId) {
    res.json({
      success: false,
      message: 'Please supply a valid token.'
    });
    return;
  }
  if (!userId) {
    res.json({
      success: false,
      message: 'Please supply a userId.'
    });
    return;
  }
  utilities.checkUserIsAdmin(currentUserId, function(isAdmin) {
    if (!isAdmin) {
      res.json({
        success: false,
        message: 'Current user must be an admin of their household.'
      });
      return;
    }
    utilities.checkUsersAreInSameHousehold(currentUserId, userId, function(hh) {
      if (!hh) {
        res.json({
          success: false,
          message: 'Current user must be in the same household as userId.'
        });
        return;
      }
      if (setAdmin) {
        /* if we're setting, no need to check for number of admins */
        setUserAdminAndResponse(userId, true, res);
      }
      else {
        /*
          if we're unsetting, we need to check to make sure user
          isn't the sole admin of their household
        */
        utilities.getNumAdmins(hh, function(num) {
          if (num == 1) {
            res.json({
              success: false,
              message: "Sole admin of a household can't unset themselves as an admin."
            });
            return;
          }
          setUserAdminAndResponse(userId, false, res);
        });
      }
    });
  });
}

var setUserAdmin = function(userId, setAdmin, callback) {
  User.update({ '_id': userId }, { $set: { 'admin': setAdmin } }, undefined,
  function(err, res) {
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
