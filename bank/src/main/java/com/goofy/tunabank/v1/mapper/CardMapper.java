package com.goofy.tunabank.v1.mapper;

import com.goofy.tunabank.v1.domain.Card;
import com.goofy.tunabank.v1.domain.CardProduct;
import com.goofy.tunabank.v1.dto.card.CardIssueResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CardMapper {

  CardMapper INSTANCE = Mappers.getMapper(CardMapper.class);

  @Mapping(source = "card.cardNo", target = "cardNo")
  @Mapping(source = "card.cvc", target = "cvc")
  @Mapping(source = "card.expireAt", target = "cardExpiryDate")
  @Mapping(source = "cardProduct.cardUniqueNo", target = "cardUniqueNo")
  @Mapping(source = "cardProduct.cardName", target = "cardName")
  @Mapping(source = "cardProduct.cardDescription", target = "cardDescription")
  @Mapping(source = "accountNo", target = "withdrawalAccountNo")
  @Mapping(source = "issuerName", target = "cardIssuerName")
  CardIssueResponseDto cardToCardIssueResponseDto(Card card, CardProduct cardProduct, String accountNo, String issuerName);
}
