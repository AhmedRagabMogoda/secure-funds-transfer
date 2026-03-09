package com.transferapp.mapper;


import com.transferapp.dto.response.TransactionResponse;
import com.transferapp.entity.Transaction;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


/**
 * MapStruct mapper for Transaction entity → TransactionResponse DTO.
 *
 * Replaces the manual mapToTransactionResponse() helper previously
 * written in TransferService.
 *
 * The @Context Long viewerAccountId is passed at call-time so the mapper
 * can determine the transfer direction (SENT / RECEIVED) without
 * requiring extra fields on the entity.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "senderAccountNumber", source = "senderAccount.accountNumber")
    @Mapping(target = "receiverAccountNumber", source = "receiverAccount.accountNumber")
    @Mapping(target = "direction", expression = "java(resolveDirection(transaction, viewerAccountId))")
    TransactionResponse toResponse(Transaction transaction, @Context Long viewerAccountId);

    /**
     * Determines whether the transaction was SENT or RECEIVED
     * from the perspective of the viewing account.
     */
    default String resolveDirection(Transaction transaction, Long viewerAccountId) {
        return transaction.getSenderAccount().getId().equals(viewerAccountId)
                ? "SENT"
                : "RECEIVED";
    }
}
