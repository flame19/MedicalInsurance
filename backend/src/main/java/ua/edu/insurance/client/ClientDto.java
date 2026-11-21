package ua.edu.insurance.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class ClientDto {

    private ClientDto() {
    }

    public record Request(
            @NotBlank @Size(max = 160) String fullName,
            @NotBlank @Email @Size(max = 254) String email,
            @Size(max = 32) String phone
    ) {
    }

    public record Response(
            Long id,
            String fullName,
            String email,
            String phone
    ) {
        static Response from(Client value) {
            return new Response(
                    value.getId(),
                    value.getFullName(),
                    value.getEmail(),
                    value.getPhone()
            );
        }
    }
}
