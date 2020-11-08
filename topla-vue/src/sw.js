// Give the service worker access to Firebase Messaging.
// Note that you can only use Firebase Messaging here, other Firebase libraries
// are not available in the service worker.
importScripts('https://www.gstatic.com/firebasejs/8.0.1/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.0.1/firebase-messaging.js');

// Initialize the Firebase app in the service worker by passing in the
// messagingSenderId.
firebase.initializeApp({
    apiKey: "AIzaSyAvIewptVslnP63znkaPxQN6zKxAiadd7U",
    projectId: "topla-5f186",
    messagingSenderId: "311735027066",
    appId: "1:311735027066:web:2fb86c71d2238471a782c3",
})

// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
const messaging = firebase.messaging()

// 백그라운드 상태에서 받은 알림 처리
messaging.setBackgroundMessageHandler((payload) => {
    console.log('[firebase-messaging-sw.js] Received background message ', payload)
    // Customize notification here
    const notificationTitle = 'Background Message Title'
    const notificationOptions = {
        body: 'Background Message body.',
        icon: '/firebase-logo.png'
    }

    return self.registration.showNotification(notificationTitle,
        notificationOptions)
})
