var chai = require('chai');
var expect = chai.expect;
var login = require('./../../../accounts/login.js');
var User = require('../models/user');

var url = 'localhost';
var port = '8080';
var fullUrl = host + ':' + port;

describe('login', function() {
  it('should return success: false if no data is supplied', function() {
    $.post(fullUrl + '/login', { }, function(data, textStatus) {
      expect(data.success).to.equal(false);
    });
  });

  it('should return success: false if no password is supplied', function() {
    $.post(fullUrl + '/login', { email: 'c@c', username: 'c' }, function(data, textStatus) {
      expect(data.success).to.equal(false);
    });
  });
});
