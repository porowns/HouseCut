process.env.NODE_ENV = 'test';

var config = require('./../../../config');
var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);

var register = require('./../../../accounts/register.js');
var login = require('./../../../accounts/login.js');
var deleteAccount = require('./../../../accounts/delete.js');
var User = require('./../../../models/user');

describe('loginSuccessTests', function() {
  beforeEach(function(done) {
    chai.request(config.hostname)
      .post('/deleteaccount')
      .send({ email: 'test@test', password: 'test123' })
      .end(function(err, res) {
        chai.request(config.hostname)
          .post('/register')
          .send({ email: 'test@test', password: 'test123', username: 'test_username' })
          .end(function(err, res) {
            done();
          });
      });
  });
  after(function(done) {
    chai.request(config.hostname)
      .post('/deleteaccount')
      .send({ email: 'test@test', password: 'test123' })
      .end(function(err, res) {
        done();
      });
  });
  describe('Login to account', function() {
    it('login to existing account returns success and token', function(done) {
      chai.request(config.hostname)
        .post('/login')
        .send({ email: 'test@test', password: 'test123'})
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(true);
          res.res.body.should.have.property('token');
          res.res.body.token.should.have.length.of.at.least(1);
          done();
        });
    });
  });
  describe('Login to account', function() {
    it('login to existing account TWICE returns success and token both times', function(done) {
      chai.request(config.hostname)
        .post('/login')
        .send({ email: 'test@test', password: 'test123'})
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(true);
          res.res.body.should.have.property('token');
          res.res.body.token.should.have.length.of.at.least(1);
          chai.request(config.hostname)
            .post('/login')
            .send({ email: 'test@test', password: 'test123'})
            .end(function(err, res) {
              res.should.have.status(200);
              res.res.body.should.have.property('success');
              res.res.body.success.should.be.eql(true);
              res.res.body.should.have.property('token');
              res.res.body.token.should.have.length.of.at.least(1);
              done();
            });
        });
    });
  });
});

describe('loginFailureTests', function() {
  describe('Login no data', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/login')
        .send({})
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Login no password', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/login')
        .send({ email: 'test@test' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Login no email', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/login')
        .send({ password: 'test123' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Login no matching account', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/login')
        .send({ email: 'test@test', password: 'test123' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Login wrong password', function() {
    before(function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({ email: 'test@test', password: 'test123', username: 'test_username' })
        .end(function(err, res) {
          done();
        });
    });
    after(function(done) {
      chai.request(config.hostname)
        .post('/deleteaccount')
        .send({ email: 'test@test', password: 'test123' })
        .end(function(err, res) {
          done();
        });
    });
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/login')
        .send({ email: 'test@test', password: 'test456' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
});
