import { FormControl, InputLabel, MenuItem } from "@mui/material";
import React from "react";
import Select from "@mui/material/Select";

interface TypeInputProps {
  meetingType: string;
  onChange: (meetingType: string) => void;
}

const MeetingTypeSelect: React.FC<TypeInputProps> = ({ meetingType, onChange }) => {
  return (
    <FormControl
      variant="filled"
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
        },
        "& .MuiInputLabel-root": {
          fontSize: "20px",
        },
        "& .MuiInputLabel-shrink": {
          fontSize: "16px",
        },
        "& .MuiFilledInput-underline:before, & .MuiFilledInput-underline:after": {
          display: "none",
        },
        "& .MuiSelect-select:focus": {
          backgroundColor: "white",
          borderRadius: "10px",
        },
      }}>
      <InputLabel id="demo-simple-select-filled-label">모임종류</InputLabel>
      <Select
        labelId="demo-simple-select-filled-label"
        id="demo-simple-select-filled"
        value={meetingType}
        onChange={(e) => onChange(e.target.value)}>
        <MenuItem value="none">선택안함</MenuItem>
        <MenuItem value="airplane">여행</MenuItem>
        <MenuItem value="school">학교</MenuItem>
      </Select>
    </FormControl>
  );
};

export default MeetingTypeSelect;
