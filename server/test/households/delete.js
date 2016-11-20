// Implemented by Simon
process.env.NODE_ENV = 'test';

var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);

var utilities = require('./../utilities.js');

describe('deleteHouseholdTest', function() {
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
        },  function(err, res) {
          token = res.res.body.token,
          utilities.createHousehold({
            token: token,
            houseHoldName: 'houseHoldTest',
            houseHoldPassword: 'house123',
          }, function(err, res) {
            done();
          });
        });
      });
  });
  after(function(done) {
      utilities.deleteAccount({
        email: 'test@test',
        password: 'test123'
      }, done);
  });
  describe('Delete the Household', function() {
    it('delete the existing household', function(done) {
      utilities.deleteHousehold({
        token: token
      }, done);
    });
  });
});
