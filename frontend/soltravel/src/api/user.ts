import api from "../lib/axios";

export const userApi = {
  // 회원가입
  fetchSignUp: (formData: FormData) => {
    return api.post(`/user/join`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  // 로그인
  fetchLogin: (email: string, password: string) => {
    return api.post(`/auth/login`, { email, password });
  },

  // fetchNotificationSubscribe: async () => {
  //   const response = await api.get("/notification/subscribe");
  //   return response.data;
  // },
};
