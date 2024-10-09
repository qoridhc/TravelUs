import React, { useState } from "react";
import { IoIosArrowBack } from "react-icons/io";
import { useLocation, useNavigate } from "react-router";
import ExchangeRateInputMui from "../../components/travelBox/ExchangeRateInputMui";
import ExchangeAmmountInput from "../../components/travelBox/ExchangeAmmountInput";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { setTravelboxInfo } from "../../redux/meetingAccountSlice";
import { IoPerson } from "react-icons/io5";

const SelectTypeOfAutoExchange: React.FC = (props) => {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useDispatch();
  const travelboxInfo = useSelector((state: RootState) => state.meetingAccount.travelboxInfo);
  const [type, setType] = useState<number | null>(null);

  const handleNext = () => {
    if (type === 1) {
      navigate("/travelbox/create/auto/exchange/rate", { state: { currency: location.state.currency } });
    } else if (type === 3) {
      navigate(`/travelbox/transaction/${location.state.accountNo}/detail`, {
        state: { currencyCode: location.state.currencyCode },
      });
    }
  };

  const handleSelectType = (type: number) => {
    setType(type);
  };

  return (
    <div className="min-h-screen h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="flex items-center">
          <IoIosArrowBack className="text-2xl" />
        </div>

        <div className="grid gap-10">
          <div className="grid gap-6">
            <div className="grid gap-3">
              <div className="flex space-x-2">
                <p className="text-[#0471E9] font-semibold">02</p>
                <p className="font-medium">외화저금통 개설</p>
              </div>

              <div className="text-2xl font-semibold">
                <p>외화저금통으로</p>
                <p>환전할 방법을 선택해주세요</p>
              </div>
            </div>

            <div>
              <div className="flex flex-col space-y-5">
                <div
                  onClick={() => handleSelectType(1)}
                  className={`w-full p-4 py-6 rounded-lg bg-[#eef4ff] flex flex-col transition-all duration-300 ease-in-out ${
                    type === 1 ? "border-2 border-[#1429A0] shadow-lg" : "border-2 border-transparent"
                  }`}>
                  <div className="flex justify-between items-center">
                    <div className="space-y-1">
                      <div className="text-zinc-700">
                        <p className="text-[#1429A0] font-semibold">사용자 설정</p>
                        <p className="font-semibold">자동환전</p>
                      </div>
                      <p className="text-sm text-zinc-600">환율, 금액을 직접 선택해 자동환전해요</p>
                    </div>
                    <img className="w-16 h-16" src="/assets/userIcon_blue.png" alt="유저" />
                  </div>
                </div>

                <div
                  onClick={() => handleSelectType(2)}
                  className={`w-full p-4 py-6 rounded-lg bg-[#eef4ff] flex flex-col transition-all duration-300 ease-in-out ${
                    type === 2 ? "border-2 border-[#1429A0] shadow-lg" : "border-2 border-transparent"
                  }`}>
                  <div className="flex justify-between items-center">
                    <div className="space-y-1">
                      <div className="text-zinc-700">
                        <p className="font-semibold">
                          <span className="text-[#1429A0]">즉시 </span>
                          자동환전
                        </p>
                      </div>
                      <div>
                        <p className="text-sm text-zinc-600">모임통장 잔액을</p>
                        <p className="text-sm text-zinc-600">현재 환율로 즉시 자동환전해요</p>
                      </div>
                    </div>
                    <img className="w-16 h-16" src="/assets/immediatelyExchange.png" alt="즉시" />
                  </div>
                </div>

                <div
                  onClick={() => handleSelectType(3)}
                  className={`w-full p-4 py-6 rounded-lg bg-[#eef4ff] flex flex-col transition-all duration-300 ease-in-out ${
                    type === 3 ? "border-2 border-[#1429A0] shadow-lg" : "border-2 border-transparent"
                  }`}>
                  <div className="flex justify-between items-center">
                    <div className="space-y-1">
                      <div className="text-zinc-700">
                        <p className="font-semibold">자동환전</p>
                        <p className="text-[#1429A0] font-semibold">안 할래요</p>
                      </div>
                      <div>
                        <p className="text-sm text-zinc-600">직접 환전을 통해서만 환전해요</p>
                      </div>
                    </div>
                    <img className="w-16 h-16" src="/assets/directlyExchange.png" alt="직접" />
                  </div>
                </div>
              </div>
            </div>
          </div>
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

export default SelectTypeOfAutoExchange;
