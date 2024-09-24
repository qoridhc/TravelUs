import { TextField } from "@mui/material";
import React from "react";

const NameInputMui = () => {
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
      id="name"
      label="이름"
      value="이예림"
      variant="standard"
      autoComplete="off"
    />
  );
};

export default NameInputMui;
