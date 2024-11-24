package games.poker.service;

import games.poker.dto.request.RegisterUserRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OktaServiceImpl implements OktaService {

    @Value("${okta.domain}")
    private String oktaDomain;

    @Value("${okta.api.token}")
    private String oktaApiToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public ResponseEntity<String> registerUser(RegisterUserRequestDto registerUserRequestDto) {
        String url = oktaDomain + "/api/v1/users?activate=true";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "SSWS " + oktaApiToken);

        HttpEntity<RegisterUserRequestDto> request = new HttpEntity<>(registerUserRequestDto, headers);

        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        }
        catch (RestClientException rce) {
            response = ResponseEntity.badRequest().body(rce.getMessage());
        }

        return response;
    }
}
