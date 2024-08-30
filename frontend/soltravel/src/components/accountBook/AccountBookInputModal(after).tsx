import React, { useEffect, useRef, useState } from "react";
import { AxiosError } from "axios";
import { TextField } from "@mui/material";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../../redux/store";
import { setBuyItems } from "../../redux/accountBookSlice";
import { accountBookApi } from "../../api/accountBook";
import { AxiosErrorResponseData } from "../../types/axiosError";

interface Props {
  accountNo: string;
}

const AccountBookInputModal = ({ accountNo }: Props) => {
  const dispatch = useDispatch();
  const buyItems = useSelector((state: RootState) => state.accountBook.buyItems);
  const [buyStore, setBuyStore] = useState("");
  const [paid, setPaid] = useState("");
  const [itemName, setItemName] = useState("");
  const [price, setPrice] = useState("");
  const [quantity, setQuantity] = useState(0);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [receiptFile, setReceiptFile] = useState<File | null>(null);
  const [receiptImage, setReceiptImage] = useState<string | null>(null);

  const fileInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    if (isModalOpen) {
      dispatch(setBuyItems([]));
      setBuyStore("");
      setPaid("");
      setItemName("");
      setPrice("");
      setQuantity(0);
    }
  }, [isModalOpen, dispatch]);

  useEffect(() => {
    const totalPaid = buyItems.reduce((sum, item) => sum + item.price * item.quantity, 0);
    setPaid(totalPaid.toString());
  }, [buyItems]);

  const handleAddItem = () => {
    let data = { item: itemName, price: Number(price), quantity: quantity };
    dispatch(setBuyItems([...buyItems, data]));
    setItemName("");
    setPrice("");
    setQuantity(0);
  };

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  const handleReceiptUpload = () => {
    if (fileInputRef.current) {
      fileInputRef.current.click();
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
      items: buyItems,
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
            {receiptImage ? receiptImage : ""}
            {receiptImage ? <img src={receiptImage} alt="" /> : <></>}

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
              disabled
            />
          </div>

          <div className="flex flex-col items-end space-y-2">
            <TableContainer sx={{ maxHeight: 180 }} component={Paper}>
              <Table sx={{ width: "100%" }} stickyHeader aria-label="sticky table">
                <TableHead>
                  <TableRow>
                    <TableCell>상품명</TableCell>
                    <TableCell align="center">
                      상품
                      <br />
                      금액
                    </TableCell>
                    <TableCell align="center">
                      상품
                      <br />
                      수량
                    </TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {buyItems.length === 0 ? (
                    <TableCell>입력된 정보가 없습니다.</TableCell>
                  ) : (
                    <>
                      {buyItems.map((item, index) => (
                        <TableRow key={index} sx={{ "&:last-child td, &:last-child th": { border: 0 } }}>
                          <TableCell component="th" scope="row">
                            {item.item}
                          </TableCell>
                          <TableCell align="center">{item.price}</TableCell>
                          <TableCell align="center">{item.quantity}</TableCell>
                        </TableRow>
                      ))}
                    </>
                  )}
                </TableBody>
              </Table>
            </TableContainer>

            <TextField
              sx={{ width: "100%" }}
              id="filled-basic"
              label="상품명"
              variant="filled"
              value={itemName}
              onChange={(e) => setItemName(e.target.value)}
            />
            <div className="flex space-x-3">
              <TextField
                id="filled-basic"
                label="상품금액"
                variant="filled"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
              />
              <TextField
                id="filled-basic"
                label="구매수량"
                variant="filled"
                value={quantity}
                onChange={(e) => setQuantity(Number(e.target.value))}
              />
            </div>
            <button
              className={`w-28 p-2 text-sm text-[#565656] font-semibold bg-[#BBDBFF] rounded-md ${
                itemName === "" || quantity === 0 ? "opacity-30" : ""
              }`}
              onClick={() => handleAddItem()}
              disabled={itemName === "" || quantity === 0}>
              상품 추가
            </button>
          </div>

          <button
            className={`p-2 text-white font-semibold bg-[#0471E9] rounded-md ${
              buyStore === "" || paid === "" || buyItems.length === 0 ? "opacity-30" : ""
            }`}
            onClick={() => handleCreateAccountBook()}
            disabled={buyStore === "" || paid === "" || buyItems.length === 0}>
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
