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

/* Implemented by Simon */
module.exports.makeCleanAccountAndLogin = function(data, callback) {
  module.exports.makeCleanAccount(data, function() {
    chai.request(config.hostname)
      .post('/login')
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
module.exports.loginAndSetAdmin = function(data, callback) {
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

/* Implemented by Chris */
module.exports.postRoommates = function(data, callback) {
  chai.request(config.hostname)
    .post('/household/roommates')
    .send(data)
    .end(function(err, res) {
      if (callback)
        callback(err, res);
    });
};

/* Implemented by Chris */
module.exports.loginAndPostRoommates = function(data, callback) {
  module.exports.loginToAccount(data, function(err, res) {
    if (res.res.body.success) {
      var token = res.res.body.token;
      var id = res.res.body.id;
      data['token'] = token;
      module.exports.postRoommates(data, function(err, res) {
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

// Implemented by Kaleb
module.exports.createHousehold = function(data,callback) {
  chai.request(config.hostname)
    .post('/createhousehold')
    .send(data)
    .end(function(err,res) {
      if (callback)
        callback(err,res);
    });
}

// Implemented by Kaleb
module.exports.joinHousehold = function(data,callback) {
  chai.request(config.hostname)
    .post('/joinhousehold')
    .send(data)
    .end(function(err,res) {
      if (callback)
        callback(err,res);
    });
}

// Implemented by Kaleb
module.exports.deleteHousehold = function(data,callback) {
  chai.request(config.hostname)
    .post('/deletehousehold')
    .send(data)
    .end(function(err,res) {
      if (callback)
        callback(err,res);
    });
}

// Implemented by Kaleb
module.exports.createTask = function(data,callback) {
  chai.request(config.hostname)
  .post('/household/createtask')
  .send(data)
  .end(function(err,res) {
    if (callback)
      callback(err,res);
  });
}

// Implemented by Kaleb
module.exports.deleteTask = function(data,callback) {
  chai.request(config.hostname)
  .post('/household/deletetask')
  .send(data)
  .end(function(err,res) {
    if (callback)
      callback(err,res);
  });
}

// Implemented by Kaleb
module.exports.completeTask = function(data,callback) {
  chai.request(config.hostname)
  .post('/household/completetask')
  .send(data)
  .end(function(err,res) {
    if (callback)
      callback(err,res);
  });
}
