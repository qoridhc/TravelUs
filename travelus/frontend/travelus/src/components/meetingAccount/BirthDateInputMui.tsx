import { TextField } from "@mui/material";
import React from "react";

const BirthDateInputMui = () => {
  return (
    <TextField
      sx={{
        width: "100%",
        "& .MuiInputLabel-root": {
          color: "#565656",
          fontSize: "20px",
        },
        "& .MuiInputLabel-shrink": {
          fontSize: "20px",
        },
        "& .MuiInputBase-input": {
          padding: "10px 0",
          fontSize: "20px",
          fontWeight: "bold",
        },
      }}
      id="birthdate"
      label="생년월일"
      value="2001년 12월 15일"
      variant="standard"
      autoComplete="off"
    />
  );
};

export default BirthDateInputMui;
