import { ChartOptions, ChartDataset } from 'chart.js';

export const setupChart = (currencyCode: string, formatCurrency: (value: number, currencyCode: string) => string): ChartOptions => {
  return {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        enabled: false,
      },
      minMaxLabels: {
        afterDatasetDraw(chart) {
          const { ctx, data, scales } = chart;
          const dataset = data.datasets[0] as ChartDataset<'line', number[]>;
          const yScale = scales.y;

          if (!dataset.data) return;

          const min = Math.min(...dataset.data);
          const max = Math.max(...dataset.data);

          dataset.data.forEach((value, index) => {
            if (value === min || value === max) {
              const x = scales.x.getPixelForValue(index);
              const y = yScale.getPixelForValue(value);

              ctx.save();
              ctx.fillStyle = 'black';
              ctx.textAlign = 'center';
              ctx.fillText(formatCurrency(value, currencyCode), x, y - 10);
              ctx.restore();
            }
          });
        }
      }
    },
    scales: {
      x: {
        display: false,
      },
      y: {
        display: false,
      },
    },
  };
};