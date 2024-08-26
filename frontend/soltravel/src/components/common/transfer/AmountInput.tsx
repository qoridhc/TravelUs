// import React, { useState } from 'react';
// import { useNavigate, useLocation } from 'react-router'; 

// const AmountInput = () => {
//   const [transactionBalance, setTransactionBalance] = useState<number>(0);
//   const navigate = useNavigate();
//   const location = useLocation();
//   const { depositAccountNo } = location.state as { depositAccountNo: string};

//   const handleSubmit = (e: React.FormEvent) => {
//     e.preventDefault();
//     if (transactionBalance > 0) {
//       navigate('/transfer/confirm', { state: { depositAccountNo, transactionBalance } });
//     }
//   };

//   return (

//   )
// }

const App = () => {
  return (
    <div>얼마를 이체할지 입력하는 창입니다.</div>
  )
}

export default App