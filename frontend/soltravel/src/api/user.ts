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

  // 모임원 초대 시 이메일 유효성 검사
  fetchEmailValidation: (email: string) => {
    return api.get(`/user/validate-email/${email}`);
  },

  // 유저 조회
  fetchUser: (userId: string) => {
    return api.get(`/user/search/${userId}`);
  },
};
