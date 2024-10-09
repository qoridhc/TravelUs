import React, { useEffect, useState } from "react";
import { format, parseISO } from "date-fns";
import { accountBookApi } from "../../api/accountBook";
import { DayHistoryDetail } from "../../types/accountBook";
import Lottie from "lottie-react";
import loadingAnimation from "../../lottie/loadingAnimation.json";

interface Props {
  accountNo: string;
  isModalOpen: boolean;
  setIsModalOpen: (isModalOpen: boolean) => void;
  selectedDate: string;
}

const AccountBookDetailModal = ({ accountNo, isModalOpen, setIsModalOpen, selectedDate }: Props) => {
  const [dayHistoryDetail, setDayHistoryDetail] = useState<DayHistoryDetail[] | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const fetchDateDetailInfo = async () => {
    console.log(accountNo, " ", selectedDate);
    if (accountNo === "" || selectedDate === "") return;

    try {
      setIsLoading(true);
      const response = await accountBookApi.fetchAccountBookDayInfo(accountNo, selectedDate, "A");
      console.log(response);
      setDayHistoryDetail(response.data);
    } catch (error) {
      console.log(error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  const handleBackdropClick = (e: React.MouseEvent) => {
    if ((e.target as Element).classList.contains("modal")) {
      handleModalToggle();
    }
  };

  useEffect(() => {
    fetchDateDetailInfo();
  }, [selectedDate]);

  // 수입과 지출 합계 계산
  // const totalIncome = dayHistoryDetail
  //   .filter((item) => item.transactionType === "G" || item.transactionType === "1")
  //   .reduce((sum, item) => sum + parseFloat(item.amount), 0);

  // const totalExpenditure = dayHistoryDetail
  //   .filter((item) => item.transactionType === "P")
  //   .reduce((sum, item) => sum + parseFloat(item.amount), 0);

  if (accountNo === "" || selectedDate === "") return <></>;

  if (isLoading || !dayHistoryDetail) {
    return (
      <div className="h-full flex flex-col justify-center items-center">
        <Lottie animationData={loadingAnimation} />
      </div>
    );
  }

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
                  <p className="text-green-600">+ 123 USD</p>
                  <p className="text-[#df4646]">- 123 USD</p>
                </div>
              </div>
              <hr className="border border-zinc-400" />
              {dayHistoryDetail.map((item, index) => (
                <div key={index} className="flex flex-col space-y-4">
                  <div className="flex justify-between items-center">
                    <div className="flex flex-col justify-center">
                      {item.store === "" ? (
                        <p className="font-bold">모임통장 이체</p>
                      ) : (
                        <p className="font-bold">{item.store}</p>
                      )}
                      <p className="text-sm">{format(parseISO(item.transactionAt), "HH:mm:ss")}</p>
                    </div>
                    <p className="text-lg font-bold flex justify-end">
                      {item.store === ""
                        ? `- ${parseFloat(String(item.paid)).toLocaleString()}`
                        : `+ ${parseFloat(String(item.paid)).toLocaleString()}`}
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
