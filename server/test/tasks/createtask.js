var config = require('./../../config');
var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);
var utilities = require('./../utilities.js');


var User = require('./../../models/user.js');
var Household = require('./../../models/household.js');
var createhousehold = require('./../../households/createhousehold.js');



describe('taskSuccessTests', function() {
  var token;
  before(function(done) {
    utilities.makeCleanAccount({
      email: 'tasktest@test',
      password: 'test123',
      username: 'test_username'
    }, function(err,res) {
      utilities.loginToAccount({
        email: 'tasktest@test',
        password: 'test123'
      }, function(err,res) {
        token = res.res.body.token;
        utilities.createHousehold({
          houseHoldName: 'testhousehold',
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
        email: 'tasktest@test',
        password: 'test123'
      }, function(err, res) {
        done();
      });
    });
  });
/*
  describe('Create a Task', function() {
    it('Creates a task as a household admin', function(done) {
      utilities.createTask({
        name: 'testTask'
        assigned: ''
      });
    });
  });
  */
});
