import React from "react";
import { TextField } from "@mui/material";

interface AccountNumberInputProps {
  labelName: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const AccountNumberInput: React.FC<AccountNumberInputProps> = ({ labelName, value, onChange }) => {
  return (
    <TextField
      sx={{
        width: "100%",
        "& .MuiInputLabel-root": {
          color: "#9E9E9E",
          fontSize: "20px",
        },
        "& .MuiInputLabel-shrink": {
          fontSize: "20px",
        },
        "& .MuiInputBase-input": {
          fontSize: "20px",
          padding: "1.2rem 0.2rem",
          boxSizing: "border-box", // padding 포함
        },
      }}
      id="accountNumber"
      label={labelName}
      variant="standard"
      value={value}
      onChange={onChange}
      autoComplete="off"
    />
  );
};

export default AccountNumberInput;