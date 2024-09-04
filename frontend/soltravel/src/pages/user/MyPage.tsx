import { useNavigate } from "react-router";
import { IoIosArrowBack } from "react-icons/io";
import { useEffect, useState } from "react";
import { userApi } from "../../api/user";
import { error } from "console";

interface UserData {
  name: string;
  birth: string;
  phone: string;
  email: string;
  address: string;
}

const MyPage = () => {
  const navigate = useNavigate();
  const [userData, setUserData] = useState<UserData | null>(null);

  const doLogout = ()=>{
    localStorage.clear();
    sessionStorage.clear();
    navigate("/login");
  }

  useEffect(() => {
    const userId = localStorage.getItem("userId");
    if (userId) {
      userApi.fetchUser(userId).then((response) => {
        setUserData(response.data); // API 응답 데이터를 상태로 설정
      })
      .catch((error) => {
        console.error("Failed to fetch user data: ", error);
      });
    }
  }, []); 

  const formatPhoneNumber = (phoneNumber: string) => {
    return phoneNumber.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
  };

  return (
    <div className="w-full h-full pb-16 flex flex-col">
      <div className="bg-[#EFEFF5] p-5 space-y-3">
        <div className="flex space-x-1 items-center">
          <IoIosArrowBack
            onClick={() => {
              navigate(-1);
            }}
            className="text-2xl"
          />
          <p className="text-lg font-bold">내 정보</p>
        </div>
        <div className="flex flex-col justify-center items-center space-y-3">
          <img className="w-20" src="/assets/user/userIconSample.png" alt="유저아이콘" />
          <p className="font-semibold">{userData ? userData.name : "Loading..."}</p>
        </div>
      </div>
      <hr className="border-2 border-[#EFEFF5]" />
      <div className="p-5 flex flex-col space-y-4">
        <p className="text-md font-semibold">기본 정보</p>
        <div className="flex flex-col space-y-2">
          <div className="flex justify-between">
            <p>이름</p>
            <p>{userData ? userData.name : "Loading..."}</p>
          </div>
          <div className="flex justify-between">
            <p>생년월일</p>
            <p>{userData ? userData.birth : "Loading..."}</p>
          </div>
          <div className="flex justify-between">
            <p>휴대폰번호</p>
            <p>{userData ? formatPhoneNumber(userData.phone) : "Loading..."}</p>
          </div>
          <div className="flex justify-between">
            <p>이메일</p>
            <p>{userData ? userData.email : "Loading..."}</p>
          </div>
        </div>
      </div>
      <hr className="border-2" />
      <div className="p-5 flex flex-col space-y-4">
        <p className="text-md font-semibold">집 정보</p>
        <div className="flex flex-col space-y-2">
          <div className="flex justify-between">
            <p className="w-32">주소</p>
            <p>{userData ? userData.address : "Loading..."}</p>
          </div>
        </div>
      </div>
      <hr className="border-2" />
      <div className="px-5 py-4 flex justify-center">
        <button onClick={()=>{navigate("/userupdate")}} className="w-full p-2 border rounded-md border-zinc-300">정보 수정</button>
      </div>
      <div className="px-5 flex justify-center">
        <button onClick={doLogout} className="w-full p-2 border rounded-md border-zinc-300">로그아웃</button>
      </div>
    </div>
  );
};

export default MyPage;
