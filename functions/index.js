const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// Define the Cloud Function
exports.getUserData = functions.https.onCall(async (data, context) => {
  // Check if the user is authenticated
  if (!context.auth) {
    throw new functions.https.HttpsError('unauthenticated', 'User must be authenticated.');
  }

  // Retrieve the userId from the data object
  const { userId } = data;

  try {
    // Retrieve user data from the Realtime Database
    const snapshot = await admin.database().ref(`Users/${userId}`).once('value');
    const userData = snapshot.val();

    // Extract name and email from the userData object
    const { name, email } = userData;

    // Return the name and email as a response
    return { name, email };
  } catch (error) {
    throw new functions.https.HttpsError('internal', 'Error retrieving user data.', error);
  }
});
