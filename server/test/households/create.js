// Implemented by Kaleb
process.env.NODE_ENV = 'test';

var config = require('./../../config');
var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);
var utilities = require('./../utilities.js');

var User = require('./../../models/user.js');
var Household = require('./../../models/household.js');
var createhousehold = require('./../../households/createhousehold.js');


describe('householdSuccessTests', function() {
  var token;
  before(function(done) {
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
        done();
      });
    });
  });
  after(function(done) {
    utilities.deleteHousehold({
      token: token
    }, function(err, res) {
      utilities.deleteAccount({
        email: 'test@test',
        password: 'test123'
      }, function(err, res) {
        done();
      });
    });
  });

  describe('Create a Household', function() {
    it('Creates a household', function(done) {
      utilities.createHousehold({
        houseHoldName: 'testhousehold',
        houseHoldPassword: 'testpassword',
        token: token
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(true);
        done();
      });
    });
  });

});

describe ('householdFailureTests', function () {
  var token;
  before(function(done) {
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
        done();
      });
    });
  });
  after(function(done) {
    utilities.deleteAccount({
      email: 'test@test',
      password: 'test123',
    }, done);
  });

  describe('Create a household, no token, fail', function() {
    it('Creates a household with no token', function(done) {
      utilities.createHousehold({
        houseHoldName: 'testhousehold',
        houseHoldPassword: 'testpassword'
      }, function(err, res) {
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });

  describe('Create a household, no name, fail', function() {
    it('Creates a household with no name', function(done) {
      utilities.createHousehold({
        houseHoldPassword: 'testpassword',
        token: token
      }, function(err,res) {
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });

  describe('Create a household, no password, fail', function() {
    it('Create a household with no password', function(done) {
      utilities.createHousehold({
        houseHoldName: 'testname',
        token: token
      }, function(err,res) {
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
});
