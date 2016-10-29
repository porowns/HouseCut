var User = require('./models/user.js');
var Household = require('./models/household.js');


/* Implemented by Chris */
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

/* Implemented by Chris */
module.exports.checkUserIsAdmin = function(userId, callback) {
  User.findOne({ '_id': userId }, function(err, res) {
    if (err) {
      throw err;
    }
    var isAdmin = res ? res.admin : false;
    callback(isAdmin);
  });
}

/* Implemented by Chris */
module.exports.checkUsersAreInSameHousehold = function(userId1, userId2, callback) {
  User.findOne({ '_id': userId1 }, function(err, user1) {
    if (user1) {
      module.exports.checkUserIsInHousehold(userId2, user1.householdId, function(hh) {
        callback(hh);
      });
    }
    else {
      callback(false);
    }
  });
}

/* Implemented by Chris */
module.exports.getHouseholdFromUserId = function(userId, callback) {
  User.findOne({ '_id': userId }, function(err, user) {
    if (user) {
      Household.findOne({ '_id': user.householdId }, function(err, hh) {
        if (hh) {
          callback(hh);
        }
        else {
          callback(false);
        }
      });
    }
    else {
      callback(false);
    }
  });
}

/* Implemented by Chris */
module.exports.addUserToHousehold = function(userId, hhName, hhPassword, callback) {
  Household.findOne({ 'houseHoldName': hhName }, function(err, hh) {
    if (err) {
      throw err;
    }
    if (hh) {
      var hash_pw = crypto.createHash('sha512').update(hh.salt + hhPass).digest("hex");
      if (hash_pw == hh.hashed_password) {
        /* success! add user to household */
        User.update({ '_id': userId }, { $set: { 'householdId': hh._id } }, undefined, function(err, r) {
          if (err) {
            throw err;
          }
          Household.update({ '_id': hh._id }, { $push: { 'HouseholdMembers': userId } }, undefined, function(err, r) {
            if (err) {
              throw err;
            }
            callback({
              success: true,
              message: "Successfully added user to household " + hh.houseHoldName,
              householdId: hh._id
            });
          });
        });
      }
      else {
        callback({
          success: false,
          message: "Wrong household password."
        });
      }
    }
    else {
      callback({
        success: false,
        message: "No household with that name was found."
      });
    }
  });
}

/* Implemented by Chris */
module.exports.removeUserFromHousehold = function(userId, householdId, callback) {
  User.update({ '_id': userId }, {
    $set: { 'householdId': 0, 'admin': false }
  }, undefined, function(err, r) {
    if (err) {
      throw err;
    }
    Household.update({ '_id': householdId }, { $pull: { 'HouseholdMembers': userId } }, undefined, function(err, r) {
      if (err) {
        throw err;
      }
      /*
        TODO: advance or unassign tasks that were assigned to
        this user to keep the household's tasks in a good state.
      */
      callback({
        success: true,
        message: "Successfully removed user from household"
      });
    });
  });
}

/* Implemented by Chris */
module.exports.getNumAdmins = function(hh, callback) {
  var roommates = hh.HouseholdMembers;
  var numAdmins = 0;
  for (var i = 0; i < roommates.length; i++) {
    if (roommates[i].admin) {
      numAdmins++;
    }
  }
  callback(numAdmins);
}
