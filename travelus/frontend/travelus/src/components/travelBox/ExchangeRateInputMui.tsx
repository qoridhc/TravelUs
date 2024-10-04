import { FilledInput, FormControl, FormHelperText, Input, InputAdornment, TextField } from "@mui/material";
import React from "react";
import { BsDot } from "react-icons/bs";
import { currencyTypeList } from "../../types/exchange";

interface Props {
  currency: string;
  exchangeRateFront: number;
  setExchangeRateFront: (num: number) => void;
  exchangeRateBack: number;
  setExchangeRateBack: (num: number) => void;
}

const ExchangeRateInputMui = ({
  currency,
  exchangeRateFront,
  setExchangeRateFront,
  exchangeRateBack,
  setExchangeRateBack,
}: Props) => {
  return (
    <div className="grid gap-3">
      <div className="flex justify-between">
        <label className="text-[#565656]" htmlFor="">
          희망 환율
        </label>
        <p>{currencyTypeList.find((item) => item.value === currency)?.text}</p>
      </div>

      <div className="text-xl flex justify-between space-x-3">
        <div className="flex items-end space-x-2">
          <input
            className="w-32 text-right border-b-2 border-[#D7D7D7] placeholder:text-black outline-none"
            type="number"
            placeholder="0"
            value={exchangeRateFront}
            onChange={(e) => setExchangeRateFront(Number(e.target.value))}
          />
          <BsDot />
          <input
            className="w-16 text-right border-b-2 border-[#D7D7D7] placeholder:text-black outline-none"
            type="number"
            placeholder="0"
            value={exchangeRateBack}
            onChange={(e) => {
              const value = e.target.value;
              if (value.length <= 2) {
                setExchangeRateBack(Number(value));
              }
            }}
          />
        </div>

        <p className="text-[#1429A0] font-semibold">원 이하일 때</p>
      </div>
    </div>
  );
};

export default ExchangeRateInputMui;
