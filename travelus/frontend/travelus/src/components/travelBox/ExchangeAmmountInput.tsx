import React, { useEffect, useState } from "react";

const ExchangeAmountInput = () => {
  const [transferAmount, setTransferAmount] = useState<string>(""); // 금액 상태
  const [isFullExchange, setIsFullExchange] = useState<boolean>(false); // 전액 환전 체크박스 상태

  const handleIncrement = (incrementValue: number) => {
    const currentAmount = Number(transferAmount) || 0;
    const newAmount = currentAmount + incrementValue;
    setTransferAmount(String(newAmount));
  };

  useEffect(() => {
    setTransferAmount("");
  }, [isFullExchange]);

  return (
    <div className="grid gap-3">
      <label className="text-[#565656]" htmlFor="">
        환전 금액
      </label>

      <div className="text-xl flex justify-between space-x-3">
        <div className={`w-full border-b-2 border-[#D7D7D7] flex justify-between items-end space-x-2`}>
          <input
            className={`w-full text-right outline-none ${
              isFullExchange ? "text-[#9e9e9e] placeholder:text-[#9e9e9e]" : "placeholder:text-black "
            }`}
            type="number"
            value={transferAmount}
            onChange={(e) => setTransferAmount(e.target.value)}
            placeholder="0"
            disabled={isFullExchange}
          />
          <p className={`${isFullExchange ? "text-[#9e9e9e]" : ""}`}>원</p>
        </div>
      </div>

      <div className="grid grid-cols-4 gap-2">
        <button
          onClick={() => handleIncrement(10000)}
          className={`py-1 text-sm text-[#565656] border border-zinc-300 rounded-md ${
            isFullExchange ? "text-[#9e9e9e]" : ""
          }`}
          disabled={isFullExchange}>
          +1만
        </button>
        <button
          onClick={() => handleIncrement(50000)}
          className={`py-1 text-sm text-[#565656] border border-zinc-300 rounded-md ${
            isFullExchange ? "text-[#9e9e9e]" : ""
          }`}
          disabled={isFullExchange}>
          +5만
        </button>
        <button
          onClick={() => handleIncrement(100000)}
          className={`py-1 text-sm text-[#565656] border border-zinc-300 rounded-md ${
            isFullExchange ? "text-[#9e9e9e]" : ""
          }`}
          disabled={isFullExchange}>
          +10만
        </button>
        <button
          onClick={() => handleIncrement(1000000)}
          className={`py-1 text-sm text-[#565656] border border-zinc-300 rounded-md ${
            isFullExchange ? "text-[#9e9e9e]" : ""
          }`}
          disabled={isFullExchange}>
          +100만
        </button>
      </div>

      <div className="flex justify-end">
        <label className="flex justify-between items-center space-x-2">
          <input
            type="checkbox"
            className="w-5 aspect-1 appearance-none bg-[url('./assets/check/nochecked.png')] checked:bg-[url('./assets/check/checked.png')] bg-cover rounded-full"
            checked={isFullExchange}
            onChange={() => setIsFullExchange(!isFullExchange)}
          />
          <p>전액 환전</p>
        </label>
      </div>
    </div>
  );
};

export default ExchangeAmountInput;
