const functions = require('firebase-functions');

var admin = require('firebase-admin');
var serviceAccount = require()

admin.initializeApp({
  credential: admin.credential.applicationDefault(),
  databaseURL: 'https://exercise.firebaseio.com'
});
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
