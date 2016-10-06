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
var User = require('./../../../models/user');

var utilities = require('./utilities.js');

describe('loginSuccessTests', function() {
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
  describe('Login to account', function() {
    it('login to existing account returns success and token', function(done) {
      utilities.loginToAccount({
        email: 'test@test',
        password: 'test123'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(true);
        res.res.body.should.have.property('token');
        res.res.body.token.should.have.length.of.at.least(1);
        done();
      });
    });
  });
  describe('Login to account', function() {
    it('login to existing account TWICE returns success and token both times', function(done) {
      utilities.loginToAccount({
        email: 'test@test',
        password: 'test123'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(true);
        res.res.body.should.have.property('token');
        res.res.body.token.should.have.length.of.at.least(1);
        utilities.loginToAccount({
          email: 'test@test',
          password: 'test123'
        }, function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(true);
          res.res.body.should.have.property('token');
          res.res.body.token.should.have.length.of.at.least(1);
          done();
        });
      });
    });
  });
});

describe('loginFailureTests', function() {
  describe('Login no data', function() {
    it('returns success false', function(done) {
      utilities.loginToAccount({}, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Login no password', function() {
    it('returns success false', function(done) {
      utilities.loginToAccount({ email: 'test@test' }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Login no email', function() {
    it('returns success false', function(done) {
      utilities.loginToAccount({ password: 'test123' }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Login no matching account', function() {
    it('returns success false', function(done) {
      utilities.loginToAccount({
        email: 'test2@test',
        password: 'test123'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Login wrong password', function() {
    before(function(done) {
      utilities.makeAccount({
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
    it('returns success false', function(done) {
      utilities.loginToAccount({
        email: 'test@test',
        password: 'test456'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
});
