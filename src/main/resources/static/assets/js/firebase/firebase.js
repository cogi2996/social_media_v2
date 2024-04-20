import { initializeApp } from "https://www.gstatic.com/firebasejs/10.9.0/firebase-app.js";

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyCiNvUmhSrB3RphdgINyUxeqPg742ysWVw",
  authDomain: "socialmedia-f7504.firebaseapp.com",
  projectId: "socialmedia-f7504",
  storageBucket: "socialmedia-f7504.appspot.com",
  messagingSenderId: "646554981030",
  appId: "1:646554981030:web:de7aeacc3e1edeb7519a67",
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
// console.log(app);

export { app };
