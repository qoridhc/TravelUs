import React from "react";
import { format, parseISO } from "date-fns";
import { useSelector } from "react-redux";
import { RootState } from "../../redux/store";

interface Props {
  accountNo: string;
  isModalOpen: boolean;
  setIsModalOpen: (isModalOpen: boolean) => void;
  selectedDate: Date | null;
}

const AccountBookDetailModal = ({ accountNo, isModalOpen, setIsModalOpen, selectedDate }: Props) => {
  const dayHistoryDetail = useSelector((state: RootState) => state.accountBook.dayHistoryDetail);

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  const handleBackdropClick = (e: React.MouseEvent) => {
    if ((e.target as Element).classList.contains("modal")) {
      handleModalToggle();
    }
  };

  // 수입과 지출 합계 계산
  const totalIncome = dayHistoryDetail
    .filter((item) => item.transactionType === "G" || item.transactionType === "1")
    .reduce((sum, item) => sum + parseFloat(item.amount), 0);

  const totalExpenditure = dayHistoryDetail
    .filter((item) => item.transactionType === "P")
    .reduce((sum, item) => sum + parseFloat(item.amount), 0);

  console.log(dayHistoryDetail);
  return (
    <>
      <input type="checkbox" id="detail-modal" className="modal-toggle" checked={isModalOpen} readOnly />
      <div className="modal" role="dialog" onClick={handleBackdropClick}>
        <div className="modal-box grid gap-2" onClick={(e) => e.stopPropagation()}>
          <h3 className="font-bold text-lg">
            {selectedDate ? format(selectedDate, "yyyy-MM-dd") : "선택된 날짜가 없습니다."}
          </h3>
          {dayHistoryDetail.length > 0 ? (
            <>
              <div className="flex justify-between">
                <p className="text-sm text-zinc-700">총 {dayHistoryDetail.length}건</p>
                <div className="flex space-x-2">
                  <p className="text-green-600">+ {totalIncome.toLocaleString()} USD</p>
                  <p className="text-[#df4646]">- {totalExpenditure.toLocaleString()} USD</p>
                </div>
              </div>
              <hr className="border border-zinc-400" />
              {dayHistoryDetail.map((item, index) => (
                <div key={index} className="flex flex-col space-y-4">
                  <div className="flex justify-between items-center">
                    <div className="flex flex-col justify-center">
                      {item.transactionType === "1" ? (
                        <p className="font-bold">모임통장 이체</p>
                      ) : (
                        <p className="font-bold">{item.store}</p>
                      )}
                      <p className="text-sm">{format(parseISO(item.transactionAt), "HH:mm:ss")}</p>
                    </div>
                    <p className="text-lg font-bold flex justify-end">
                      {item.transactionType === "P"
                        ? `- ${parseFloat(item.amount).toLocaleString()}`
                        : `+ ${parseFloat(item.amount).toLocaleString()}`}{" "}
                      USD
                    </p>
                  </div>
                </div>
              ))}
            </>
          ) : (
            <>
              <div className="flex justify-between">
                <p className="text-sm text-zinc-700">총 {dayHistoryDetail.length}건</p>
                <div className="flex space-x-2">
                  <p className="text-green-600">+ 0 USD</p>
                  <p className="text-[#df4646]">- 0 USD</p>
                </div>
              </div>
              <hr className="border border-zinc-400" />
              <p className="mt-5 text-center text-zinc-500">내역이 없습니다.</p>
            </>
          )}
        </div>
      </div>
    </>
  );
};

export default AccountBookDetailModal;
