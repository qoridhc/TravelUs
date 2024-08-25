import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';

// 해당 트레블 박스 데이터 타입 선언
interface TravleBoxInfo {
    balance: number;
    currency: string;
    accountNumber: string;
}

const SettleExchange: React.FC = () => {
    const navigate = useNavigate();
    const [exchangeAmount, setExchangeAmount] = useState<string>('');
    const [expectedExchange, setExpectedExchange] = useState<string>('0');
    const [travelBox, setTravelBox] = useState<TravleBoxInfo>({
        balance: 0,
        currency: '',
        accountNumber: ''
    })
    
    useEffect(() => {
        // API 요청으로 travel box 관련 정보 가져오기
        // 아래는 예시 데이터
        setTravelBox({
            balance: 5000,
            currency: '엔',
            accountNumber: '신한 110-455-247307'
        });
    }, []);

    // 환율 계산 로직인데, 백에서 계산을 해서 던져주는건지? 여기서 계산을 해야하는건지?
    const handleExchangeAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const amount = e.target.value.replace(/[^0-9]/g, '');
        const numAmount = Number(amount);
        
        if (numAmount > travelBox.balance) {
            setExchangeAmount(travelBox.balance.toLocaleString());
            // 실제로는 여기서 환율 계산 로직을 구현 필요
            setExpectedExchange((travelBox.balance * 10).toLocaleString());
        } else {
            setExchangeAmount(numAmount.toLocaleString());
            // 실제로는 여기서 환율 계산 로직을 구현 필요
            setExpectedExchange((numAmount * 10).toLocaleString());
        }
    };

    // 환전 이후에 바로 정산 페이지 가게 하기 + 환전 요청 필요
    const handleExchange = () => {
    // TODO: 환전 로직 구현 필요
    navigate('/settlement');
    };

    return (
        <div className="p-4 max-w-md mx-auto bg-gray-100 min-h-screen">
            <h1 className="mb-4 text-xl font-bold">환전하기</h1>
            <h2 className="mb-2 font-bold">환전할 금액 입력</h2>
            <div className="p-4 mb-4 bg-white rounded-lg shadow">
                <div className="mb-2 flex items-center">
                    <div className="w-8 h-8 mr-2 bg-[#0046FF] rounded-full"></div>
                    <div>
                        <p className="font-semibold">신한은행 트래블 박스</p>
                        <p className="text-sm text-gray-500">{travelBox.accountNumber}</p>
                    </div>
                </div>
                <input
                    type="text"
                    className="p-2 w-full text-right text-2xl font-bold border rounded"
                    value={exchangeAmount}
                    onChange={handleExchangeAmountChange}
                    placeholder="0"
                />
                <p className="text-right text-sm text-gray-500">{travelBox.currency}</p>
                <p className="text-right text-sm text-gray-500">
                    트래블 박스 잔액: {travelBox.balance.toLocaleString()} {travelBox.currency}
                </p>
                <p className="text-right text-sm text-gray-500">
                    최대 {travelBox.balance.toLocaleString()} {travelBox.currency} 환전 가능
                </p>
            </div>
            <div className="p-4 mb-4 bg-white rounded-lg shadow">
                <p className="mb-2 font-bold">예상 환전 금액</p>
                <p className="text-2xl font-bold">{expectedExchange} 원</p>
            </div>
            <button
                className="py-3 mt-4 w-full bg-[#0046FF] text-white font-bold rounded-lg"
                onClick={handleExchange}
            >
                환전하기
            </button>
        </div>
    );
};

export default SettleExchange