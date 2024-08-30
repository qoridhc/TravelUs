import React, { useEffect, useState } from "react";
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

const AccountBookInputModal = () => {
  const dispatch = useDispatch();
  const buyItems = useSelector((state: RootState) => state.accountBook.buyItems);
  const [buyStore, setBuyStore] = useState("");
  const [paid, setPaid] = useState(0);
  const [itemName, setItemName] = useState("");
  const [price, setPrice] = useState(0);
  const [quantity, setQuantity] = useState(0);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleAddItem = () => {
    let data = { item: itemName, price: price, quantity: quantity };
    dispatch(setBuyItems([...buyItems, data]));
    setItemName("");
    setPrice(0);
    setQuantity(0);
  };

  useEffect(() => {
    if (isModalOpen) {
      dispatch(setBuyItems([]));
      setBuyStore("");
      setPaid(0);
      setItemName("");
      setPrice(0);
      setQuantity(0);
    }
  }, [isModalOpen, dispatch]);

  const handleModalToggle = () => {
    setIsModalOpen(!isModalOpen);
  };

  return (
    <>
      <label
        className="btn px-5 py-2 text-center text-[#EEF4FC] text-md font-semibold bg-[#5FA0F4] rounded-xl"
        htmlFor="input-modal">
        추가
      </label>

      <input type="checkbox" id="input-modal" className="modal-toggle" />
      <div className="modal" role="dialog">
        <div className="modal-box grid gap-8">
          <div>
            <p className="text-lg font-bold">결제 정보를</p>
            <p className="text-lg font-bold">입력해주세요</p>
          </div>

          <div className="flex flex-col space-y-3">
            <TextField
              id="filled-basic"
              label="사용처"
              variant="filled"
              value={buyStore === "" ? null : buyStore}
              onChange={(e) => setBuyStore(e.target.value)}
            />
            <TextField
              id="filled-basic"
              label="사용금액"
              variant="filled"
              value={paid}
              onChange={(e) => setPaid(Number(e.target.value))}
            />
          </div>

          <div className="flex flex-col items-end space-y-3">
            <TableContainer component={Paper}>
              <Table sx={{ width: "100%" }} aria-label="simple table">
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
                onChange={(e) => setPrice(Number(e.target.value))}
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
              buyStore === "" || paid === 0 ? "opacity-30" : ""
            }`}
            disabled={buyStore === "" || paid === 0}>
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
