import React, { useState } from "react";
import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select, { SelectChangeEvent } from "@mui/material/Select";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { AccountInfo } from "../../types/account";

interface DropdownInputProps {
  accountList: AccountInfo[];
  selectedOption: string;
  onChange: (accountName: string, accountIndex: number, foreignAccountNo: string) => void;
}

const DropdownInput: React.FC<DropdownInputProps> = ({ accountList, selectedOption, onChange }) => {
  const foreignAccountList = useSelector((state: RootState) => state.account.foreignAccountList);
  const handleChange = (selected: string) => {
    const selectedIndex = accountList.findIndex((account) => account.groupName === selected);
    onChange(selected, selectedIndex, foreignAccountList[selectedIndex - 1].accountNo);
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
      <InputLabel id="demo-simple-select-filled-label">모임명을 선택해주세요</InputLabel>
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
        {accountList.map((account, index) =>
          index !== 0 ? (
            <MenuItem value={account.groupName} key={index}>
              {account.groupName}
            </MenuItem>
          ) : (
            <></>
          )
        )}
      </Select>
    </FormControl>
  );
};

export default DropdownInput;
