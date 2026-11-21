package ua.edu.insurance.plan;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public final class InsurancePlanDto {

    private InsurancePlanDto() {
    }

    public record Request(
            @NotBlank @Size(max = 120) String name,
            @NotNull @Positive @Digits(integer = 12, fraction = 2) BigDecimal coverageLimit,
            @NotNull @Positive @Digits(integer = 12, fraction = 2) BigDecimal annualPremium
    ) {
    }

    public record Response(
            Long id,
            String name,
            BigDecimal coverageLimit,
            BigDecimal annualPremium
    ) {
        static Response from(InsurancePlan value) {
            return new Response(
                    value.getId(),
                    value.getName(),
                    value.getCoverageLimit(),
                    value.getAnnualPremium()
            );
        }
    }
}
