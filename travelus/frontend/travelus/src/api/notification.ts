import api from "../lib/axios";

export const notificationApi = {
  // fcm 토큰 저장
  fetchFcmToken: (fcmToken: string) => {
    return api.post(`/notification/register`, { fcmToken });
  },
};
