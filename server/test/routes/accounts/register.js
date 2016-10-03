var chai = require('chai');
var expect = chai.expect;
var login = require('./../../../accounts/login.js');
var User = require('../models/user');

describe('login', function() {
  it('should return success: false if no data is supplied', function() {
    $.post('localhost:8080/login', { }, function(data, textStatus) {
      expect(data.success).to.equal(false);
    });
  });
});
