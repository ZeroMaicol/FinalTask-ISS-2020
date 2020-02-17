var express = require('express');

const coap          = require('../../uniboSupports/coapClientToResourceModel');  
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
  setTimeout(() => { coap.loadData() }, 500)
});

module.exports = router;
