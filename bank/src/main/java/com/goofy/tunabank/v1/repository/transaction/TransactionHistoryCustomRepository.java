package com.goofy.tunabank.v1.repository.transaction;

import com.goofy.tunabank.v1.domain.TransactionHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryListRequestDto;
import java.util.List;
import java.util.Optional;

public interface TransactionHistoryCustomRepository {

  Optional<List<TransactionHistory>> findByCustomOrder(TransactionHistoryListRequestDto requestDto);

}
