var env = process.env.NODE_ENV || 'development';
var cfg = require('./config.' + env + '.js');

module.exports = cfg;
