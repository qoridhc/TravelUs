const SignUp = () => {
  return (
    <div className="w-full h-full p-5 bg-[#EFEFF5]">
      <p className="text-2xl font-bold">All-In-One 뱅크 회원가입</p>
      <div className="w-full h-full flex flex-col justify-center items-center">
        <div className="w-full h-20 flex flex-col items-center justify-around rounded-xl bg-white shadow-md">
          <div className="w-[90%] h-full py-2 flex flex-col justify-around text-zinc-500">
            <p className="text-sm font-semibold">아이디</p>
            <input className="focus:outline-none text-zinc-800" type="text" />
          </div>
        </div>
        <div className="w-full h-20 flex flex-col items-center justify-around rounded-xl bg-white shadow-md">
          <div className="w-[90%] h-full py-2 flex flex-col justify-around text-zinc-500">
            <p className="text-sm font-semibold">사용자 암호</p>
            <input className="focus:outline-none text-zinc-800" type="text" />
          </div>
        </div>
        <div className="w-full h-20 flex flex-col items-center justify-around rounded-xl bg-white shadow-md">
          <div className="w-[90%] h-full py-2 flex flex-col justify-around text-zinc-500">
            <p className="text-sm font-semibold">이메일</p>
            <input className="focus:outline-none text-zinc-800" type="text" />
          </div>
        </div>
        <div className="w-full h-20 flex flex-col items-center justify-around rounded-xl bg-white shadow-md">
          <div className="w-[90%] h-full py-2 flex flex-col justify-around text-zinc-500">
            <p className="text-sm font-semibold">성명</p>
            <input className="focus:outline-none text-zinc-800" type="text" />
          </div>
        </div>
        <div className="w-full h-20 flex flex-col items-center justify-around rounded-xl bg-white shadow-md">
          <div className="w-[90%] h-full py-2 flex flex-col justify-around text-zinc-500">
            <p className="text-sm font-semibold">생년월일</p>
            <input className="focus:outline-none text-zinc-800" type="text" />
          </div>
        </div>
        <div className="w-full h-20 flex flex-col items-center justify-around rounded-xl bg-white shadow-md">
          <div className="w-[90%] h-full py-2 flex flex-col justify-around text-zinc-500">
            <p className="text-sm font-semibold">휴대폰 번호</p>
            <input className="focus:outline-none text-zinc-800" type="text" />
          </div>
        </div>
        <div className="w-full h-20 flex flex-col items-center justify-around rounded-xl bg-white shadow-md">
          <div className="w-[90%] h-full py-2 flex flex-col justify-around text-zinc-500">
            <p className="text-sm font-semibold">주소</p>
            <input className="focus:outline-none text-zinc-800" type="text" />
          </div>
        </div>
        <button className="w-full h-12 rounded-md bg-[#0046FF] font-bold text-white text-sm">회원가입</button>
      </div>
    </div>
  );
};

export default SignUp;
