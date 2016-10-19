/* Implemented by Chris */

process.env.NODE_ENV = 'test';

var config = require('./../../../config');
var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);

var register = require('./../../../accounts/register.js');
var deleteAccount = require('./../../../accounts/delete.js');
var User = require('./../../../models/user.js');

var utilities = require('./utilities.js');

describe('registerSuccessTests', function() {
  after(function(done) {
    utilities.deleteAccount({ email: 'test@test', password: 'test123' }, done);
  });
  describe('Create account', function() {
    it('creates account successfully returning token', function(done) {
      utilities.makeCleanAccount({
        email: 'test@test',
        password: 'test123',
        username: 'test_username'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(true);
        done();
      });
    });
  });
});

describe('registerFailureTests', function() {
  describe('Create account no data', function() {
    it('returns success false', function(done) {
      utilities.makeAccount({}, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Create account only email', function() {
    it('returns success false', function(done) {
      utilities.makeAccount({ email: 'test@test' }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Create account only username', function() {
    it('returns success false', function(done) {
      utilities.makeAccount({ username: 'test' }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Create account only password', function() {
    it('returns success false', function(done) {
      utilities.makeAccount({ password: 'test123' }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Create account no email', function() {
    it('returns success false', function(done) {
      utilities.makeAccount({
        username: 'test',
        password: 'test123'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Create account no username', function() {
    it('returns success false', function(done) {
      utilities.makeAccount({
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
  describe('Create account short password', function() {
    it('returns success false', function(done) {
      utilities.makeAccount({
        email: 'test@test',
        password: 'test',
        username: 'test_username'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Create account already taken email', function() {
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
        password: 'test123',
      }, done);
    });
    it('returns success false', function(done) {
      utilities.makeAccount({
        email: 'test@test',
        password: 'test123',
        username: 'test_username'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Create account no @ in e-mail', function() {
    it('returns success false', function(done) {
      utilities.makeAccount({
        email: 'test',
        username: 'test',
        password: 'test123'
      }, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Create account @ at end of email', function() {
    it('returns success false', function(done) {
      utilities.makeAccount({
        email: 'test@',
        username: 'test',
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
