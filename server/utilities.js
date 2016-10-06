var User = require('./models/user.js');
var Household = require('./models/household.js');

module.exports.checkUserIsInHousehold = function(userId, householdId, callback) {
  Household.findOne({ '_id': householdId }, function(err, hh) {
    if (err) {
      throw err;
    }
    if (hh) {
      if (userId) {
        var index = hh.householdMembers.indexOf(userId);
        if (index == -1) {
          callback(false);
        }
        else {
          callback(hh);
        }
      }
    }
    else {
      callback(false);
    }
  });
}

module.exports.checkUserIsAdmin = function(userId, callback) {
  User.findOne({ '_id': userId }, function(err, res) {
    if (err) {
      throw err;
    }
    var isAdmin = res ? res.admin : false;
    callback(isAdmin);
  });
}
