/* Implemented by Chris */

process.env.NODE_ENV = 'test';

var config = require('./../../config');
var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);

var register = require('./../../accounts/register.js');
var login = require('./../../accounts/login.js');
var deleteAccount = require('./../../accounts/delete.js');
var User = require('./../../models/user.js');
var getRoommates = require('./../../roommates/getroommates.js');

var utilities = require('./../utilities.js');

describe('getRoommatesFailureTests', function() {
  describe('Get roommates no token', function() {
    it('has success false', function(done) {
      utilities.getRoommates({}, function(err, res) {
        res.should.have.status(403);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Get roommates invalid token', function() {
    it('has success false', function(done) {
      utilities.getRoommates({
        token: 'fakeToken123'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Get roommate with invalid user ID', function() {
    before(function(done) {
      utilities.makeCleanAccount({
        email: 'test@test',
        password: 'test123',
        username: 'test_username'
      }, done);
    });
    after(function(done) {
      utilities.deleteAccount({
        email: 'test@test',
        password: 'test123'
      }, done);
    });
    it('has success false', function(done) {
      utilities.loginAndGetRoommates({
        email: 'test@test',
        password: 'test123',
        userId: 'fakeUserId123'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Get roommates with no household', function() {
    before(function(done) {
      utilities.makeCleanAccount({
        email: 'test@test',
        password: 'test123',
        username: 'test_username'
      }, done);
    });
    after(function(done) {
      utilities.deleteAccount({
        email: 'test@test',
        password: 'test123'
      }, done);
    });
    it('has success false', function(done) {
      utilities.loginAndGetRoommates({
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
});

/*
describe('getRoommatesSuccessTests', function() {
  describe('Get roommates of household with single user', function() {
    before(function(done) {
      utilities.makeCleanAccount({
        email: 'test@test',
        password: 'test123',
        username: 'test_username'
      }, done);
      utilities.createHousehold({

      }, done);
    });
    after(function(done) {
      utilities.deleteHousehold({

      }, done);
      utilities.deleteAccount({
        email: 'test@test',
        password: 'test123'
      }, done);
    });
    it('has success true', function(done) {
      utilities.loginAndGetRoommates({
        email: 'test@test',
        password: 'test123'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(true);
        res.res.body.should.have.property('roommates');
        res.res.body.roommates.should.be.instanceof(Array);
        res.res.body.roommates.should.have.lengthOf(1);
        res.res.body.roommates[0].should.be.an('object');
        res.res.body.roommates[0].should.have.property('displayName');
        res.res.body.roommates[0].displayName.should.be.a('string');
        res.res.body.roommates[0].should.have.property('id');
        res.res.body.roommates[0].id.should.be.a('string');
        res.res.body.roommates[0].should.have.property('admin');
        res.res.body.roommates[0].admin.should.be.a('boolean');
        done();
      });
    });
  });
});
*/
