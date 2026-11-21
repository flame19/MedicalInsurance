package ua.edu.insurance.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ua.edu.insurance.client.ClientDto.Request;
import ua.edu.insurance.client.ClientDto.Response;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository repository;

    public List<Response> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size, Sort.by("id")))
                .stream().map(Response::from).toList();
    }

    public Response findById(Long id) {
        return Response.from(requireById(id));
    }

    @Transactional
    public Response create(Request request) {
        Client value = new Client();
        apply(value, request);
        return Response.from(repository.save(value));
    }

    @Transactional
    public Response update(Long id, Request request) {
        Client value = requireById(id);
        apply(value, request);
        return Response.from(value);
    }

    @Transactional
    public void delete(Long id) {
        repository.delete(requireById(id));
    }

    private Client requireById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Client " + id + " was not found"));
    }

    private void apply(Client value, Request request) {
        value.setFullName(request.fullName().trim());
        value.setEmail(request.email().trim());
        value.setPhone(request.phone());
    }
}
