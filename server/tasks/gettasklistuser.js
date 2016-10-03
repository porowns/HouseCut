/* Implemented by: Chris */
/* GET /usertasklist */

module.exports = function(req, res) {
  var currentUserId = req.body.currentUserId;
  var userId = req.body.userId;

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
          var index = hh.members.indexOf(userId);
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
            var userTasklist = hh.tasklist.filter(function(e, i, a) {
              return (e.currentlyAssigned == userId);
            });
            res.json({
              success: true,
              tasklist: userTasklist;
            })
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
