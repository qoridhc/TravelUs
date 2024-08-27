import { useNavigate } from "react-router";
import { IoIosArrowBack } from "react-icons/io";

const MyPage = () => {
  const navigate = useNavigate();

  return (
    <div className="w-full h-full pb-16 flex flex-col">
      <div className="bg-[#EFEFF5] p-5 space-y-3">
        <div className="flex space-x-1 items-center">
          <IoIosArrowBack
            onClick={() => {
              navigate(-1);
            }}
            className="text-2xl"
          />
          <p className="text-lg font-bold">내 정보</p>
        </div>
        <div className="flex flex-col justify-center items-center space-y-3">
          <img className="w-20" src="/assets/user/userIconSample.png" alt="유저아이콘" />
          <p className="font-semibold">허동원</p>
        </div>
      </div>
      <hr className="border-2 border-[#EFEFF5]" />
      <div className="p-5 flex flex-col space-y-4">
        <p className="text-md font-semibold">기본 정보</p>
        <div className="flex flex-col space-y-2">
          <div className="flex justify-between">
            <p>이름</p>
            <p>허동원</p>
          </div>
          <div className="flex justify-between">
            <p>생년월일</p>
            <p>99.00.00</p>
          </div>
          <div className="flex justify-between">
            <p>휴대폰번호</p>
            <p>010-****-0000</p>
          </div>
          <div className="flex justify-between">
            <p>이메일</p>
            <p>shinhan@gmail.com</p>
          </div>
        </div>
      </div>
      <hr className="border-2" />
      <div className="p-5 flex flex-col space-y-4">
        <p className="text-md font-semibold">집 정보</p>
        <div className="flex flex-col space-y-2">
          <div className="flex justify-between">
            <p className="w-32">주소</p>
            <p>42112 대구광역시 달서구 용산서로 102, 1130동 807호 (용산1동, 도레미아파트)</p>
          </div>
        </div>
      </div>
      <hr className="border-2" />
      <div className="p-5 flex justify-center">
        <button onClick={()=>{navigate("/userupdate")}} className="w-full p-3 border rounded-md border-zinc-300">정보 수정</button>
      </div>
    </div>
  );
};

export default MyPage;
