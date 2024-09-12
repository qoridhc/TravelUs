import React, { useEffect, useRef, useState } from "react";
import { TextField } from "@mui/material";
import { useDispatch } from "react-redux";
import { setBuyItems } from "../../redux/accountBookSlice";
import { accountBookApi } from "../../api/accountBook";

interface Props {
  accountNo: string;
  isModalOpen: boolean;
  setIsModalOpen: (isModalOpen: boolean) => void;
  getAccountBookInfo: () => void;
}

const AccountBookInputModal = ({ accountNo, isModalOpen, setIsModalOpen, getAccountBookInfo }: Props) => {
  const dispatch = useDispatch();
  const [buyStore, setBuyStore] = useState("");
  const [paid, setPaid] = useState("");
  const [receiptFile, setReceiptFile] = useState<File | null>(null);
  const [receiptImage, setReceiptImage] = useState<string | null>(null);
  const [loading, setLoading] = useState(false); // 로딩 상태 추가

  const fileInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (isModalOpen) {
      dispatch(setBuyItems([]));
      setBuyStore("");
      setPaid("");
    }
  }, [isModalOpen, dispatch]);

  useEffect(() => {
    if (receiptFile) {
      const fileType = receiptFile.type.replace("image/", "");
      console.log(fileType);
      handleReceiptUploadInfo(fileType);
    }
  }, [receiptFile]);

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  const handleReceiptUpload = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
    }
  };

  const handleReceiptUploadInfo = async (fileType: string) => {
    setLoading(true); // 로딩 시작
    const formData = new FormData();
    if (receiptFile) {
      formData.append("file", receiptFile);
      formData.append("format", fileType);
      formData.append("lang", ""); // "" : 영어
    }

    try {
      const response = await accountBookApi.fetchReceiptInfo(formData);
      console.log(response);
      if (response.status === 200) {
        setBuyStore(response.data.store);
        setPaid(response.data.paid.toString());
      }
    } catch (error) {
      console.log("accountBookApi의 fetchReceiptInfo : ", error);
    } finally {
      setLoading(false); // 로딩 끝
    }
  };

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      console.log("Selected file:", file);
      setReceiptFile(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        const result = reader.result as string;
        setReceiptImage(result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleCreateAccountBook = async () => {
    const now = new Date();
    const kstOffset = 9 * 60 * 60 * 1000; // 9시간을 밀리초로 변환
    const kstDate = new Date(now.getTime() + kstOffset);

    const data = {
      accountNo: accountNo,
      store: buyStore,
      paid: Number(paid),
      transactionAt: `${kstDate.toISOString()}`,
      items: [
        {
          item: "기타",
          price: Number(paid),
          quantity: 1,
        },
      ],
    };

    try {
      const response = await accountBookApi.createAccountBook(data);
      setIsModalOpen(false);
      getAccountBookInfo();
      document.getElementById("input-modal")?.click();
    } catch (error) {
      console.log("accountBookApi의 createAccountBook : ", error);
    }
  };

  return (
    <>
      <label
        className={`btn px-5 py-2 text-center text-[#EEF4FC] text-md font-semibold bg-[#5FA0F4] rounded-xl ${
          accountNo === "" ? "opacity-30" : ""
        }`}
        htmlFor={accountNo === "" ? "" : "input-modal"}>
        추가
      </label>

      <input type="checkbox" id="input-modal" className="modal-toggle" />
      <div className="modal" role="dialog">
        <div className="modal-box grid gap-5">
          {loading ? ( // 로딩 중일 때 렌더링할 화면
            <p className="text-center">로딩 중...</p>
          ) : (
            <>
              <div className="flex justify-between items-end">
                <div>
                  <p className="text-lg font-bold">결제 정보를</p>
                  <p className="text-lg font-bold">입력해주세요</p>
                </div>

                <button
                  className="w-32 h-1/2 text-sm text-[#565656] font-semibold bg-[#BBDBFF] rounded-md"
                  onClick={handleReceiptUpload}>
                  영수증으로 작성
                </button>

                <input
                  type="file"
                  ref={fileInputRef}
                  style={{ display: "none" }}
                  onChange={handleFileChange}
                  accept="image/*"
                />
              </div>

              <div className="flex flex-col space-y-2">
                <TextField
                  id="filled-basic"
                  label="사용처"
                  variant="filled"
                  value={buyStore}
                  onChange={(e) => setBuyStore(e.target.value)}
                />
                <TextField
                  id="filled-basic"
                  label="사용금액"
                  variant="filled"
                  value={paid}
                  onChange={(e) => setPaid(e.target.value)}
                />
              </div>

              <button
                className={`p-2 text-white font-semibold bg-[#0471E9] rounded-md ${
                  buyStore === "" || paid === "" ? "opacity-30" : ""
                }`}
                onClick={() => handleCreateAccountBook()}
                disabled={buyStore === "" || paid === ""}>
                등록
              </button>
            </>
          )}
        </div>

        <label className="modal-backdrop" htmlFor="input-modal" onClick={handleModalToggle}>
          Close
        </label>
      </div>
    </>
  );
};

export default AccountBookInputModal;
