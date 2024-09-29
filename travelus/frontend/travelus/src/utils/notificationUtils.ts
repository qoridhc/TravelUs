import { getToken, onMessage } from "firebase/messaging";
import { messaging } from "../firebase";
import { notificationApi } from "../api/notification";

// FCM 서비스 워커 등록
export const registerServiceWorker = async () => {
  try {
    const registration = await navigator.serviceWorker.register("firebase-messaging-sw.js");
    console.log("Service Worker 등록 성공:", registration);
  } catch (error) {
    console.error("Service Worker 등록 실패:", error);
  }
};

// 알림 권한 요청
export const requestPermission = async () => {
  console.log("권한 요청 중...");
  const permission = await Notification.requestPermission();
  const granted = permission === "granted";

  return granted;
};

// 토큰 발급 및 저장
export const fetchAndStoreFcmToken = async () => {
  try {
    const currentToken = await getToken(messaging, {
      vapidKey: process.env.REACT_APP_VAPID_KEY,
    });

    if (!currentToken) {
      console.warn("토큰을 생성할 수 없습니다. 권한을 요청해보세요.");
      return null;
    }

    // console.log("FCM Token:", currentToken);

    // FCM 토큰을 서버에 저장
    await notificationApi.fetchFcmToken(currentToken);

    return currentToken;
  } catch (err) {
    console.error("FCM 토큰 발급 및 저장 중 오류 발생: ", err);
    throw err;
  }
};

// FCM 초기 설정
export const initializeFcmAndRegisterToken = async () => {
  try {
    const permissionGranted = await requestPermission();
    if (permissionGranted) {
      await fetchAndStoreFcmToken();

      console.log("토큰 저장 성공");


    } else {
      console.warn("알림 권한이 허용되지 않았습니다.");
    }
  } catch (error) {
    console.error("FCM 설정 중 오류 발생:", error);
  }
};


