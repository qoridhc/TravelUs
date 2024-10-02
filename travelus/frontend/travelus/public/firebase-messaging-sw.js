self.addEventListener("install", function (e) {
  console.log("fcm sw install..");
  self.skipWaiting();
});

self.addEventListener("activate", function (e) {
  console.log("fcm sw activate..");
});

self.addEventListener("push", function (e) {

  if (!e.data.json()) return;

  const resultData = e.data.json().notification;
  const notificationTitle = resultData.title;
  const notificationOptions = {
    body: resultData.body,
    icon: resultData.image,
    tag: resultData.tag,
    ...resultData,
  };
  console.log("service-worker 백그라운드 수신: ", { resultData, notificationTitle, notificationOptions });


  self.registration.showNotification(notificationTitle, notificationOptions);
});

self.addEventListener("notificationclick", function (event) {
  console.log("notification click");

  event.notification.close();
  event.waitUntil(
    (async () => {
      const allClients = await clients.matchAll({
        type: "window",
        includeUncontrolled: true // 모든 창을 포함하여 검색
      });

      // 이미 열려 있는 클라이언트(창) 중 메인 페이지가 있으면 포커스
      let clientToFocus = allClients.find(client => client.url.includes("/") && 'focus' in client);

      console.log("clientToFocus", clientToFocus);

      if (clientToFocus) {
        // 포커스를 시도하고 실패할 경우 예외 처리
        try {
          return clientToFocus.focus();
        } catch (e) {
          console.log("포커스할 수 없습니다: ", e);
        }
      }

      // 기존 창이 없으면 새 창 열기
      return clients.openWindow("/").then(windowClient => windowClient ? windowClient.focus() : null);
    })()
  );
});
