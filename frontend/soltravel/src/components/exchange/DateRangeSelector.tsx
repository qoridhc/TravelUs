import React from 'react';

interface DateRangeSelectorProps {
  onSelect: (days: number) => void;
  selectedRange: number;
}

const DateRangeSelector: React.FC<DateRangeSelectorProps> = ({ onSelect, selectedRange }) => {
  const ranges = [
    { label: '1주일', days: 7 },
    { label: '15일', days: 15 },
    { label: '1개월', days: 30 },
    { label: '3개월', days: 90 },
  ]

return (
  <div className="flex space-x-2">
    {ranges.map(range => (
      <button
        key={range.days}
        onClick={() => onSelect(range.days)}
        className={`px-3 py-1 rounded ${
          selectedRange === range.days
            ? 'bg-blue-500 text-white'
            : 'bg-gray-200 text-gray-700'
        }`}
      >
        {range.label}
      </button>
    ))}
  </div>
  );
};

export default DateRangeSelector;