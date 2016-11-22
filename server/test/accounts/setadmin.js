/* Implemented by Chris */

process.env.NODE_ENV = 'test';

var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);

var config = require('./../../config');
var utilities = require('./../utilities.js');
var User = require('./../../models/user.js');

describe('setAdminSuccessTestsSimple', function() {
  var token;
  beforeEach(function(done) {
    utilities.makeCleanAccount({
      email: 'test@test',
      password: 'test123',
      username: 'test_username'
    }, function(err, res) {
      utilities.loginToAccount({
        email: 'test@test',
        password: 'test123'
      }, function(err, res) {
        token = res.res.body.token;
        utilities.createHousehold({
          token: token,
          houseHoldName: 'test_household',
          houseHoldPassword: 'household123'
        }, function(err, res) {
          done();
        });
      });
    });
  });
  afterEach(function(done) {
    utilities.deleteHousehold({
      token: token,
      houseHoldName: 'test_household',
      houseHoldPassword: 'household123',
    }, function() {
        utilities.deleteAccount({
        email: 'test@test',
        password: 'test123',
      }, done);
    });
  });
  describe('Make user admin of their own household (already admin)', function() {
    it('has success true', function(done) {
      utilities.loginAndSetAdmin({
        email: 'test@test',
        password: 'test123',
        setAdmin: 'true'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(true);
        done();
      });
    });
  });
});

describe('setAdminSuccessTestsComplex', function() {
  var token, originalUserToken, otherUserToken, originalUserId, otherUserId;
  beforeEach(function(done) {
    utilities.makeCleanAccount({
      email: 'test@test',
      password: 'test123',
      username: 'test_username'
    }, function(err, res) {
      utilities.loginToAccount({
        email: 'test@test',
        password: 'test123'
      }, function(err, res) {
        originalUserToken = res.res.body.token;
        originalUserId = res.res.body.id;
        utilities.createHousehold({
          token: originalUserToken,
          houseHoldName: 'test_household',
          houseHoldPassword: 'household123'
        }, function(err, res) {
          utilities.makeCleanAccount({
            email: 'test2@test2',
            password: 'test123',
            username: 'test_username2'
          }, function() {
            utilities.loginToAccount({
              email: 'test2@test2',
              password: 'test123'
            }, function(err, res) {
              otherUserToken = res.res.body.token;
              otherUserId = res.res.body.id;
              var token = res.res.body.token;
              utilities.joinHousehold({
                token: otherUserToken,
                houseHoldName: 'test_household',
                houseHoldPassword: 'household123'
              }, function(err, res) {
                done();
              });
            });
          });
        });
      });
    });
  });
  afterEach(function(done) {
    utilities.deleteHousehold({
      token: token,
      houseHoldName: 'test_household',
      houseHoldPassword: 'household123',
    }, function(err, res) {
        utilities.deleteAccount({
        email: 'test@test',
        password: 'test123',
      }, function() {
        utilities.deleteAccount({
          email: 'test2@test2',
          password: 'test123',
        }, done);
      });
    });
  });
  describe('Make other user admin', function() {
    it('has success true', function(done) {
      utilities.loginAndSetAdmin({
        email: 'test@test',
        password: 'test123',
        userId: otherUserId,
        setAdmin: 'true'
      }, function(err, res) {
        token = originalUserToken;
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(true);
        done();
      });
    });
  });
  describe('Let a user take over Household, kick original admin. ' +
           'Original admin loses privileges', function() {
    it('has success true', function(done) {
      utilities.loginAndSetAdmin({
        email: 'test@test',
        password: 'test123',
        userId: otherUserId,
        setAdmin: 'true'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(true);
        utilities.loginAndSetAdmin({
          email: 'test2@test2',
          password: 'test123',
          userId: originalUserId,
          setAdmin: 'false'
        }, function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(true);
          utilities.loginAndSetAdmin({
            email: 'test@test',
            password: 'test123',
            userId: otherUserId,
            setAdmin: 'false'
          }, function(err, res) {
            token = otherUserToken;
            res.should.have.status(200);
            res.res.body.should.have.property('success');
            res.res.body.success.should.be.eql(false);
            done();
          });
        });
      });
    });
  });
});

describe('setAdminFailureTests', function() {
  var token;
  beforeEach(function(done) {
    utilities.makeCleanAccount({
      email: 'test@test',
      password: 'test123',
      username: 'test_username'
    }, function(err, res) {
      utilities.loginToAccount({
        email: 'test@test',
        password: 'test123'
      }, function(err, res) {
        token = res.res.body.token;
        utilities.createHousehold({
          token: token,
          houseHoldName: 'test_household',
          houseHoldPassword: 'household123',
        }, done);
      });
    });
  });
  afterEach(function(done) {
    utilities.deleteHousehold({
      token: token,
      houseHoldName: 'test_household',
      houseHoldPassword: 'household123',
    }, function() {
        utilities.deleteAccount({
        email: 'test@test',
        password: 'test123',
      }, done);
    });
  });
  describe('No token/data', function() {
    it('returns forbidden', function(done) {
      chai.request(config.hostname)
        .post('/setadmin')
        .send({})
        .end(function(err, res) {
          res.should.have.status(403);
          done();
        });
    });
  });
  describe('No data with token', function() {
    it('returns success false', function(done) {
      utilities.loginToAccount({
        email: 'test@test',
        password: 'test123'
      }, function(err, res) {
        if (res.res.body.success) {
          chai.request(config.hostname)
            .post('/setadmin')
            .send({ token: res.res.body.token })
            .end(function(err, res) {
              res.should.have.status(200);
              res.res.body.should.have.property('success');
              res.res.body.success.should.be.eql(false);
              done();
            });
        }
        else {
          callback(err, res);
        }
      });
    });
  });
  describe('Missing setAdmin', function() {
    it('returns success false', function(done) {
      utilities.loginAndSetAdmin({
        email: 'test@test',
        password: 'test123'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('setAdmin something other than true or false', function() {
    it('returns success false', function(done) {
      utilities.loginAndSetAdmin({
        email: 'test@test',
        password: 'test123',
        setAdmin: 'notTrueOrFalse'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Unset admin when self is sole admin', function() {
    it('returns success false', function(done) {
      utilities.loginAndSetAdmin({
        email: 'test@test',
        password: 'test123',
        setAdmin: 'false'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe("Set user who isn't in same household", function() {
    var otherUserId;
    before(function(done) {
      utilities.makeCleanAccount({
        email: 'test2@test2',
        password: 'test123',
        username: 'test_username2'
      }, function() {
        utilities.loginToAccount({
          email: 'test2@test2',
          password: 'test123'
        }, function(err, res) {
          otherUserId = res.res.body.id;
          done();
        });
      });
    });
    after(function(done) {
      utilities.deleteAccount({
        email: 'test2@test2',
        password: 'test123',
      }, done);
    });
    it('returns success false', function(done) {
      utilities.loginToAccount({
        email: 'test@test',
        password: 'test123'
      }, function(err, res) {
        if (res.res.body.success) {
          chai.request(config.hostname)
            .post('/setadmin')
            .send({
              userId: otherUserId,
              setAdmin: 'true',
              token: res.res.body.token
            })
            .end(function(err, res) {
              res.should.have.status(200);
              res.res.body.should.have.property('success');
              res.res.body.success.should.be.eql(false);
              done();
            });
        }
        else {
          callback(err, res);
        }
      });
    });
  });
});
