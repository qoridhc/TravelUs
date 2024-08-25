import React from 'react';
import { ChevronDown, ArrowDown } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

// 백에서 받아올 데이터들 interface로 type 설정
interface Account {
  id: string;
  type: string;
  name: string;
  accountNumber: string;
  balance: number;
  currency: string;
}

// 에시 데이터들, axios로 백에서 데이터 받아와야 함
const SelectAccount: React.FC = () => {
  const navigate = useNavigate();
  const accounts: Account[] = [
    {
      id: '1',
      type: '모임 통장',
      name: '신한은행 모임 통장',
      accountNumber: '신한 110-455-247307',
      balance: 3000000,
      currency: '원'
    },
    {
      id: '2',
      type: '트래블 박스',
      name: '신한은행 외화 모임 통장',
      accountNumber: '여행 예정일 : 25.01.13',
      balance: 300000,
      currency: '엔'
    }
  ];

//   해당 계좌에 Id가 있을 경우 사용
//   const handleAccountSelect = (accountId: string) => {
//     navigate(`/exchange`);
//   };

  return (
    <div className="p-4 max-w-md mx-auto bg-gray-100 min-h-screen">
    <h1 className="text-xl font-bold mb-4">모임 통장 선택</h1>
      <div className="mb-4">
        <button className="w-full flex items-center justify-between bg-white rounded-lg p-3 shadow">
          <span>환전할 모임 통장 선택</span>
          <ChevronDown className="w-5 h-5" />
        </button>
      </div>
      <div className="space-y-4">
        <div className="bg-white rounded-lg p-4 shadow">
          <h2 className="font-bold mb-2">모임 통장</h2>
          <div className="flex items-center mb-2">
            <div className="w-8 h-8 bg-[#0046FF] rounded-full mr-2"></div>
            <div>
              <p className="font-semibold">{accounts[0].name}</p>
              <p className="text-sm text-gray-500">{accounts[0].accountNumber}</p>
            </div>
          </div>
          <p className="text-right font-bold">{accounts[0].balance.toLocaleString()} {accounts[0].currency}</p>
        </div>
        
        <div className="flex justify-center items-center my-4">
          <ArrowDown className="w-8 h-8 text-[#0046FF]" />
        </div>
        
        <div className="bg-white rounded-lg p-4 shadow">
          <h2 className="font-bold mb-2">트래블 박스</h2>
          <div className="flex items-center mb-2">
            <div className="w-8 h-8 bg-[#0046FF] rounded-full mr-2"></div>
            <div>
              <p className="font-semibold">{accounts[1].name}</p>
              <p className="text-sm text-gray-500">{accounts[1].accountNumber}</p>
            </div>
          </div>
          <p className="text-right font-bold">{accounts[1].balance.toLocaleString()} {accounts[1].currency}</p>
        </div>
      </div>
      <button
        className="w-full bg-[#0046FF] text-white py-3 rounded-lg mt-4"
        // onClick={() => handleAccountSelect(accounts[0].id)}
      >
        환전하기
      </button>
    </div>
  );
};

export default SelectAccount;