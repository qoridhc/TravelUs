import React from 'react';
import { XAxis, XAxisProps } from 'recharts';

const CustomXAxis: React.FC<XAxisProps> = (props) => {
  return <XAxis {...props} />;
};

export default CustomXAxis;