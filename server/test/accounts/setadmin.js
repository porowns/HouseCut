/* Implemented by Chris */

process.env.NODE_ENV = 'test';

var config = require('./../../../config');
var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);

var register = require('./../../../accounts/register.js');
var login = require('./../../../accounts/login.js');
var deleteAccount = require('./../../../accounts/delete.js');
var setAdmin = require('./../../../accounts/setadmin.js');
var User = require('./../../../models/user.js');

var utilities = require('./utilities.js');
/*
describe('setAdminSuccessTests', function() {
  beforeEach(function(done) {
    utilities.makeCleanAccount({
      email: 'test@test',
      password: 'test123',
      username: 'test_username'
    }, function() {
      utilities.createHousehold({
        houseHoldName: 'test_household',
        houseHoldPassword: 'household123',
      }, done);
    });
  });
  afterEach(function(done) {
    utilities.deleteHousehold({
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
      utilities.setAdmin({
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

describe('setAdminFailureTests', function() {
  beforeEach(function(done) {
    utilities.makeCleanAccount({
      email: 'test@test',
      password: 'test123',
      username: 'test_username'
    }, function() {
      utilities.createHousehold({
        houseHoldName: 'test_household',
        houseHoldPassword: 'household123',
      }, done);
    });
  });
  afterEach(function(done) {
    utilities.deleteHousehold({
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
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/setadmin')
        .send(data)
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
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
          data['token'] = token;
          chai.request(config.hostname)
            .post('/setadmin')
            .send({})
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
      utilities.setAdmin({
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
      utilities.setAdmin({
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
      utilities.setAdmin({
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
      }, function(
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
*/
