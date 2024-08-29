import { useNavigate } from "react-router";
import { PiAirplaneTiltFill } from "react-icons/pi";
import { IoSchool } from "react-icons/io5";
import { useDispatch, useSelector } from "react-redux";
import { AccountInfo } from "../../types/account";

interface Props {
  index: number; 
  account: AccountInfo;
  foreignAccount: AccountInfo;
}

const MeetingAccount = ({ index, account, foreignAccount }: Props) => {
  const navigate = useNavigate();

  // 숫자를 세 자리마다 쉼표로 구분하여 표시
  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat("ko-KR").format(amount);
  };

  const formatAccountNumber = (accountNo: string) => {
    // 계좌번호를 각 4자리씩 나누고 '-'로 연결
    return accountNo.replace(/(\d{3})(\d{4})(\d{4})(\d{5})/, "$1-$2-$3-$4");
  };

  return (
    <div
      key={index}
      onClick={() => {
        navigate(`/meetingaccount/${index}`);
      }}
      className="w-full py-5 px-5 flex flex-col rounded-xl bg-white shadow-md">
      <div className="flex flex-col space-y-4">
        <div className="flex items-center space-x-1 mb-1">
          {account.iconName === "airPlane" ? (
            <div className="w-6 h-6 bg-[#638ee4] rounded-full flex justify-center items-center">
              <PiAirplaneTiltFill className="text-zinc-50" />
            </div>
          ) : (
            <div className="w-6 h-6 bg-[#61bb9d] rounded-full flex justify-center items-center">
              <IoSchool className="text-zinc-50" />
            </div>
          )}
          <p className="font-bold">{account.groupName}</p>
        </div>
        <div className="rounded-md flex justify-between">
          <div>
            <p className="text-sm font-bold">올인원 일반모임통장</p>
            <p className="text-sm text-zinc-500">{formatAccountNumber(account.accountNo)}</p>
          </div>
          <div className="flex items-center">
            <p className="text-[1.3rem] font-semibold">{formatCurrency(account.balance)}</p>
            <p className="text-[1rem]">원</p>
          </div>
        </div>
        <hr />
        <div
          onClick={(e) => {
            e.stopPropagation();
            navigate("/foreignaccount");
          }}
          className="rounded-md flex justify-between">
          <div className="flex flex-col">
            <p className="text-sm font-bold">올인원 외화모임통장</p>
            <p className="text-sm text-zinc-500">{formatAccountNumber(foreignAccount.accountNo)}</p>
          </div>
          <div className="flex items-center space-x-[0.1rem]">
            <p className="text-[1.3rem] font-semibold">{formatCurrency(foreignAccount.balance)}</p>
            <p className="text-[1rem]">{foreignAccount.currency.currencyCode}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MeetingAccount;
