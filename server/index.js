var express = require('express');
app = express();
var bodyParser = require('body-parser');
var morgan = require('morgan');
var mongoose = require('mongoose');
var dotenv = require('dotenv').config();
var config = require('./config');

var User = require('./models/user');

var verifyToken = require('./accounts/verifytoken.js');
var register = require('./accounts/register.js');
var login = require('./accounts/login.js');
var deleteAccount = require('./accounts/delete.js');

var createhousehold = require('./households/createhousehold.js');
var getTaskList = require('./tasks/gettasklist.js');

/* config */
var port = process.env.PORT || 8080;
mongoose.connect(config.DATABASE);
app.set('secret', process.env.SECRET);
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());
if (process.env.NODE_ENV !== 'test') {
  app.use(morgan('dev'));
}

/* routes */

/* register */
app.post('/register', register);

/* login */
app.post('/login', login);

/* GET tasklist for user or household */
app.get('/tasklist', verifyToken, getTaskList);

/* delete account */
app.post('/deleteaccount', verifyToken, deleteAccount);

app.post('/createhousehold', verifyToken, createhousehold);

/* start the server */
app.listen(port);
