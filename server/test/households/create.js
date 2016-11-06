// Implemented by Kaleb
process.env.NODE_ENV = 'test';

var config = require('./../../config');
var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);

var User = require('./../../models/user.js');
var Household = require('./../../models/household.js');
var createhousehold = require('./../../households/createhousehold.js');


describe('createHouseholdTests', function() {
  before(function(done) {
    utilities.makeCleanAccount({
      email: 'test@test',
      password: 'test123',
      username: 'test_username'
    }, done);
  after(function(done) {
    utilities.deleteAccount({
      email: 'test@test',
      password: 'test123'
    }, done);
  });

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
