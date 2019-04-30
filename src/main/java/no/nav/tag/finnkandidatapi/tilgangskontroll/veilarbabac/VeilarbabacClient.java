package no.nav.tag.finnkandidatapi.tilgangskontroll.veilarbabac;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.nav.tag.finnkandidatapi.tilgangskontroll.TokenUtils;
import no.nav.tag.finnkandidatapi.tilgangskontroll.sts.STSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class VeilarbabacClient {
    private final TokenUtils tokenUtils;
    private final RestTemplate restTemplate;
    private final STSClient stsClient;
    @Value("${veilarbabac.url}") private String veilarbabacUrl;

    public boolean harSkrivetilgangTilKandidat(String fnr) {
        String uriString = UriComponentsBuilder.fromHttpUrl(veilarbabacUrl)
                .path("/person")
                .queryParam("fnr", fnr)
                .queryParam("action", "update")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("subject", tokenUtils.hentInnloggetOidcToken());
        headers.set("AUTHORIZATION", hentOidcTokenTilSystembruker());
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> jsonResponse = restTemplate.exchange(
                uriString,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        );

        log.info("veilarbabac-response: " + jsonResponse.getBody());

        return true;
    }

    private String hentOidcTokenTilSystembruker() {
        return stsClient.getToken().getAccess_token();
    }
}
