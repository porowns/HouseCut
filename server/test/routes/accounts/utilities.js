/* Implemented by Chris */

var config = require('./../../../config');
var chai = require('chai');
var chaiHttp = require('chai-http');
chai.use(chaiHttp);

module.exports.deleteAccount = function(data, callback) {
  module.exports.loginToAccount(data, function(err, res) {
    if (res.res.body.success) {
      var token = res.res.body.token;
      data['token'] = token;
      chai.request(config.hostname)
        .post('/deleteaccount')
        .send(data)
        .end(function(err, res) {
          callback(err, res);
        });
    }
    else {
      callback(err, res);
    }
  });
}

module.exports.makeCleanAccount = function(data, callback) {
  module.exports.deleteAccount(data, function() {
    chai.request(config.hostname)
      .post('/register')
      .send(data)
      .end(function(err, res) {
        callback(err, res);
      });
  });
}

module.exports.makeAccount = function(data, callback) {
  chai.request(config.hostname)
    .post('/register')
    .send(data)
    .end(function(err, res) {
      callback(err, res);
    });
}

module.exports.loginToAccount = function(data, callback) {
  chai.request(config.hostname)
    .post('/login')
    .send(data)
    .end(function(err, res) {
      callback(err, res);
    });
}
