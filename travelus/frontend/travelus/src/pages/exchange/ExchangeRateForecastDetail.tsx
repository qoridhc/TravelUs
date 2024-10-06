import React, { useState, useEffect } from "react";
import { fetchDetailedPredictions } from "../../api/exchange";
import { AllDetailedPredictions, DetailedPrediction } from "../../types/exchange";

const ExchangeRateForecastDetail: React.FC = () => {
  const [predictions, setPredictions] = useState<AllDetailedPredictions | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const loadPredictions = async () => {
      try {
        const data = await fetchDetailedPredictions();
        setPredictions(data);
        setLoading(false);
      } catch (err) {
        setError("Failed to load predictions");
        setLoading(false);
      }
    };

    loadPredictions();
  }, []);

  const renderCurrencyPrediction = (currency: string, prediction: DetailedPrediction) => (
    <div key={currency} className="currency-prediction">
      <h2>{currency} Prediction</h2>
      <p>Current Rate: {prediction.current_rate.toFixed(2)}</p>
      <p>Trend: {prediction.trend}</p>
      <h3>Forecast</h3>
      <ul>
        {Object.entries(prediction.forecast).map(([date, rate]) => (
          <li key={date}>
            {date}: {rate.toFixed(2)}
          </li>
        ))}
      </ul>
      <h3>Minimum Profit Strategy</h3>
      <p>Recommended Rate: {prediction.min_profit.recommended_rate.toFixed(2)}</p>
      <p>Recommended Date: {prediction.min_profit.recommended_date}</p>
      <h3>Maximum Profit Strategy</h3>
      <p>Average Rate: {prediction.max_profit.average_rate.toFixed(2)}</p>
      <p>Minimum Rate: {prediction.max_profit.min_rate.toFixed(2)}</p>
      <p>Maximum Rate: {prediction.max_profit.max_rate.toFixed(2)}</p>
    </div>
  );

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!predictions) return <div>No predictions available</div>;

  return (
    <div className="exchange-rate-forecast-detail">
      <h1>Exchange Rate Forecast Detail</h1>
      <p>Last Updated: {predictions.last_updated}</p>
      {renderCurrencyPrediction("USD", predictions.USD)}
      {renderCurrencyPrediction("JPY", predictions.JPY)}
    </div>
  );
};

export default ExchangeRateForecastDetail;