const express = require('express');
const mysql = require('mysql');
const admin = require('firebase-admin');
const serviceAccount = require('./serviceAccountKey.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://jobinterview-79656-default-rtdb.asia-southeast1.firebasedatabase.app/'
});

const app = express();
const port = 3000;

const db = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: '123456',
  database: 'jlia'
});

db.connect((err) => {
  if (err) throw err;
  console.log('Connected to MySQL');
});

const syncDataToFirebase = () => {
 //add sql query cần được đẩy lên realtime
};

app.get('/sync', (req, res) => {
  syncDataToFirebase();
  res.send('Data sync initiated');
});

app.listen(port, () => {
  console.log(`Server running at http://localhost:${port}/`);
});
