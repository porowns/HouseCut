process.env.NODE_ENV = 'test';

var config = require('./../../../config');
var chai = require('chai');
var chaiHttp = require('chai-http');
var should = chai.should();
chai.use(chaiHttp);

var register = require('./../../../accounts/register.js');
var deleteAccount = require('./../../../accounts/delete.js');
var User = require('./../../../models/user');

describe('registerSuccessTests', function() {
  beforeEach(function(done) {
    /* if there's a test@test account, delete it */
    chai.request(config.hostname)
      .post('/login')
      .send({ email: 'test@test', password: 'test123' })
      .end(function(err, res) {
        if (res.res.body.success) {
          var token = res.res.body.token;
          chai.request(config.hostname)
            .post('/deleteaccount')
            .send({ email: 'test@test', password: 'test123', token: token })
            .end(function(err, res) {
              done();
            });
        }
        else {
          done();
        }
      });
  });
  after(function(done) {
    /* delete the account we made */
    chai.request(config.hostname)
      .post('/login')
      .send({ email: 'test@test', password: 'test123' })
      .end(function(err, res) {
        if (res.res.body.success) {
          var token = res.res.body.token;
          chai.request(config.hostname)
            .post('/deleteaccount')
            .send({ email: 'test@test', password: 'test123', token: token })
            .end(function(err, res) {
              done();
            });
        }
        else {
          done();
        }
      });
  });
  describe('Create account', function() {
    it('creates account successfully returning token', function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({ email: 'test@test', password: 'test123', username: 'test_username' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(true);
          done();
        });
    });
  });
});

describe('registerFailureTests', function() {
  describe('Create account no data', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({})
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Create account only email', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({ email: 'test@test' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Create account only username', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({ username: 'test' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Create account only password', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({ password: 'test123' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Create account no email', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({ username: 'test', password: 'test123' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Create account no username', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({ email: 'test@test', password: 'test123' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Create account short password', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({ email: 'test@test', username: 'test', password: 'test' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Create account already taken email', function() {
    before(function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({ email: 'test@test', username: 'test_username', password: 'test123' })
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
        .post('/register')
        .send({ email: 'test@test', username: 'test_username', password: 'test123' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Create account no @ in e-mail', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({ email: 'test', username: 'test_username', password: 'test123' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
  describe('Create account @ at end of email', function() {
    it('returns success false', function(done) {
      chai.request(config.hostname)
        .post('/register')
        .send({ email: 'test@', username: 'test_username', password: 'test123' })
        .end(function(err, res) {
          res.should.have.status(200);
          res.res.body.should.have.property('success');
          res.res.body.success.should.be.eql(false);
          done();
        });
    });
  });
});
