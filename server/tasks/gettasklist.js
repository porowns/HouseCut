/* Implemented by: Chris */
/*
  GET /tasklist?currentUserId=X&(userId=Y|householdId=Z)
*/

module.exports = function(req, res) {
  var currentUserId, userId, householdId;

  /* make sure there's a currentUserId */
  if (req.query && req.query.currentUserId) {
    currentUserId = req.query.currentUserId;
  }
  else {
    res.json({
      success: false,
      message: 'Please supply a currentUserId parameter in the query.'
    });
  }
  /* make sure there's either a userId or householdId */
  if (req.query && req.query.userId) {
    userId = req.query.userId;
  }
  else {
    if (req.query && req.query.householdId) {
      householdId = req.query.householdId;
    }
    else {
      res.json({
        success: false,
        message: 'Please supply either a userId or householdId parameter in the query.'
      });
    }
  }

  /*
    currentUserId has to be in the same household as householdId or
    the household that userId is in
  */
  User.findOne({ '_id': currentUserId }, function(err, user) {
    if (err) {
      throw err;
    }
    if (user) {
      Household.findOne({ '_id': user.householdId }, function(err, hh) {
        if (err) {
          throw err;
        }
        if (hh) {
          if (userId) {
            /* if they supplied a userId */

            var index = hh.householdMembers.indexOf(userId);
            if (index == -1) {
              /* user wasn't in the same household as caller */
              res.json({
                success: false,
                message: 'userId wasn\'t in the same household as currentUserId.'
              });
            }
            else {
              /* all good */
              /* only keep the tasks that are assigned to given userId */
              var userTasklist = hh.taskList.filter(function(e, i, a) {
                return (e.currentlyAssigned == userId);
              });
              res.json({
                success: true,
                tasklist: userTasklist
              });
            }
          }
          else if (householdId) {
            /* if they supplied a householdId */

            res.json({
              success: true,
              tasklist: taskList /* TODO: make sure this field is right */
            });
          }
        }
        else {
          res.json({
            success: false,
            message: 'currentUserId\'s household was not found.'
          });
        }
      });
    }
    else {
      res.json({
        success: false,
        message: 'No user with currentUserId found.'
      });
    }
  });
}
