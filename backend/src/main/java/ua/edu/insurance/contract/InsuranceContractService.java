package ua.edu.insurance.contract;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.edu.insurance.contract.InsuranceContractDto.Request;
import ua.edu.insurance.contract.InsuranceContractDto.Response;
import ua.edu.insurance.client.ClientRepository;
import ua.edu.insurance.plan.InsurancePlanRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InsuranceContractService {

    private final InsuranceContractRepository repository;
    private final ClientRepository clientRepository;
    private final InsurancePlanRepository insurancePlanRepository;

    public List<Response> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size, Sort.by("id")))
                .stream().map(Response::from).toList();
    }

    public Response findById(Long id) {
        return Response.from(requireById(id));
    }

    @Transactional
    public Response create(Request request) {
        InsuranceContract value = new InsuranceContract();
        apply(value, request);
        return Response.from(repository.save(value));
    }

    @Transactional
    public Response update(Long id, Request request) {
        InsuranceContract value = requireById(id);
        apply(value, request);
        return Response.from(value);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(requireById(id));
    }

    private InsuranceContract requireById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Insurance contract " + id + " was not found"));
    }

    private void apply(InsuranceContract value, Request request) {
        if (request.endDate().isBefore(request.startDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "endDate must be on or after startDate");
        }

        value.setContractNumber(request.contractNumber().trim());
        value.setStartDate(request.startDate());
        value.setEndDate(request.endDate());
        value.setStatus(request.status());
        value.setClient(clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Client " + request.clientId() + " was not found")));
        value.setInsurancePlan(insurancePlanRepository.findById(request.insurancePlanId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Insurance plan " + request.insurancePlanId() + " was not found")));
    }
}
