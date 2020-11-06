self.addEventListener('fetch', function(e) {
    console.log('[Service Worker] Fetched resource '+e.request.url);
});

self.onmessage = function(msg){
    console.log(msg.data);
    noti(msg.data)
}

function noti(body){
    new Notification("제목", {
        body: body
    });

    setTimeout(noti, 1000);
}