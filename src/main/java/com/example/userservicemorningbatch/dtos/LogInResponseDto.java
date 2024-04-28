package com.example.userservicemorningbatch.dtos;

import com.example.userservicemorningbatch.models.Token;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogInResponseDto {
    private Token token;
}
