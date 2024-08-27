import React from 'react';
import { AccountInfo } from '../../types/account';

type AccountDetailsProps = {
  accounts: AccountInfo[];
};

const AccountDetails = ({ accounts }: AccountDetailsProps): React.ReactElement => {
  return (
    <div>
      {accounts.map((accountInfo, index))}
      <div className='p-4 mb-4 bg-white rounded-lg shadow'>
        <div className='mb-2 flex items-center'>
        <div className='w-8 h-8 mr-2 bg-[#0046FF] rounded-full'></div>
          <div className='ml-3'>
            <p className='font-semibold'>{accountInfo.bankName}</p>
            <p className='font-semibold'>{accountInfo.accountName}</p>
            <p className='text-sm text-gray-500'>{accountInfo.accountNo}</p>
          </div>
        </div>
        <p className='text-2xl font-bold text-right'>{(accountInfo.accountBalance).toLocaleString()} {accountInfo.currency}</p>
      </div>
    </div>
    
  );
};

export default AccountDetails;