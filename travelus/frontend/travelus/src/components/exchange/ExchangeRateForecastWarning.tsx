import React from "react";
import { Drawer, Typography, Box, List, ListItem, ListItemText } from "@mui/material";

interface RecommendationDrawerProps {
  open: boolean;
  onClose: () => void;
}

const RecommendationDrawer: React.FC<RecommendationDrawerProps> = ({ open, onClose }) => {
  const predictionChallenges = [
    "시장의 변동성: 환율은 다양한 경제, 정치적 요인에 의해 급변할 수 있어요",
    "글로벌 이벤트: 예측하기 어려운 세계적 사건들이 환율에 큰 영향을 미칠 수 있어요",
    "정책 변화: 중앙은행의 정책 변화가 환율에 즉각적인 영향을 줄 수 있어요",
    "데이터의 한계: 과거 데이터가 항상 미래를 정확히 예측하기는 어려워요",
  ];

  const usedInformation = [
    "과거 환율 데이터",
    "경제 지표 (GDP, 인플레이션율 등)",
    "중앙은행 정책 동향",
    "국제 무역 데이터",
    "정치적 안정성 지수",
  ];
};

export default RecommendationDrawer;
