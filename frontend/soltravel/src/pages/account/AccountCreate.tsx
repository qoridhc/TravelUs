import React from "react";
import { IoIosArrowBack } from "react-icons/io";

const AccountCreate = () => {
  const userName = "허동원";

  return (
    <div className="h-full flex flex-col justify-between">
      <div>
        <div className="flex justify-between">
          <div className="p-5 flex items-center space-x-2">
            <IoIosArrowBack className="text-2xl" />
            <p className="text-lg font-semibold">입출금통장 개설</p>
          </div>

          <div className="p-5 flex items-center">
            <button className="text-[#0471E9] text-sm font-semibold">취소</button>
          </div>
        </div>

        <div className="bg-[#F8F9FC]">
          <p className="p-5 text-sm font-semibold">정보입력</p>
        </div>

        <div className="p-5 grid gap-5">
          <p className="font-semibold">신규가입 정보</p>

          <div className="flex flex-col space-y-2">
            <label className="font-semibold" htmlFor="name">
              이름
            </label>
            <input
              className="p-4 text-[#565656] bg-[#F8F9FC] border rounded-lg outline-none"
              type="text"
              id="name"
              value={userName}
              disabled
            />
          </div>

          <div className="flex flex-col space-y-2">
            <label className="font-semibold" htmlFor="name">
              주민등록번호
            </label>
            <div className="grid grid-cols-[5fr_1fr_6fr] items-center">
              <input
                className="w-full p-4 text-[#565656] bg-[#F8F9FC] border rounded-lg outline-none tracking-widest"
                type="text"
                id="name"
                maxLength={6}
                placeholder="ex) 011215"
              />
              <p className="text-xl font-semibold text-center">-</p>
              <input
                className="w-full p-4 text-[#565656] bg-[#F8F9FC] border rounded-lg outline-none tracking-widest"
                type="password"
                id="name"
                maxLength={7}
                placeholder="ex) 1234567"
              />
            </div>
          </div>

          <div className="flex flex-col space-y-2">
            <label className="font-semibold" htmlFor="name">
              계좌 비밀번호 설정
            </label>
            <form autoComplete="off">
              <input
                className="w-full p-4 text-[#565656] bg-[#F8F9FC] border rounded-lg outline-none"
                type="text"
                id="name"
                placeholder="4자리 숫자 입력"
              />
            </form>
          </div>
        </div>
      </div>

      <div className="px-5 py-10">
        <button className="w-full py-3 text-white bg-[#0471E9] rounded-lg">완료</button>
      </div>
    </div>
  );
};

export default AccountCreate;
