import React, { useState } from "react";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import { MeetingAccountInfo } from "../../types/account";

interface DropdownInputProps {
  meetingAccountList: MeetingAccountInfo[];
  accountNo: string;
  setAccountNo: (accountNo: string) => void;
}

const DropdownInput: React.FC<DropdownInputProps> = ({ meetingAccountList, accountNo, setAccountNo }) => {
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
      <InputLabel id="demo-simple-select-filled-label">모임통장을 선택해주세요</InputLabel>
      <Select
        labelId="demo-simple-select-filled-label"
        id="demo-simple-select-filled"
        value={accountNo}
        onChange={(e) => setAccountNo(e.target.value)}
        MenuProps={{
          PaperProps: {
            style: {
              maxHeight: 48 * 3.5,
              borderRadius: "5px",
            },
          },
        }}>
        {meetingAccountList.map((account, index) => (
          <MenuItem value={account.groupAccountNo} key={index}>
            {account.groupName} ({account.groupAccountNo})
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default DropdownInput;
