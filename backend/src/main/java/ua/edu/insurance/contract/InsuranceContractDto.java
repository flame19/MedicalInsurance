package ua.edu.insurance.contract;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public final class InsuranceContractDto {

    private InsuranceContractDto() {
    }

    public record Request(
            @NotBlank @Size(max = 40) String contractNumber,
            @NotNull LocalDate startDate,
            @NotNull LocalDate endDate,
            @NotNull ContractStatus status,
            @NotNull @Positive Long clientId,
            @NotNull @Positive Long insurancePlanId
    ) {
    }

    public record Response(
            Long id,
            String contractNumber,
            LocalDate startDate,
            LocalDate endDate,
            ContractStatus status,
            Long clientId,
            Long insurancePlanId
    ) {
        static Response from(InsuranceContract value) {
            return new Response(
                    value.getId(),
                    value.getContractNumber(),
                    value.getStartDate(),
                    value.getEndDate(),
                    value.getStatus(),
                    value.getClient().getId(),
                    value.getInsurancePlan().getId()
            );
        }
    }
}
