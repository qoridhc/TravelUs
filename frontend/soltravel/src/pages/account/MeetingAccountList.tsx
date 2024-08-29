import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import MeetingAccount from "../../components/account/MeetingAccount";

const MeetingAccountList = () => {
  const accountList = useSelector((state: RootState) => state.account.accountList);

  return (
    <div className="w-full h-full pb-16 bg-[#EFEFF5]">
      <div className="w-full p-5 flex flex-col items-center space-y-8">
        <div className="w-full flex justify-start">
          <p className="text-xl font-bold">내 모임통장 계좌</p>
        </div>

        <div className="w-full flex flex-col space-y-3">
          {/* 모임 통장 총 개수 표시 */}
          <div className="w-full py-3 px-5 flex flex-col rounded-xl bg-[#e6e6e6]">
            <p className="font-bold">
              총 <span className="text-blue-500">{accountList.length - 1}</span>개
            </p>
          </div>
          {/* 모임 통장 있을 시 표시 */}
          {/* {accountList.length > 1 ? (
            accountList.map((account, index) => <MeetingAccount account={account} />)
          ) : (
            <div>
              <p>개설된 모임 통장이 없어요</p>
            </div>
          )} */}
        </div>
      </div>
    </div>
  );
};

export default MeetingAccountList;
