package com.goofy.tunabank.v1.repository.transaction;

import com.goofy.tunabank.v1.domain.history.AbstractHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryListRequestDto;
import java.util.List;
import java.util.Optional;

public interface TransactionHistoryCustomRepository {

//    Optional<List<AbstractHistory>> findByCustomOrder(TransactionHistoryListRequestDto requestDto);

    Optional<List<AbstractHistory>> findHistoryByAccountNo(TransactionHistoryListRequestDto accountNo);
}
