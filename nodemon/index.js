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

app.use(express.json()); 

const syncTableToFirebase = async (tableName, firebaseNode) => {
    try {
        db.connect((err) => {
            if (err) {
                console.error('Error connecting to MySQL:', err);
                return;
            }

            db.query(`SELECT * FROM ${tableName}`, (err, results) => {
                if (err) {
                    console.error('Error fetching data from MySQL:', err);
                    db.end();
                    return;
                }

                results.forEach((row) => {
                    admin.database().ref(`${firebaseNode}/${row.id}`).set(row);
                });

                console.log(`Data from ${tableName} synced to Firebase at ${firebaseNode}`);
                db.end();
            });
        });
    } catch (err) {
        console.error('Error syncing data:', err);
    }
};

app.post('/create', async (req, res) => {
    const { tableName, data } = req.body;
    try {
        db.connect((err) => {
            if (err) {
                console.error('Error connecting to MySQL:', err);
                res.status(500).send('Error connecting to database');
                return;
            }

            db.query(`INSERT INTO ${tableName} SET ?`, data, (err, result) => {
                if (err) {
                    console.error('Error inserting data:', err);
                    res.status(500).send('Error inserting data');
                    db.end();
                    return;
                }

                syncTableToFirebase(tableName, `firebase/${tableName}`);
                res.send('Data inserted and synced');
                db.end();
            });
        });
    } catch (err) {
        res.status(500).send('Error inserting data');
    }
});

app.put('/update', async (req, res) => {
    const { tableName, data, id } = req.body;
    try {
        db.connect((err) => {
            if (err) {
                console.error('Error connecting to MySQL:', err);
                res.status(500).send('Error connecting to database');
                return;
            }

            db.query(`UPDATE ${tableName} SET ? WHERE id = ?`, [data, id], (err, result) => {
                if (err) {
                    console.error('Error updating data:', err);
                    res.status(500).send('Error updating data');
                    db.end();
                    return;
                }

                syncTableToFirebase(tableName, `firebase/${tableName}`);
                res.send('Data updated and synced');
                db.end();
            });
        });
    } catch (err) {
        res.status(500).send('Error updating data');
    }
});

app.delete('/delete', async (req, res) => {
    const { tableName, id } = req.body;
    try {
        db.connect((err) => {
            if (err) {
                console.error('Error connecting to MySQL:', err);
                res.status(500).send('Error connecting to database');
                return;
            }

            db.query(`DELETE FROM ${tableName} WHERE id = ?`, [id], (err, result) => {
                if (err) {
                    console.error('Error deleting data:', err);
                    res.status(500).send('Error deleting data');
                    db.end();
                    return;
                }

                syncTableToFirebase(tableName, `firebase/${tableName}`);
                res.send('Data deleted and synced');
                db.end();
            });
        });
    } catch (err) {
        res.status(500).send('Error deleting data');
    }
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}/`);
});
