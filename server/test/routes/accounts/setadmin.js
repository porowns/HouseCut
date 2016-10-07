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
    }, done);
  });
  afterEach(function(done) {
    utilities.deleteAccount({
      email: 'test@test',
      password: 'test123',
    }, done);
  });
  describe('Make user admin', function() {
    it('has success true', function(done) {
      utilities.setAdmin({
        email: 'test@test',
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
*/
