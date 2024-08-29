import React, { useState } from "react";
import { styled } from "@mui/material/styles";
import Chip from "@mui/material/Chip";
import Paper from "@mui/material/Paper";
import { FormControl, OutlinedInput } from "@mui/material";

interface MemberSelectProps {
  memberList: Array<string>;
  onChange: (addMember: string) => void;
  onDelete: (deleteMember: number) => void;
}

const ListItem = styled("li")(({ theme }) => ({
  margin: theme.spacing(0.5),
}));

const MemberSelect: React.FC<MemberSelectProps> = ({ memberList, onChange, onDelete }) => {
  const [addMember, setAddMember] = useState("");
  const [hover, setHover] = useState(false);

  const handleAddMember = () => {
    onChange(addMember);
    setAddMember("");
  };

  const handleDelete = (indexToDelete: number) => () => {
    onDelete(indexToDelete);
  };

  return (
    <div className="grid gap-3">
      <div className="grid grid-cols-[3fr_1fr] gap-3">
        <form noValidate autoComplete="off">
          <FormControl sx={{ width: "100%" }}>
            <OutlinedInput
              sx={{
                "&.Mui-focused .MuiOutlinedInput-notchedOutline": {
                  border: "1px solid #9E9E9E",
                },
              }}
              value={addMember}
              placeholder={hover ? "" : "초대할 아이디 입력"}
              onChange={(e) => setAddMember(e.target.value)}
              onMouseEnter={() => setHover(true)}
              onMouseLeave={() => setHover(false)}
            />
          </FormControl>
        </form>

        <button
          className={`border border-[#cecece] rounded-md ${addMember === "" ? "opacity-50" : ""}`}
          disabled={addMember === ""}
          onClick={() => handleAddMember()}>
          초대
        </button>
      </div>

      <div className="p-3 border rounded-md">
        <Paper
          sx={{
            display: "flex",
            justifyContent: "left",
            flexWrap: "wrap",
            listStyle: "none",
            m: 0,
            boxShadow: "none",
          }}
          component="ul">
          {memberList.map((member, index) => {
            return (
              <ListItem key={index}>
                <Chip label={member} onDelete={member === "허동원" ? undefined : handleDelete(index)} />
              </ListItem>
            );
          })}
        </Paper>
        <p className="text-sm text-[#565656] text-right">총 {memberList.length}명</p>
      </div>
    </div>
  );
};

export default MemberSelect;
