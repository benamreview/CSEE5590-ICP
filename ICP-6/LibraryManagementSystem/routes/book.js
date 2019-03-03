var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');
var Book = require('../models/Book.js');

/* GET ALL BOOKS */
router.get('/', function(req, res, next) {
  Book.find(function (err, products) {
    console.log("in router get all");
    if (err) return next(err);
    res.json(products);
  });
});

/* GET SINGLE BOOK BY ID */
router.get('/:id', function(req, res, next) {
  Book.findById(req.params.id, function (err, post) {
    console.log("in router get single");
    if (err) return next(err);
    res.json(post);
  });
});

/* SAVE BOOK */
router.post('/', function(req, res, next) {
  Book.create(req.body, function (err, post) {
    console.log("in router save");
    if (err) return next(err);
    res.json(post);
  });
});

/* UPDATE BOOK */
router.put('/:id', function(req, res, next) {
  Book.findByIdAndUpdate(req.params.id, req.body, function (err, post) {
    console.log("in router update");
    if (err) return next(err);
    res.json(post);
  });
});
/* DELETE BOOK */
router.delete('/:id', function(req, res, next) {
  Book.deleteOne({_id: req.params.id}, function (err, post) {
    console.log("in router delete");
    if (err) return next(err);
    res.json(post);
  });
});


module.exports = router;
