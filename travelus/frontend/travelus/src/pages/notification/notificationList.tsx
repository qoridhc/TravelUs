import React from "react";
import { useNavigate } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { MdPayments } from "react-icons/md";

interface NotificationListProps {
  // Define props here
}

const NotificationList: React.FC<NotificationListProps> = (props) => {
  const navigate = useNavigate();

  const notificationList = [
    {
      id: 1,
      notificationType: "D",
      title: "새로운 알림이 도착했습니다!",
      content: "새로운 알림이 도착했습니다. 확인해보세요.",
      date: "2021-09-01",
    },
    {
      id: 2,
      notificationType: "CW",
      title: "새로운 알림이 도착했습니다!",
      content: "새로운 알림이 도착했습니다. 확인해보세요.",
      date: "2021-09-02",
    },
    {
      id: 3,
      notificationType: "CW",
      title: "새로운 알림이 도착했습니다!",
      content: "새로운 알림이 도착했습니다. 확인해보세요.",
      date: "2021-09-03",
    },
  ];

  return (
    <div className="min-h-screen h-full p-6 pb-8">
      <div className="flex flex-col space-y-10">
        <IoIosArrowBack
          onClick={() => {
            navigate("/");
          }}
          className="text-2xl"
        />

        <div className="w-full space-y-9">
          <p className="text-3xl font-bold">알림</p>
          {notificationList.map((notification) => (
            <div key={notification.id} className="space-y-1">
              <div className="flex items-start space-x-2">
                <MdPayments className="text-[#26a85d]" />
                <div className="w-full flex flex-col space-y-[0.4rem]">
                  <div className="flex justify-between items-center">
                    <p className="text-sm text-zinc-400">{notification.title}</p>
                    <p className="text-sm text-zinc-400">{notification.date}</p>
                  </div>
                  <p className=" text-gray-600">{notification.content}</p>
                </div>
              </div>
            </div>
          ))}

          <div className="pt-3 grid grid-cols-[20%_60%_20%] items-center">
            <div className="w-full h-[0.1rem] bg-zinc-200"></div>
            <p className="w-full text-center text-sm text-zinc-400">7일전 알림까지 확인할 수 있어요</p>
            <div className="w-full h-[0.1rem] bg-zinc-200"></div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default NotificationList;
