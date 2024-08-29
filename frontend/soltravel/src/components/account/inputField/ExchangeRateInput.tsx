import { TextField } from "@mui/material";
import React, { useEffect, useState } from "react";
import { currencyTypeList } from "../../../types/exchange";
import { AiTwotoneExclamationCircle } from "react-icons/ai";

interface ExchangeRateInputProps {
  currencyType: string;
  exchangeRate: number;
  currentExchangeRate: number;
  onChange: (exchangeRate: number) => void;
}

const ExchangeRateInput: React.FC<ExchangeRateInputProps> = ({
  currencyType,
  exchangeRate,
  currentExchangeRate,
  onChange,
}) => {
  const [currencyTypeSymbol, setCurrencyTypeSymbol] = useState("");

  useEffect(() => {
    let temp = currencyTypeList.find((item) => item.value === currencyType);
    if (temp) {
      setCurrencyTypeSymbol(temp.text.charAt(temp.text.length - 2));
    }
  }, [currencyType]);

  return (
    <div className="grid gap-1">
      <div className="flex justify-end items-center space-x-1">
        <AiTwotoneExclamationCircle />
        <p>현재 {currencyType}의 환율</p>
        <p>:</p>
        <p className="text-[#0471E9] font-semibold">{currentExchangeRate}</p>
        <p className="text-[#565656] font-semibold">{currencyTypeSymbol}</p>
      </div>

      <TextField
        sx={{
          width: "100%",
          "& .MuiInputBase-root": {
            backgroundColor: "white",
          },
          "& .MuiInputBase-input": {
            backgroundColor: "white",
            fontSize: "20px",
            fontWeight: "bold",
            border: "1px solid #9E9E9E",
            borderRadius: "10px",
            textAlign: "right",
          },
          "& .MuiInputLabel-root": {
            color: "#9E9E9E",
            fontSize: "20px",
          },
          "& .MuiInputLabel-shrink": {
            fontSize: "16px",
          },
          "& .MuiFilledInput-underline:before, & .MuiFilledInput-underline:after": {
            display: "none",
          },
        }}
        id="filled-basic"
        label="희망 환율"
        variant="filled"
        value={exchangeRate}
        onChange={(e) => {
          onChange(Number(e.target.value));
        }}
        inputProps={{ inputMode: "decimal" }}
        autoComplete="off"
      />
    </div>
  );
};

export default ExchangeRateInput;
