import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { 
  Typography, 
  Button, 
  Box, 
  Container, 
  Paper, 
  ToggleButtonGroup, 
  ToggleButton 
} from '@mui/material';
import { ChevronLeft } from "lucide-react";
import { LineChart } from '@mui/x-charts/LineChart';
import { exchangeApi } from '../../api/exchange';
import { ExchangeRateInfo, currencyNames } from '../../types/exchange';

const ExchangeDetail: React.FC = () => {
  const { currencyCode } = useParams<{ currencyCode: string }>();
  const navigate = useNavigate();
  const [exchangeData, setExchangeData] = useState<ExchangeRateInfo | null>(null);
  const [historicalData, setHistoricalData] = useState<{x: number, y: number}[]>([]);
  const [selectedPeriod, setSelectedPeriod] = useState<string>('1일');

  useEffect(() => {
    const fetchData = async () => {
      if (currencyCode) {
        try {
          const rates = await exchangeApi.getExchangeRates();
          const currentRate = rates.find(rate => rate.currencyCode === currencyCode);
          setExchangeData(currentRate || null);

          // TODO: Implement API call for historical data
          // const history = await exchangeApi.getExchangeRateHistory({ currencyCode, period: selectedPeriod });
          // setHistoricalData(history);

          // Temporary mock data
          setHistoricalData([
            { x: 0, y: 1340 },
            { x: 1, y: 1345 },
            { x: 2, y: 1342 },
            { x: 3, y: 1348 },
            { x: 4, y: 1350 },
          ]);
        } catch (error) {
          console.error('Failed to fetch exchange rate data:', error);
        }
      }
    };

    fetchData();
  }, [currencyCode, selectedPeriod]);

  const handlePeriodChange = (
    event: React.MouseEvent<HTMLElement>,
    newPeriod: string,
  ) => {
    if (newPeriod !== null) {
      setSelectedPeriod(newPeriod);
    }
  };

  if (!exchangeData) {
    return <Typography>Loading...</Typography>;
  }

  return (
    <Container maxWidth="sm">
      <Box sx={{ my: 4 }}>
        <Button startIcon={<ChevronLeft />} onClick={() => navigate(-1)} sx={{ mb: 2 }}>
          뒤로
        </Button>
        <Typography variant="h4" component="h1" gutterBottom>
          {currencyNames[currencyCode || ''] || '알 수 없는 통화'}
        </Typography>
        <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
          <Typography variant="h5" gutterBottom>
            실시간 환율: {exchangeData.exchangeRate.toFixed(2)}원
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            어제보다 {(exchangeData.exchangeRate - 1340).toFixed(2)}원 ▲
          </Typography>
        </Paper>
        <Box sx={{ mb: 2 }}>
          <ToggleButtonGroup
            value={selectedPeriod}
            exclusive
            onChange={handlePeriodChange}
            aria-label="period selection"
          >
            <ToggleButton value="1일" aria-label="1일">
              1일
            </ToggleButton>
            <ToggleButton value="1주" aria-label="1주">
              1주
            </ToggleButton>
            <ToggleButton value="3달" aria-label="3달">
              3달
            </ToggleButton>
          </ToggleButtonGroup>
        </Box>
        <Paper elevation={3} sx={{ p: 2, mb: 3 }}>
          <Box sx={{ width: '100%', height: 300 }}>
            <LineChart
              xAxis={[{ data: [0, 1, 2, 3, 4] }]}
              series={[
                {
                  data: historicalData.map(d => d.y),
                  area: true,
                },
              ]}
              width={500}
              height={300}
            />
          </Box>
        </Paper>
        <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
          <Button variant="outlined" color="primary">
            원화로 바꾸기
          </Button>
          <Button variant="contained" color="primary">
            외화 채우기
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default ExchangeDetail;