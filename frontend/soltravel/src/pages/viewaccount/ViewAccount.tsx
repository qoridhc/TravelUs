import React from 'react';
import { useParams } from 'react-router-dom';
import GroupAccount from '../../components/viewaccount/GroupAccount';

const GroupAccountPage = (): React.ReactElement => {
  const { userId } = useParams<{ userId: string }>();

  if (!userId) {
    return <div>사용자 ID가 필요합니다.</div>
  }

  return (
    <div className='px-4 mx-auto container'>
      <h1 className='mb-4 text-2xl font-bold'>계좌 정보</h1>
      <GroupAccount />
    </div>
  );
};

export default GroupAccountPage;