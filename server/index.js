var express = require('express');
app = express();
var bodyParser = require('body-parser');
var morgan = require('morgan');
var mongoose = require('mongoose');
var dotenv = require('dotenv').config();
var config = require('./config');

var User = require('./models/user.js');

var verifyToken = require('./accounts/verifytoken.js');
var register = require('./accounts/register.js');
var login = require('./accounts/login.js');
var deleteAccount = require('./accounts/delete.js');
var setAdmin = require('./accounts/setadmin.js');

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

app.post('/register', register);

app.post('/login', login);

app.get('/tasklist', verifyToken, getTaskList);

app.post('/setadmin', verifyToken, setAdmin);

app.post('/deleteaccount', verifyToken, deleteAccount);

app.post('/createhousehold', verifyToken, createhousehold);

/* start the server */
app.listen(port);
