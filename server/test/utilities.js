var config = require('./../config');
var chai = require('chai');
var chaiHttp = require('chai-http');
chai.use(chaiHttp);

/* Implemented by Chris */
module.exports.deleteAccount = function(data, callback) {
  module.exports.loginToAccount(data, function(err, res) {
    if (res.res.body.success) {
      var token = res.res.body.token;
      data['token'] = token;
      chai.request(config.hostname)
        .post('/deleteaccount')
        .send(data)
        .end(function(err, res) {
          if (callback)
            callback(err, res);
        });
    }
    else {
      if (callback)
        callback(err, res);
    }
  });
};

/* Implemented by Chris */
module.exports.makeCleanAccount = function(data, callback) {
  module.exports.deleteAccount(data, function() {
    chai.request(config.hostname)
      .post('/register')
      .send(data)
      .end(function(err, res) {
        if (callback)
          callback(err, res);
      });
  });
};

/* Implemented by Chris */
module.exports.makeAccount = function(data, callback) {
  chai.request(config.hostname)
    .post('/register')
    .send(data)
    .end(function(err, res) {
      if (callback)
        callback(err, res);
    });
};

/* Implemented by Chris */
module.exports.loginToAccount = function(data, callback) {
  chai.request(config.hostname)
    .post('/login')
    .send(data)
    .end(function(err, res) {
      if (callback)
        callback(err, res);
    });
};

/* Implemented by Chris */
module.exports.setAdmin = function(data, callback) {
  module.exports.loginToAccount(data, function(err, res) {
    if (res.res.body.success) {
      var token = res.res.body.token;
      var id = res.res.body.id;
      data['token'] = token;
      if (!data['userId']) {
        data['userId'] = id;
      }
      chai.request(config.hostname)
        .post('/setadmin')
        .send(data)
        .end(function(err, res) {
          if (callback)
            callback(err, res);
        });
    }
    else {
      callback(err, res);
    }
  });
};

/* Implemented by Chris */
module.exports.getRoommates = function(data, callback) {
  var queryParams = '?';
  if (data.token)
    queryParams += 'token=' + data.token;
  if (data.userId) {
    if (data.token)
      queryParams += '&';
    queryParams += 'userId=' + data.userId;
  }
  chai.request(config.hostname)
    .get('/household/roommates' + queryParams)
    .send(data)
    .end(function(err, res) {
      if (callback)
        callback(err, res);
    });
};

/* Implemented by Chris */
module.exports.loginAndGetRoommates = function(data, callback) {
  module.exports.loginToAccount(data, function(err, res) {
    if (res.res.body.success) {
      var token = res.res.body.token;
      var id = res.res.body.id;
      data['token'] = token;
      module.exports.getRoommates(data, function(err, res) {
        if (callback)
          callback(err, res);
      });
    }
    else {
      if (callback)
        callback(err, res);
    }
  });
};

/* TODO: write createHousehold and deleteHousehold */

// Implemented by Kaleb
module.exports.createHousehold = function(data,callback) {
  chai.request(config.hostname)
    .post('/creathousehold')
    .send(data)
    .end(function(err,res) {
      if (callback)
        callback(err,res);
    });
}
