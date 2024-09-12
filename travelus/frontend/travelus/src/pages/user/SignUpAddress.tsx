import React from "react";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { RootState } from "../../redux/store";
import { editSignUpUserInformation } from "../../redux/userInformationSlice";
import { IoIosArrowBack } from "react-icons/io";
import { userApi } from "../../api/user";
import IdInput from "../../components/user/inputField/IdInput";
import UserPasswordInput from "../../components/user/inputField/UserPasswordInput";
import UserPasswordConfirmInput from "../../components/user/inputField/UserPasswordConfirmInput";
import NameInput from "../../components/user/inputField/NameInput";
import BirthdayInput from "../../components/user/inputField/BirthdayInput";
import PhoneInput from "../../components/user/inputField/PhoneInput";
import VerificationCodeInput from "../../components/user/inputField/VerificationCodeInput";
import { set } from "date-fns";

interface SignUpAddressProps {
  // Define your props here
}

interface InputState {
  id: string;
  password: string;
  confirmPassword: string;
  name: string;
  birthday: string;
  phone: string;
  verificationCode: string;
}

const SignUpAddress = () => {
  return (
    <div className="w-full min-h-screen p-5 bg-[#F3F4F6] flex flex-col justify-between">
      <div className="flex flex-col space-y-10">
        <div className="mt-16 text-2xl font-semibold">
          <p>주소를 입력해주세요</p>
        </div>

        <div className="transition-transform duration-300 ease-in-out translate-y-[3px]">
          <p>폼 자리임</p>
        </div>

      </div>
      <button className={`w-full mb-5 py-3 text-white rounded-lg bg-[#1429A0]`}>다음</button>
    </div>
  );
};

export default SignUpAddress;
