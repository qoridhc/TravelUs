import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { accountApi } from '../../api/account';
import { settlementApi } from '../../api/settle';
import { AccountInfo, AccountParticipants } from '../../types/account';
import { SettlementRequest, SettlementResponse } from '../../types/settle';

const Settlement: React.FC = () => {
    // const { userId } = useParams<{ userId: string }>();
    const userId = 2
    // const accountNo = '0883228427954042'
    const [accountInfo, setAccountInfo] = useState<AccountInfo | null>(null);
    const [participants, setParticipants] = useState<AccountParticipants | null>(null);
    const [settlementAmount, setSettlementAmount] = useState<number>(0);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchData = async () => {
            if (!userId) {
                setError('사용자 ID가 없습니다.');
                setIsLoading(false);
                return;
            }

            try {
                const accounts = await accountApi.fetchAccountInfo(userId);
                const groupAccount = accounts.find(account => account.accountType === 'GROUP');

                if (!groupAccount) {
                    setError('모임 통장을 찾을 수 없습니다.');
                    setIsLoading(false);
                    return;
                }

                setAccountInfo(groupAccount);

                const participantsData = await accountApi.fetchParticipantInfo(groupAccount.id);
                setParticipants(participantsData);
                // console.log(participantsData)

                const participantCount = participantsData.participants.length;
                setSettlementAmount(Math.floor(groupAccount.balance / participantCount));

                setIsLoading(false);
            } catch (err) {
                console.error('Error fetching data:', err);
                setError('데이터를 불러오는 데 실패했습니다.');
                setIsLoading(false);
            }
        };

        fetchData();
    }, [userId]);

    const handleSettlement = async () => {
        if (!accountInfo) return;

        setIsLoading(true);
        try {
            const request: SettlementRequest = {
                accountId: 1,
                // accountNo: accountInfo.accountNo
                accountNo: '0883228427954042'
            };
            console.log(request);
            const response: SettlementResponse = await settlementApi.SettleInfo(request);
            alert(response.message || '정산이 완료되었습니다.');
        } catch (err) {
            console.error('Settlement error:', err);
            setError('정산 처리 중 오류가 발생했습니다.');
        } finally {
            setIsLoading(false);
        }
    };

    if (isLoading) {
        return <div className='p-4 text-center'>Loading...</div>;
    }

    if (error) {
        return <div className='p-4 text-center text-red-500'>{error}</div>;
    }

    if (!accountInfo || !participants) {
        return <div className='p-4 text-center'>데이터를 불러올 수 없습니다.</div>;
    }

    return (
        <div className="p-4 max-w-md mx-auto bg-gray-100 min-h-screen">
            <h1 className="mb-4 text-xl font-bold">모임 통장 정산</h1>
            <h2 className="mb-2 font-bold">정산 계산법</h2>
            <p className="mb-4 text-sm text-gray-500">남은 금액 / 모임 통장 인원</p>
            <div className="p-4 mb-4 bg-white rounded-lg shadow">
                <div className="mb-2 flex items-center">
                    <div className="w-8 h-8 mr-2 bg-[#0046FF] rounded-full"></div>
                    <div>
                        <p className="font-semibold">{accountInfo.accountName}</p>
                        <p className="text-sm text-gray-500">{accountInfo.accountNo}</p>
                    </div>
                </div>
                <input
                    type="text"
                    className="p-2 w-full text-right text-2xl font-bold border rounded"
                    value={accountInfo.balance.toLocaleString()}
                    readOnly
                />
                <p className="text-right text-sm text-gray-500">{accountInfo.currency.currencyCode}</p>
            </div>
            <div className="mb-4">
                <p className="font-bold">모임 통장 인원: {participants.participants.length}명</p>
                <p className="mt-2 font-bold">예상 정산 금액</p>
                <input
                    type="text"
                    className="p-2 mt-1 w-full text-right text-2xl font-bold border rounded"
                    value={settlementAmount.toLocaleString()}
                    readOnly
                />
                <p className="text-right text-sm text-gray-500">{accountInfo.currency.currencyCode}</p>
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

export default Settlement;