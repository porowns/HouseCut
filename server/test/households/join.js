// Implemented by Simon
process.env.NODE_ENV = 'test';

var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);

var utilities = require('./../utilities.js');

describe('joinHouseholdSuccessTest', function() {
  var token, token2;
  before(function(done) {
    utilities.makeCleanAccountAndLogin({
      email: 'test@test',
      password: 'test123',
      username: 'test_username'
    }, function(err, res) {
      token = res.res.body.token,
      utilities.createHousehold({
        token: token,
        houseHoldName: 'joinTest',
        houseHoldPassword: 'house123',
      }, function(err, res) {
        utilities.makeCleanAccountAndLogin({
          email: 'test2@test',
          password: 'test123',
          username: 'test2_username'
        }, function(err, res) {
          token2 = res.res.body.token
          done();
        });
      });
    });
  });
  after(function(done) {
    utilities.deleteAccount({
      email: 'test2@test',
      password: 'test123'
    }, function(err, res) {
      utilities.deleteHousehold({
        token: token
      }, function(err, res) {
        utilities.deleteAccount({
          email: 'test@test',
          password: 'test123'
        }, function() {
          done();
        });
      });
    });
  });
  describe('Join the Household', function() {
    it('should join the existing household', function(done) {
      utilities.joinHousehold({
        token: token2,
        houseHoldName: 'joinTest',
        houseHoldPassword: 'house123'
      }, function( err, res ) {
        res.should.have.status(200);
        res.res.body.should.have.property('success');
        res.res.body.success.should.be.eql(true);
        done();
      });
    });
  });
});
