package com.sid.gl.usercontext.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotNull String lastName,
        @NotNull String firstName,
        @Email String email,
        @NotNull @Size(max = 12) String password,

        @NotNull String username,
        String imageUrl
) {
}
