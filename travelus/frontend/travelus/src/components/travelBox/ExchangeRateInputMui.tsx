import { FilledInput, FormControl, FormHelperText, Input, InputAdornment, TextField } from "@mui/material";
import React from "react";
import { BsDot } from "react-icons/bs";

const ExchangeRateInputMui = () => {
  return (
    <div className="grid gap-3">
      <div className="flex justify-between">
        <label className="text-[#565656]" htmlFor="">
          희망 환율
        </label>
        <p>USD(미국/$)</p>
      </div>

      <div className="text-xl flex justify-between space-x-3">
        <div className="flex items-end space-x-2">
          <input
            className="w-32 text-right border-b-2 border-[#D7D7D7] placeholder:text-black outline-none"
            type="number"
            placeholder="0"
          />
          <BsDot />
          <input
            className="w-16 text-left border-b-2 border-[#D7D7D7] placeholder:text-black outline-none"
            type="number"
            placeholder="0"
          />
        </div>

        <p className="text-[#1429A0] font-semibold">원 이하일 때</p>
      </div>
    </div>
  );
};

export default ExchangeRateInputMui;
