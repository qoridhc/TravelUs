import api from "../lib/axios";
import { meetingAccountInfo } from "../types/meetingAccount";

export const groupApi = {
  // 모임통장 개설
  createMeetingAccount: (data: meetingAccountInfo) => {
    return api.post(`/groups/createGroup`, data);
  },
};