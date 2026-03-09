package com.transferapp.mapper;

import com.transferapp.dto.response.AccountResponse;
import com.transferapp.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for Account entity → AccountResponse DTO.
 *
 * Replaces the manual mapToResponse() helper previously written in AccountService.
 * The ownerUsername is sourced from the nested User entity relationship.
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "ownerUsername", source = "user.username")
    AccountResponse toResponse(Account account);
}
