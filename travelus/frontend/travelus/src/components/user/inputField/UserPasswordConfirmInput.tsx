import React from "react";
import { TextField } from "@mui/material";

interface UserPasswordConfirmInputProps {
  labelName: string;
  name: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const UserPasswordConfirmInput: React.FC<UserPasswordConfirmInputProps> = ({ labelName, name, onChange }) => {
  return (
    <TextField
      sx={{
        width: "100%",
        "& .MuiInputBase-root": {
          backgroundColor: "white",
          height: "100%",
        },
        "& .MuiInputBase-input": {
          backgroundColor: "white",
          fontSize: "18px",
          fontWeight: "bold",
          border: "1px solid #9E9E9E",
          borderRadius: "10px",
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
      id="confirmPassword"
      type="password"
      label={labelName}
      variant="filled"
      value={name}
      onChange={onChange}
      autoComplete="off"
    />
  );
};

export default UserPasswordConfirmInput;
