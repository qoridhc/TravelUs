import React, { useState, MouseEvent, useCallback } from "react";
import { LuDelete } from "react-icons/lu";
import { IoCloseOutline } from "react-icons/io5";

const PASSWORD_MAX_LENGTH = 4; // 비밀번호 입력길이 제한 설정

const shuffle = (nums: number[]) => {
  // 배열 섞는 함수
  let num_length = nums.length;
  while (num_length) {
    console.log("here");
    let random_index = Math.floor(num_length-- * Math.random());
    let temp = nums[random_index];
    nums[random_index] = nums[num_length];
    nums[num_length] = temp;
  }
  return nums;
};

const SecurityKeyboard = () => {
  let nums_init = Array.from({ length: 10 }, (v, k) => k);
  const [nums, setNums] = useState(nums_init);
  const [password, setPassword] = useState("");

  const handlePasswordChange = useCallback(
    (num: number) => {
      if (password.length === PASSWORD_MAX_LENGTH) {
        return;
      }
      setPassword(password + num.toString());
    },
    [password]
  );

  const erasePasswordOne = useCallback(
    (e: MouseEvent) => {
      setPassword(password.slice(0, password.length === 0 ? 0 : password.length - 1));
    },
    [password]
  );

  const shuffleNums = useCallback(
    (num: number) => (e: MouseEvent) => {
      // 0 ~ 9 섞어주기
      let nums_random = Array.from({ length: 10 }, (v, k) => k); // 이 배열을 변경해 입력문자 변경 가능
      setNums(shuffle(nums_random));
      handlePasswordChange(num);
    },
    [handlePasswordChange]
  );

  return (
    <div className="bg-[#EFEFF5] rounded-3xl relative flex flex-col">
      <IoCloseOutline className="text-3xl absolute top-5 left-5" />
      <div className="py-14 flex flex-col space-y-3">
        <p className="text-lg text-center font-semibold">비밀번호 입력</p>
        <input className="text-center bg-[#EFEFF5]" type="password" value={password}></input>
      </div>
      <div className="p-5 bg-white grid grid-cols-3 gap-5">
        {nums.map((n, i) => {
          const Basic_button = (
            <button className="text-2xl font-semibold" value={n} onClick={shuffleNums(n)} key={i}>
              {n}
            </button>
          );
          return i == nums.length - 1 ? (
            <>
              <button className=""></button>
              {Basic_button}
            </>
          ) : (
            Basic_button
          );
        })}
        <button className="text-2xl flex justify-center" onClick={erasePasswordOne}>
          <LuDelete />
        </button>
      </div>
    </div>
  );
};

export default SecurityKeyboard;
