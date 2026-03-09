package com.transferapp.mapper;


import com.transferapp.dto.response.TransferResponse;
import com.transferapp.entity.Account;
import com.transferapp.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for building a TransferResponse from a completed Transaction.
 *
 * After a successful transfer, the updated sender Account is passed alongside
 * the saved Transaction so the new balance can be included in the response.
 *
 * Replaces the manual TransferResponse.builder() block in TransferService.transfer().
 */
@Mapper(componentModel = "spring")
public interface TransferMapper {

    @Mapping(target = "senderAccountNumber",   source = "transaction.senderAccount.accountNumber")
    @Mapping(target = "receiverAccountNumber", source = "transaction.receiverAccount.accountNumber")
    @Mapping(target = "amount",                source = "transaction.amount")
    @Mapping(target = "status",                source = "transaction.status")
    @Mapping(target = "createdAt",             source = "transaction.createdAt")
    @Mapping(target = "newBalance",            source = "senderAccount.balance")
    TransferResponse toResponse(Transaction transaction, Account senderAccount);
}
