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

var utilities = require('./../utilities.js');

describe('deleteAccountSuccessTests', function() {
  before(function(done) {
  /*  User.count({}, function(err, c) {
      numAccounts = c;*/
      utilities.makeCleanAccount({
        email: 'test@test',
        password: 'test123',
        username: 'test_username'
      }, done);
    /*});*/
  });
  describe('Delete existing account', function() {
    it('has success true', function(done) {
      utilities.deleteAccount({
        email: 'test@test',
        password: 'test123'
      }, function(err, res) {
      /*  User.count({}, function(err, c) {
          c.should.be.eql(numAccounts-1);*/
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(true);
          done();
        /*});*/
      });
    });
  });
});

describe('deleteAccountFailureTests', function() {
  describe('Delete non-existing account', function() {
    it('has success false', function(done) {
      utilities.deleteAccount({
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
  describe('Delete account no data', function() {
    it('has success false', function(done) {
      utilities.deleteAccount({}, function(err, res) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(false);
        done();
      });
    });
  });
  describe('Delete account wrong password', function() {
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
      utilities.deleteAccount({
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
  describe('Delete account no token', function() {
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
      chai.request(config.hostname)
        .post('/deleteaccount')
        .send({ email: 'test@test', password: 'test123' })
        .end(function(err, res) {
          res.should.have.status(403);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
});
