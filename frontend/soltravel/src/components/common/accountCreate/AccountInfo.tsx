import React from "react";

const AccountInfo = () => {
  return (
    <div className="px-5 grid gap-5">
      <div className="flex flex-col space-y-2">
        <label className="font-semibold" htmlFor="name">
          이름
        </label>
        <input
          className="p-4 text-[#565656] bg-[#F8F9FC] border rounded-lg outline-none"
          type="text"
          id="name"
          placeholder="이름 입력"
        />
      </div>

      <div className="flex flex-col space-y-2">
        <label className="font-semibold" htmlFor="name">
          계좌 비밀번호 설정
        </label>
        <input
          className="p-4 text-[#565656] bg-[#F8F9FC] border rounded-lg outline-none"
          type="text"
          id="name"
          placeholder="4자리 숫자 입력"
        />
      </div>
    </div>
  );
};

export default AccountInfo;
