package com.ssafy.soltravel.dto.transaction.request;

import com.ssafy.soltravel.domain.Enum.OrderByType;
import com.ssafy.soltravel.domain.Enum.TransacntionType;
import lombok.Data;

@Data
public class TransactionHistoryRequestDto {

    String startDate;

    String endDate;

    TransacntionType transactionType;

    OrderByType orderByType;

}
