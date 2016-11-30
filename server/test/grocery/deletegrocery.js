// Implemented by Kaleb
var config = require('./../../config');
var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);
var utilities = require('./../utilities.js');


var User = require('./../../models/user.js');
var Household = require('./../../models/household.js');


describe('deleteGrocerySuccessTests', function() {
  var token;
  before(function(done) {
    utilities.makeCleanAccount({
      email: 'grocerytest@test',
      password: 'test123',
      username: 'groceryTest'
    }, function(err,res) {
      utilities.loginToAccount({
        email: 'grocerytest@test',
        password: 'test123'
      }, function(err,res) {
        token = res.res.body.token;
        utilities.createHousehold({
          houseHoldName: 'groceryHousehold',
          houseHoldPassword: 'testpassword',
          token: token
        }, function(err, res) {
          done();
        });
      });
    });
  });

  after(function(done) {
    utilities.deleteHousehold({
      token: token
    }, function(err, res) {
      utilities.deleteAccount({
        email: 'grocerytest@test',
        password: 'test123'
      }, function(err, res) {
        done();
      });
    });
  });

  describe('Delete a grocery item', function() {
    it('Creates an Grocery Item & Deletes it', function(done) {
      utilities.createGrocery({
        token: token,
        itemName: 'eggs',
      }, function(err, res) {
        utilities.deleteGrocery({
          token: token,
          itemName: 'eggs'
        }, done);
      });
    });
  });

});
