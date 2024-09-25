import React, { useEffect, useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useLocation, useNavigate } from "react-router";

interface Member {
  name: string;
  amount: number;
}

const SettlementInfo = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const totalAmount = 160834;
  const [members, setMembers] = useState<Member[]>([
    { name: "박민규", amount: 53612 },
    { name: "박예진", amount: 53611 },
    { name: "이예림", amount: 53611 },
  ]);

  const handleSettlement = () => {
    navigate("/balancesettlementcompleted");
  };

  const handleMembers = () => {
    navigate("/editmembers/balance", { state: { selectedMemberList: members } });
  };

  useEffect(() => {
    if (location.state?.members) {
      const memberList = location.state.members;
      const memberCount = memberList.length;

      const amountPerMember = Math.floor(totalAmount / memberCount);
      const remainder = totalAmount % memberCount;

      const updatedMembers = memberList.map((member: Member, index: number) => ({
        name: member.name,
        amount: index === 0 ? amountPerMember + remainder : amountPerMember,
      }));

      setMembers(updatedMembers);
    }
  }, [location.state]);

  return (
    <div className="h-full py-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="px-5 grid grid-cols-3">
          <div className="flex items-center">
            <IoIosArrowBack className="text-2xl" />
          </div>
          <p className="text-lg text-center">정산하기</p>
        </div>

        <div className="grid gap-8">
          <div className="px-5 text-2xl font-semibold tracking-wide">
            <div className="flex">
              <p className="text-[#1429A0]">119.67 $</p>
              <p>를</p>
            </div>
            <p>일반모임통장에 넣었어요</p>
          </div>

          <div className="w-full h-5 bg-[#F6F6F8]"></div>

          <div className="px-5 text-2xl font-semibold tracking-wide">
            <div className="flex">
              <p>총&nbsp;</p>
              <p className="text-[#1429A0]">160,834원</p>
              <p>을</p>
            </div>

            <div className="flex">
              <p className="text-[#1429A0]">{members.length}명</p>
              <p>이 나눕니다</p>
            </div>
          </div>
        </div>

        <div className="px-5 grid gap-5">
          <p className="text-[#565656] underline underline-offset-4 decoration-1" onClick={() => handleMembers()}>
            친구편집
          </p>
          {members.map((item, index) => (
            <div className="flex justify-between items-center">
              <div className="flex items-center space-x-3" key={index}>
                <img className="w-10 aspect-1" src="/assets/user/userIconSample.png" alt="" />
                <p>{item.name}</p>
              </div>

              <p>{item.amount}원</p>
            </div>
          ))}
        </div>
      </div>

      <div className="px-5">
        <button
          className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
          onClick={() => handleSettlement()}>
          정산하기
        </button>
      </div>
    </div>
  );
};

export default SettlementInfo;
