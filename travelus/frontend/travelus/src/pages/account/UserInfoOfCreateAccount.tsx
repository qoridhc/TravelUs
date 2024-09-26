import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useNavigate } from "react-router";
import NameInputMui from "../../components/meetingAccount/NameInputMui";
import BirthDateInputMui from "../../components/meetingAccount/BirthDateInputMui";
import GenderInputMui from "../../components/meetingAccount/GenderInputMui";
import { userApi } from "../../api/user";

const UserInfoOfCreateAccount = () => {
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [birthdate, setBirthdate] = useState("");
  const [gender, setGender] = useState("");

  const handleNext = () => {
    navigate("/meeting/create/password/nomeeting");
  };

  const getUserInfo = async () => {
    try {
      const response = await userApi.fetchUser();
      setName(response.name);
      setBirthdate(response.birth);
      setGender("여성");
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    getUserInfo();
  }, []);

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack className="text-2xl" />
        </div>

        <div className="grid gap-10">
          <div className="text-2xl font-semibold">
            <p>이예림님의 정보를</p>
            <p>확인해주세요</p>
          </div>

          <NameInputMui name={name} setName={setName} />
          <BirthDateInputMui birthdate={birthdate} setBirthdate={setBirthdate} />
          <GenderInputMui gender={gender} setGender={setGender} />
        </div>
      </div>

      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleNext()}>
        다음
      </button>
    </div>
  );
};

export default UserInfoOfCreateAccount;
