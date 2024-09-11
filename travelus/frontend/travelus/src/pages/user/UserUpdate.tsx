import { useNavigate } from "react-router";
import UpdateUserForm from "../../components/user/UpdateUserForm";
import { IoIosArrowBack } from "react-icons/io";

const UserUpdate = () => {
  const navigate = useNavigate();

  return (
    <div className="w-full h-full p-5 bg-[#EFEFF5] flex flex-col justify-between space-y-10">
      <div className="flex space-x-1 items-center">
        <IoIosArrowBack
          onClick={() => {
            navigate(-1);
          }}
          className="text-2xl"
        />
        <p className="text-lg font-bold">내 정보 수정</p>
      </div>
      <div className="flex flex-col">
        <UpdateUserForm />
      </div>
      <button
        onClick={() => {
          navigate("/mypage");
        }}
        className="w-full h-12 rounded-md bg-[#0046FF] font-bold text-white text-sm">
        수정하기
      </button>
    </div>
  );
};

export default UserUpdate;
