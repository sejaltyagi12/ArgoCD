var express = require('express');
var app=express();
var bodyParser=require('body-parser');

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

var resourcesPath= '/home/wittybrains/policiesAndProcedure';

console.log('__dirname = ', __dirname);
app.use(express.static(__dirname +  '/src/'));
app.use(express.static(resourcesPath));


const port = process.env.PORT || 9000;
app.listen(port, () => { 
 	console.log('Node server listening on ', port);
});


