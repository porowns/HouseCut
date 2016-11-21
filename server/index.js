var express = require('express');
app = express();
var bodyParser = require('body-parser');
var morgan = require('morgan');
var mongoose = require('mongoose');
var dotenv = require('dotenv').config();
var config = require('./config');

var verifyToken = require('./accounts/verifytoken.js');
var register = require('./accounts/register.js');
var login = require('./accounts/login.js');
var deleteAccount = require('./accounts/delete.js');
var setAdmin = require('./accounts/setadmin.js');

var createhousehold = require('./households/createhousehold.js');
var joinhousehold = require('./households/joinhousehold.js');
var deletehousehold = require('./households/deletehousehold.js');
var getTaskList = require('./tasks/gettasklist.js');
var getRoommates = require('./roommates/getroommates.js');
var postRoommates = require('./roommates/postroommates.js');
var createTask = require('./tasks/createtask.js');
var deleteTask = require('./tasks/deletetask.js');
var getHousehold = require('./households/gethousehold.js');


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

app.post('/setadmin', verifyToken, setAdmin);

app.post('/deleteaccount', verifyToken, deleteAccount);

app.post('/createhousehold', verifyToken, createhousehold);

app.post('/joinhousehold', verifyToken, joinhousehold);

app.post('/deletehousehold', verifyToken, deletehousehold);

app.post('/setadmin', verifyToken, setAdmin);

app.get('/household/name', verifyToken, getHousehold);

app.get('/household/tasklist', verifyToken, getTaskList);

app.get('/household/roommates', verifyToken, getRoommates);

app.post('/household/roommates', verifyToken, postRoommates);

app.post('/household/createtask', verifyToken, createTask);

app.post('/household/deletetask', verifyToken, deleteTask);

/* start the server */
app.listen(port);
