import { useNavigate } from "react-router";

const Login = () => {
  const navigate = useNavigate();

  return (
    <div className="w-full h-full bg-[#EFEFF5]">
      <div className="w-full h-full flex flex-col justify-center items-center">
        <div className="w-full h-[50%] flex flex-col justify-between items-center">

          <div className="w-[90%] h-[78%] flex flex-col items-center justify-around rounded-xl bg-white shadow-sm">
            <p className="text-2xl font-bold">All-In-One 로그인</p>
            <div className="w-[83%] h-[50%] flex flex-col justify-around text-zinc-500">
              <p className="text-sm font-semibold">아이디</p>
              <input className="focus:outline-none text-zinc-800" type="text" />
              <hr className="bg-zinc-800" />
              <p className="text-sm font-semibold">사용자 암호</p>
              <input className="focus:outline-none text-zinc-800" type="password" />
              <hr className="bg-zinc-800" />
            </div>
            <button className="w-[92%] h-12 rounded-md bg-[#0046FF] font-bold text-white text-sm">로그인</button>
          </div>

          <div className="w-[90%] h-[18%] flex items-center justify-around rounded-xl bg-white shadow-sm">
            <div className="flex w-[58%] justify-around items-center">
              <p className="text-sm font-bold">계정이 없으신가요?</p>
              <button
                onClick={() => {
                  navigate("/signup");
                }}
                className="text-[#0046FF] font-bold">
                가입하기
              </button>
            </div>
          </div>

        </div>
      </div>
    </div>
  );
};

export default Login;
