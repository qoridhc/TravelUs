import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

interface Account {
    id: string;
    type: string;
    name: string;
    accountNumber: string;
    balance: number;
    currency: string;
    travelDate?: string;
}

const SettleStart: React.FC = () => {
    const navigate = useNavigate();
    const [accounts, setAccounts] = useState<Account[]>([]);
    const [showAlert, setShowAlert] = useState(false);

    useEffect(() => {
        // API로부터 계좌 정보를 가져오는 로직 필요
        // 아래는 예시 데이터
        setAccounts([
            {
                id: '1',
                type: '모임 통장',
                name: '신한은행 올인원모임통장',
                accountNumber: '신한 110-455-247307',
                balance: 200000,
                currency: '원'
            },
            {
                id: '2',
                type: '외화 모임 통장',
                name: '신한은행 올인원외화모임통장',
                accountNumber: '신한 110-455-2473082',
                travelDate: '25.01.13',
                balance: 5000,
                currency: '엔'
            }
        ]);
    }, []);

    const handleSettlement = () => {
        const foreignAccount = accounts.find(account => account.type === '외화 모임 통장');
        if (foreignAccount && foreignAccount.balance > 0) {
            setShowAlert(true);
        } else {
            navigate('/settlement')
        }
    };

    const handleExchange = () => {
        navigate('/settleexchange')
    }

    const handleNoExchange = () => {
        setShowAlert(false);
        navigate('/settlement');
    }

    const calculateDaysSinceTravel = (travelDate: string): number => {
        const travel = new Date(travelDate);
        const today = new Date();
        const diffTime = Math.abs(today.getTime() - travel.getTime());
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        return diffDays;
    }

    return (
        <div className="p-4 max-w-md mx-auto bg-gray-100 min-h-screen">
            <h1 className="mb-4 text-xl font-bold">정산 가능한 모임 통장</h1>
            {accounts.map(account => (
                <div key={account.id} className="mb-4 bg-white rounded-lg p-4 shadow">
                    <h2 className="font-bold mb-2">{account.type}</h2>
                    <div className="flex items-center">
                        <div className="w-8 h-8 mr-2 bg-[#0046FF] rounded-full"></div>
                        <div>
                            <p className="font-semibold">{account.name}</p>
                            <p className="text-sm text-gray-500">{account.accountNumber}</p>
                            {account.travelDate && (
                                <p className="text-sm text-gray-500">
                                    {new Date(account.travelDate) > new Date() 
                                        ? `여행 예정일: ${account.travelDate}`
                                        : `여행 후 ${calculateDaysSinceTravel(account.travelDate)}일 경과`
                                    }
                                </p>
                            )}
                        </div>
                    </div>
                    <p className="mt-2 text-right font-bold">{account.balance.toLocaleString()} {account.currency}</p>
                </div>
            ))}
            <button
                className="mt-4 w-full bg-[#0046FF] text-white font-bold py-3 rounded-lg"
                onClick={handleSettlement}
            >
                정산하기
            </button>
            {showAlert && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white p-4 rounded-lg">
                        <p>외화 모임 통장에 잔액이 있습니다. 환전 후 정산하시겠습니까?</p>
                        <div className="mt-4 flex justify-end">
                            <button className="mr-2 px-4 py-2 bg-gray-200 rounded" onClick={handleNoExchange}>아니오</button>
                            <button className="px-4 py-2 bg-[#0046FF] text-white rounded" onClick={handleExchange}>예</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default SettleStart;