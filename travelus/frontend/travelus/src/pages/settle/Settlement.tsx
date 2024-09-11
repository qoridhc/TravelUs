import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { RiHome5Line } from 'react-icons/ri';
import { accountApi } from '../../api/account';
import { settlementApi } from '../../api/settle';
import { AccountInfo, AccountParticipants } from '../../types/account';
import { SettlementRequest, SettlementResponse } from '../../types/settle';

const Settlement: React.FC = () => {
    const navigate = useNavigate();
    const [userId, setUserId] = useState<number | null>(null);
    const [groupAccounts, setGroupAccounts] = useState<AccountInfo[]>([]);
    const [foreignAccounts, setForeignAccounts] = useState<AccountInfo[]>([]);
    const [selectedGroupAccount, setSelectedGroupAccount] = useState<AccountInfo | null>(null);
    const [selectedForeignAccount, setSelectedForeignAccount] = useState<AccountInfo | null>(null);
    const [participants, setParticipants] = useState<AccountParticipants | null>(null);
    const [settlementAmount, setSettlementAmount] = useState<number>(0);
    const [isBalanceExceeded, setIsBalanceExceeded] = useState<boolean>(true);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const storedUserId = localStorage.getItem('userId');
        if (!storedUserId) {
            setError('사용자 정보를 찾을 수 없습니다. 다시 로그인해주세요.')
            setIsLoading(false);
            return;
        }
        setUserId(parseInt(storedUserId, 10));
    }, []);

    useEffect(() => {
        const fetchData = async () => {
            if (!userId) return;

            try {
                const [generalAccounts, fetchedForeignAccounts] = await Promise.all([
                    accountApi.fetchAccountInfo(userId),
                    accountApi.fetchForeignAccountInfo(userId)
                ]);

                const groupAccountsList = generalAccounts.filter(account => account.accountType === 'GROUP');
                setGroupAccounts(groupAccountsList);
                setForeignAccounts(fetchedForeignAccounts);

                if (groupAccountsList.length > 0) {
                    setSelectedGroupAccount(groupAccountsList[0]);
                    setSelectedForeignAccount(fetchedForeignAccounts[0]);

                    const participantsData = await accountApi.fetchParticipantInfo(groupAccountsList[0].id);
                    setParticipants(participantsData);

                    const participantCount = participantsData.participants.length;
                    setSettlementAmount(Math.floor(groupAccountsList[0].balance / participantCount));
                }

                setIsLoading(false);
            } catch (err) {
                console.error('Error fetching data:', err);
                setError('데이터를 불러오는 데 실패했습니다.');
                setIsLoading(false);
            }
        };

        fetchData();
    }, [userId]);

    const handleGroupAccountChange = async (e: React.ChangeEvent<HTMLSelectElement>) => {
        const selectedAccount = groupAccounts.find(acc => acc.id === parseInt(e.target.value));
        if (selectedAccount) {
            setSelectedGroupAccount(selectedAccount);
            const correspondingForeignAccount = foreignAccounts[groupAccounts.indexOf(selectedAccount)];
            setSelectedForeignAccount(correspondingForeignAccount);

            const participantsData = await accountApi.fetchParticipantInfo(selectedAccount.id);
            setParticipants(participantsData);

            const participantCount = participantsData.participants.length;
            setSettlementAmount(Math.floor(selectedAccount.balance / participantCount));
        }
    };

    const handleSettlement = async () => {
        if (!selectedForeignAccount) return;

        setIsLoading(true);
        try {
            const request: SettlementRequest = {
                accountId: selectedForeignAccount.id,
                accountNo: selectedForeignAccount.accountNo
            };
            console.log(request);
            const response: SettlementResponse = await settlementApi.SettleInfo(request);
            alert(response.message || '정산이 완료되었습니다.');
            navigate('/');  // 정산 완료 후 홈으로 이동
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

    if (groupAccounts.length === 0 || foreignAccounts.length === 0) {
        return <div className='p-4 text-center'>모임 통장 정보를 찾을 수 없습니다.</div>;
    }

    return (
        <div className="w-full h-full bg-[#EFEFF5]">
            <div className="flex flex-col bg-[#c3d8eb]">
                <div className="pl-5 pt-5 mb-14 flex space-x-2 items-center justify-start">
                    <RiHome5Line
                        onClick={() => navigate("/")}
                        className="text-2xl text-zinc-600 cursor-pointer"
                    />
                    <p className="text-sm font-bold flex items-center">모임 통장 정산</p>
                </div>
            </div>
            
            <div className="w-full p-5 flex flex-col">
                <div className="mb-6 p-4 bg-white rounded-lg shadow">
                    <h2 className="mb-4 text-lg font-semibold">모임통장 선택</h2>
                    <select
                        className="w-full p-2 border rounded mb-2"
                        value={selectedGroupAccount?.id || ''}
                        onChange={handleGroupAccountChange}
                    >
                        {groupAccounts.map(account => (
                            <option key={account.id} value={account.id}>
                                {account.accountName} ({account.accountNo})
                            </option>
                        ))}
                    </select>
                </div>
                
                {selectedGroupAccount && (
                    <div className="mb-6 p-4 bg-white rounded-lg shadow">
                        <h3 className="font-bold mb-2">원화 모임통장</h3>
                        <div className="mb-2 flex items-center">
                            <div className="w-8 h-8 mr-2 bg-[#0046FF] rounded-full"></div>
                            <div>
                                <p className="font-semibold">{selectedGroupAccount.accountName}</p>
                                <p className="text-sm text-gray-500">{selectedGroupAccount.accountNo}</p>
                            </div>
                        </div>
                        <input
                            type="text"
                            className="p-2 w-full text-right text-2xl font-bold border rounded"
                            value={selectedGroupAccount.balance.toLocaleString()}
                            readOnly
                        />
                        <p className="text-right text-sm text-gray-500">잔액 : {selectedGroupAccount.currency.currencyCode}</p>
                    </div>
                )}
                
                {selectedForeignAccount && (
                    <div className="mb-6 p-4 bg-white rounded-lg shadow">
                        <h3 className="font-bold mb-2">외화 모임통장</h3>
                        <div className="mb-2 flex items-center">
                            <div className="w-8 h-8 mr-2 bg-[#0046FF] rounded-full"></div>
                            <div>
                                <p className="font-semibold">{selectedForeignAccount.accountName}</p>
                                <p className="text-sm text-gray-500">{selectedForeignAccount.accountNo}</p>
                            </div>
                        </div>
                        <input
                            type="text"
                            className="p-2 w-full text-right text-2xl font-bold border rounded"
                            value={selectedForeignAccount.balance.toLocaleString()}
                            readOnly
                        />
                        <p className="text-right text-sm text-gray-500">잔액 : {selectedForeignAccount.currency.currencyCode}</p>
                    </div>
                )}
                
                {participants && (
                    <div className="mb-6 p-4 bg-white rounded-lg shadow">
                        <p className="font-bold mb-2">모임 통장 인원: {participants.participants.length}명</p>
                        <p className="font-bold mb-2">예상 정산 금액 (원화 기준)</p>
                        <h6 className='mb-3 text-red-500'>외화 모임 통장에 잔액이 있을 경우 예상 정산 금액과 정산된 금액이 상이할 수 있습니다.</h6>
                        <input
                            type="text"
                            className="p-2 w-full text-right text-2xl font-bold border rounded"
                            value={settlementAmount.toLocaleString()}
                            readOnly
                        />
                        <p className="text-right text-sm text-gray-500">{selectedGroupAccount?.currency.currencyCode}</p>
                    </div>
                )}

                <button
                    className="mt-4 py-3 w-full bg-[#0046FF] text-white font-bold rounded-lg"
                    onClick={handleSettlement}
                    disabled={isLoading || !selectedGroupAccount || !selectedForeignAccount}
                >
                    {isLoading ? '처리 중...' : '정산하기'}
                </button>
            </div>
        </div>
    );
};

export default Settlement;