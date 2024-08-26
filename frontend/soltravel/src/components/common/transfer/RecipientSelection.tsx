import React, { useState } from 'react';
import { useNavigate } from 'react-router';

const RecipientSelection = () => {
  const [depositAccountNo, setDepositAccountNo] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (depositAccountNo) {
      navigate('transfer/amount', { state: { depositAccountNo}})
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>누구에게 보낼까요?</h2>
      <input
        type="text"
        value={depositAccountNo}
        onChange={(e) => setDepositAccountNo(e.target.value)}
        placeholder="계좌번호를 입력하세요" 
      />
    </form>
  )
};

export default RecipientSelection