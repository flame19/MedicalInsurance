package ua.edu.insurance.plan;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.edu.insurance.plan.InsurancePlanDto.Request;
import ua.edu.insurance.plan.InsurancePlanDto.Response;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InsurancePlanService {

    private final InsurancePlanRepository repository;

    public List<Response> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size, Sort.by("id")))
                .stream().map(Response::from).toList();
    }

    public Response findById(Long id) {
        return Response.from(requireById(id));
    }

    @Transactional
    public Response create(Request request) {
        InsurancePlan value = new InsurancePlan();
        apply(value, request);
        return Response.from(repository.save(value));
    }

    @Transactional
    public Response update(Long id, Request request) {
        InsurancePlan value = requireById(id);
        apply(value, request);
        return Response.from(value);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(requireById(id));
    }

    private InsurancePlan requireById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Insurance plan " + id + " was not found"));
    }

    private void apply(InsurancePlan value, Request request) {
        value.setName(request.name().trim());
        value.setCoverageLimit(request.coverageLimit());
        value.setAnnualPremium(request.annualPremium());
    }
}
