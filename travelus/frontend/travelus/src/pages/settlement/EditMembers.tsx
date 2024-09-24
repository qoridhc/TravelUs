import React, { useEffect, useState } from "react";
import { IoIosArrowBack, IoIosCloseCircle } from "react-icons/io";
import { useLocation, useNavigate } from "react-router";

const EditMembers = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const allMemberList = [{ name: "박민규" }, { name: "박예진" }, { name: "이예림" }, { name: "허동원" }];
  const [selectedMemberList, setSelectedMemberList] = useState([
    { name: "박민규" },
    { name: "박예진" },
    { name: "이예림" },
  ]);

  const handleSelected = () => {
    navigate("/settlement", { state: { members: selectedMemberList } });
  };

  const toggleMemberSelection = (name: string) => {
    if (selectedMemberList.some((member) => member.name === name)) {
      setSelectedMemberList(selectedMemberList.filter((member) => member.name !== name));
    } else {
      setSelectedMemberList([...selectedMemberList, { name }]);
    }
  };

  const removeFromSelected = (name: string) => {
    setSelectedMemberList(selectedMemberList.filter((member) => member.name !== name));
  };

  useEffect(() => {
    if (location.state?.selectedMemberList) {
      setSelectedMemberList(location.state.selectedMemberList);
    }
  }, []);

  return (
    <div className="h-full p-5 pb-8 flex flex-col justify-between">
      <div className="grid gap-14">
        <div className="grid grid-cols-3">
          <div className="flex items-center">
            <IoIosArrowBack className="text-2xl" />
          </div>
          <p className="text-lg text-center">모임원 선택</p>
        </div>

        {/* 선택된 멤버 리스트 */}
        <div className="flex space-x-5">
          {selectedMemberList.map((item, index) => (
            <div className="relative" key={index}>
              <div className="flex flex-col justify-center space-y-2">
                <img className="w-10 aspect-1" src="/assets/user/userIconSample.png" alt="" />
                <p>{item.name}</p>
              </div>
              <button onClick={() => removeFromSelected(item.name)}>
                <IoIosCloseCircle className="opacity-50 absolute top-0 right-0" />
              </button>
            </div>
          ))}
        </div>

        {/* 전체 멤버 리스트 */}
        <div className="grid gap-5">
          {allMemberList.map((item, index) => (
            <label className="flex justify-between items-center" key={index}>
              <div className="flex items-center space-x-3">
                <img className="w-10 aspect-1" src="/assets/user/userIconSample.png" alt="" />
                <p>{item.name}</p>
              </div>
              <input
                type="checkbox"
                className="w-6 aspect-1 appearance-none bg-[url('./assets/check/nochecked.png')] checked:bg-[url('./assets/check/checked.png')] bg-cover rounded-full"
                checked={selectedMemberList.some((member) => member.name === item.name)}
                onChange={() => toggleMemberSelection(item.name)}
              />
            </label>
          ))}
        </div>
      </div>

      {/* 선택된 멤버 수 동기화 */}
      <button
        className="w-full h-14 text-lg rounded-xl tracking-wide text-white bg-[#1429A0]"
        onClick={() => handleSelected()}>
        {selectedMemberList.length}명 선택
      </button>
    </div>
  );
};

export default EditMembers;
