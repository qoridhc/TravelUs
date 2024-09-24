import React, { useState, MouseEvent, useCallback, useEffect } from "react";
import { useDispatch } from "react-redux";
import { setIsKeyboard, setAccountPassword } from "../../redux/accountSlice";
import { FaArrowLeft } from "react-icons/fa";

const PASSWORD_MAX_LENGTH = 4; // 비밀번호 입력길이 제한 설정

const shuffle = (nums: number[]) => {
  let num_length = nums.length;
  while (num_length) {
    let random_index = Math.floor(num_length-- * Math.random());
    let temp = nums[random_index];
    nums[random_index] = nums[num_length];
    nums[num_length] = temp;
  }
  return nums;
};

const SecurityNumberKeyboard = () => {
  const nums_init = Array.from({ length: 10 }, (v, k) => k);
  const [nums, setNums] = useState(nums_init);
  const [password, setPassword] = useState("");

  const dispatch = useDispatch();

  const handlePasswordChange = useCallback(
    (num: number) => {
      let nums_random = Array.from({ length: 10 }, (v, k) => k);
      setNums(shuffle(nums_random));
      setPassword((prevPassword) => {
        if (prevPassword.length === PASSWORD_MAX_LENGTH) {
          return prevPassword;
        }
        const newPassword = prevPassword + num.toString();
        dispatch(setAccountPassword(newPassword));
        return newPassword;
      });
    },
    [dispatch]
  );

  const erasePasswordOne = useCallback(
    (e: MouseEvent) => {
      setPassword((prevPassword) => {
        const newPassword = prevPassword.slice(0, -1);
        dispatch(setAccountPassword(newPassword));
        return newPassword;
      });
    },
    [dispatch]
  );

  const shuffleNums = useCallback(
    (num: number) => (e: MouseEvent) => {
      let nums_random = Array.from({ length: 10 }, (v, k) => k);
      setNums(shuffle(nums_random));
      handlePasswordChange(num);
    },
    [handlePasswordChange]
  );

  const handleKeyboardClose = () => {
    dispatch(setIsKeyboard(false));
  };

  // 비밀번호 길이가 PASSWORD_MAX_LENGTH에 도달하면 handleKeyboardClose 호출
  useEffect(() => {
    if (password.length === PASSWORD_MAX_LENGTH) {
      handleKeyboardClose();
    }
  }, [password]);

  // 키보드가 열릴 때마다 배열을 무작위로 설정
  useEffect(() => {
    setNums(shuffle(Array.from({ length: 10 }, (v, k) => k)));
  }, []);

  return (
    <div>
      <div className="p-5 bg-white grid grid-cols-3 gap-7">
        {nums.map((n, i) => {
          const Basic_button = (
            <button className="text-3xl font-medium" value={n} onClick={() => handlePasswordChange(n)} key={i}>
              {n}
            </button>
          );
          return i === nums.length - 1 ? (
            <>
              <button className=""></button>
              {Basic_button}
            </>
          ) : (
            Basic_button
          );
        })}
        <button className="text-2xl flex justify-center" onClick={erasePasswordOne}>
          <FaArrowLeft />
        </button>
      </div>
    </div>
  );
};

export default SecurityNumberKeyboard;
