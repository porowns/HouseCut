var express = require('express');
app = express();
var bodyParser = require('body-parser');
var morgan = require('morgan');
var mongoose = require('mongoose');
var dotenv = require('dotenv').config();

var User = require('./models/user');
var register = require('./accounts/register.js');
var login = require('./accounts/login.js');
var createhousehold = require('./households/createhousehold.js');
var getUserTaskList = require('./tasks/getusertasklist.js');

/* config */
var port = process.env.PORT || 8080;
mongoose.connect(process.env.DATABASE);
app.set('secret', process.env.SECRET);
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
app.use(morgan('dev'));

/* routes */

/* register */
app.post('/register', function(req, res) {
  register(req, res);
});

/* login */
app.post('/login', function(req, res) {
  login(req, res);
});

/* get user tasklist */
/* TODO: need to use token verify middleware */
app.get('/usertasklist', function(req, res) {
  getUserTaskList(req, res);
});

app.post('/createhousehold', function(req, res) {
  createhousehold(req, res);
});

/* start the server */
app.listen(port);
