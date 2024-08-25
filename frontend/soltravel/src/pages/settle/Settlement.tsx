import React, { useEffect, useState } from 'react';

// 정산하려는 모임 통장에 관한 데이터 interface type 선언
interface AccountData {
    balance: number;
    memberCount: number;
    accountNumber: string;
    bankName: string;
}

const Settlement: React.FC = () => {
    const [accountData, setAccountData] = useState<AccountData | null>(null);
    const [settlementAmount, setSettlementAmount] = useState<number>(0);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        // 예시 데이터 활용, 실제는 API를 통해 백에서 데이터를 요청해야함
        const fetchMockData = () => {
            setTimeout(() => {
                const mockData: AccountData = {
                    balance: 1000000,
                    memberCount: 5,
                    accountNumber: '신한 110-455-247307',
                    bankName: '신한은행'
                };
                setAccountData(mockData);
                setSettlementAmount(Math.floor(mockData.balance / mockData.memberCount));
                setIsLoading(false);
            }, 1000);
        };

        fetchMockData();
    }, []);

    // 정산 신청 로직 (아마 백에 요청 보내면 될듯?)
    const handleSettlement = () => {
        setIsLoading(true);
        // 정산 처리 시뮬레이션
        setTimeout(() => {
            console.log('정산 완료');
            setIsLoading(false);
        }, 1000);
    };

    // 로딩
    if (isLoading) {
        return <div className='p-4 text-center'>Loading...</div>;
    }

    // 에러 처리
    if (error) {
        return <div className='p-4 text-center text-red-500'>{error}</div>;
    }

    // 데이터 없을 경우
    if (!accountData) {
        return <div className='p-4 text-center'>데이터를 불러올 수 없습니다.</div>;
    }

    return (
        <div className="p-4 max-w-md mx-auto bg-gray-100 min-h-screen">
            <h1 className="mb-4 text-xl font-bold">모임 통장 정산</h1>
            <h2 className="mb-2 font-bold">정산 계산법?</h2>
            <p className="mb-4 text-sm text-gray-500">남은 금액 / 모임 통장 인원</p>
            <div className="p-4 mb-4 bg-white rounded-lg shadow">
                <div className="mb-2 flex items-center">
                    <div className="w-8 h-8 mr-2 bg-[#0046FF] rounded-full"></div>
                <div>
                    <p className="font-semibold">{accountData.bankName} 올인원모임통장</p>
                    <p className="text-sm text-gray-500">{accountData.accountNumber}</p>
                </div>
            </div>
                <input
                type="text"
                className="p-2 w-full text-right text-2xl font-bold border rounded"
                value={accountData.balance.toLocaleString()}
                readOnly
                />
                <p className="text-right text-sm text-gray-500">원</p>
            </div>
            <div className="mb-4">
                <p className="font-bold">모임 통장 인원: {accountData.memberCount}명</p>
                <p className="mt-2 font-bold">예상 정산 금액</p>
                <input
                type="text"
                className="p-2 mt-1 w-full text-right text-2xl font-bold border rounded "
                value={settlementAmount.toLocaleString()}
                readOnly
                />
                <p className="text-right text-sm text-gray-500">원</p>
            </div>
            <button
                className="mt-4 py-3 w-full bg-[#0046FF] text-white font-bold rounded-lg"
                onClick={handleSettlement}
                disabled={isLoading}
            >
                {isLoading ? '처리 중...' : '정산하기'}
            </button>
        </div>
    );
};

export default Settlement