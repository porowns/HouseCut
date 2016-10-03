var User = require('../models/user');
var jwt = require('jsonwebtoken');
var crypto = require('crypto');
var rand = require('csprng');

module.exports = function(req, res) {
  var email = req.body.email;
  var password = req.body.password;

  if (!email || email == "") {
    res.json({
      success: false,
      message: 'Please enter an email.'
    });
  }
  else if (!password || password == "") {
    res.json({
      success: false,
      message: 'Please enter a password.'
    });
  }
  else {
    User.findOne({ 'email': email }, function(err, user) {
      if (err) {
        throw err;
      }
      if (user) {
        var hash_pw = crypto.createHash('sha512').update(user.salt + password).digest("hex");
        if (hash_pw == user.hashed_password) {
          var token = jwt.sign({
            email: user.email,
            id: user._id
          }, app.get('secret'), {
            /*expiresIn: "24h"*/
          });
          res.json({
            success: true,
            message: 'Login success',
            id: user.id,
            token: token
          });
        }
        else {
          res.json({
            success: false,
            message: 'Wrong password.'
          });
        }
      }
      else {
        res.json({
          success: false,
          message: 'No user with that email is registered.'
        });
      }
    });
  }
}
