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
var User = require('./../../../models/user.js');

var utilities = require('./utilities.js');

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
