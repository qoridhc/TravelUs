import api from "../lib/axios";

export const userApi = {
  // 회원가입
  fetchSignUp: async (formData: FormData) => {
    const response = await api.post(`/user/join`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  },
  fetchNotificationSubscribe: async () => {
    const response = await api.get("/notification/subscribe");
    return response.data;
  },
};
