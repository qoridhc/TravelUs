import React, { useEffect, useRef, useState } from "react";
import { TextField } from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { setBuyItems } from "../../redux/accountBookSlice";
import { accountBookApi } from "../../api/accountBook";

interface Props {
  accountNo: string;
}

const AccountBookInputModal = ({ accountNo }: Props) => {
  const dispatch = useDispatch();
  const [buyStore, setBuyStore] = useState("");
  const [paid, setPaid] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [receiptFile, setReceiptFile] = useState<File | null>(null);
  const [receiptImage, setReceiptImage] = useState<string | null>(null);

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
    const formData = new FormData();
    if (receiptFile) {
      formData.append("file", receiptFile);
      formData.append("format", fileType);
      formData.append("lang", ""); // "" : 영어
    }

    try {
      const response = await accountBookApi.fetchReceiptInfo(formData);
      console.log(response);
    } catch (error) {
      console.log("accountBookApi의 fetchReceiptInfo : ", error);
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
        console.log("Selected file string:", result);
        setReceiptImage(result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleCreateAccountBook = async () => {
    const data = {
      accountNo: accountNo,
      store: buyStore,
      paid: Number(paid),
      items: [],
    };

    try {
      const response = await accountBookApi.createAccountBook(data);
      console.log(response);
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
        </div>

        <label className="modal-backdrop" htmlFor="input-modal" onClick={handleModalToggle}>
          Close
        </label>
      </div>
    </>
  );
};

export default AccountBookInputModal;
