import React, { useState, useEffect } from "react";
import { exchangeApi } from "../../api/exchange";
import { AllDetailedPredictions, DetailedPrediction } from "../../types/exchange";

const ExchangeRateForecastDetail: React.FC = () => {
  const [predictions, setPredictions] = useState<AllDetailedPredictions | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedCurrency, setSelectedCurrency] = useState<"USD" | "JPY">("USD");

  useEffect(() => {
    const loadPredictions = async () => {
      try {
        const data = await exchangeApi.fetchDetailedPredictions();
        setPredictions(data);
        setLoading(false);
      } catch (err) {
        setError("Failed to load predictions");
        setLoading(false);
      }
    };

    loadPredictions();
  }, []);

  const renderForecastStats = (stats: DetailedPrediction["forecast_stats"]) => {
    return (
      <div className="forecast-stats">
        <h3 className="text-lg font-semibold mb-2">Forecast Statistics</h3>
        <p>Average: {stats.average.toFixed(2)}</p>
        <p>Minimum: {stats.min.toFixed(2)}</p>
        <p>Maximum: {stats.max.toFixed(2)}</p>
        <h4 className="text-md font-semibold mt-4 mb-2">Percentiles</h4>
        <ul className="list-disc pl-5">
          {[10, 20, 30, 40, 50, 60, 70, 80, 90, 100].map((percentile) => (
            <li key={percentile}>
              {percentile}th: {stats[`p${percentile}` as keyof typeof stats]?.toFixed(2) ?? "N/A"}
            </li>
          ))}
        </ul>
      </div>
    );
  };

  const renderCurrencyPrediction = (currency: "USD" | "JPY", prediction: DetailedPrediction) => (
    <div key={currency} className="currency-prediction">
      <h2 className="text-xl font-bold mb-4">{currency} Prediction</h2>
      <p>Current Rate: {prediction.current_rate.toFixed(2)}</p>
      <p>Trend: {prediction.trend}</p>
      <h3 className="text-lg font-semibold mt-4 mb-2">Forecast</h3>
      <ul className="list-disc pl-5">
        {Object.entries(prediction.forecast).map(([date, rate]) => (
          <li key={date}>
            {date}: {rate.toFixed(2)}
          </li>
        ))}
      </ul>
      {renderForecastStats(prediction.forecast_stats)}
    </div>
  );

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;
  if (!predictions) return <div>No predictions available</div>;

  return (
    <div className="h-full p-5 pb-8">
      <div className="exchange-rate-forecast-detail">
        <h1 className="text-2xl font-bold mb-4">희망 환율 추천</h1>
        <p className="mb-4">Last Updated: {predictions.last_updated}</p>
        <div className="mb-3 flex justify-center items-center bg-gray-200 rounded-full p-1">
          {(["USD", "JPY"] as const).map((currency) => (
            <button
              key={currency}
              onClick={() => setSelectedCurrency(currency)}
              className={`flex-1 py-2 text-center ${
                selectedCurrency === currency
                  ? "bg-white text-[#353535] font-bold shadow-sm rounded-full"
                  : "text-gray-600"
              }`}>
              {currency}
            </button>
          ))}
        </div>
        {renderCurrencyPrediction(selectedCurrency, predictions[selectedCurrency])}
      </div>
    </div>
  );
};

export default ExchangeRateForecastDetail;
