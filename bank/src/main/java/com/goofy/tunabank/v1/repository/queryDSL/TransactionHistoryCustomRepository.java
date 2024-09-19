package com.goofy.tunabank.v1.repository.queryDSL;

import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryRequestDto;
import java.util.List;
import java.util.Optional;

public interface TransactionHistoryCustomRepository {

  Optional<List<TransactionHistory>> findByCustomOrder(TransactionHistoryRequestDto requestDto);

}
