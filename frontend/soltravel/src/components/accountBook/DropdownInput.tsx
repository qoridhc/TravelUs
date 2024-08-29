import React, { useState } from "react";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select, { SelectChangeEvent } from "@mui/material/Select";

interface DropdownInputProps {
  selectedOption: string;
  onChange: (accountName: string) => void;
}

const DropdownInput: React.FC<DropdownInputProps> = ({ selectedOption, onChange }) => {
  const accountList = [
    "모히또에서 몰디브 한 잔하는 모임사람",
    "신암고 1-3반 동창회",
    "SSAFY 11기 구미 4반",
    "SSAFY 11기 구미 5반",
  ];

  const handleChange = (selected: string) => {
    onChange(selected);
  };

  return (
    <FormControl
      variant="filled"
      sx={{
        width: "100%",
        "& .MuiInputBase-root": {
          backgroundColor: "#e6e6e6",
          border: "1px solid #9E9E9E",
          borderRadius: "10px",
        },
        "& .MuiInputBase-input": {
          fontSize: "16px",
          fontWeight: 700,
        },
        "& .MuiInputLabel-root": {
          color: "#565656",
          fontSize: "16px",
        },
        "& .MuiFilledInput-underline:before, & .MuiFilledInput-underline:after": {
          display: "none",
        },
      }}>
      <InputLabel id="demo-simple-select-filled-label">모임명</InputLabel>
      <Select
        labelId="demo-simple-select-filled-label"
        id="demo-simple-select-filled"
        value={selectedOption}
        onChange={(e) => handleChange(e.target.value)}
        MenuProps={{
          PaperProps: {
            style: {
              maxHeight: 48 * 3.5,
              borderRadius: "5px",
            },
          },
        }}>
        {accountList.map((account) => (
          <MenuItem value={account}>{account}</MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default DropdownInput;
