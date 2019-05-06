package no.nav.tag.finnkandidatapi.tilgangskontroll;

import no.nav.security.oidc.api.Protected;
import no.nav.tag.finnkandidatapi.kandidat.Veileder;
import no.nav.tag.finnkandidatapi.tilgangskontroll.abac.AbacClient;
import no.nav.tag.finnkandidatapi.tilgangskontroll.abac.response.XacmlResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Protected
@RestController
public class TilgangskontrollController {

    private final TilgangskontrollService tilgangskontrollService;
    private final TokenUtils tokenUtils;
    private final AbacClient abacClient;

    public TilgangskontrollController(TilgangskontrollService tilgangskontrollService, TokenUtils tokenUtils, AbacClient abacClient) {
        this.tilgangskontrollService = tilgangskontrollService;
        this.tokenUtils = tokenUtils;
        this.abacClient = abacClient;
    }

    @GetMapping("/harTilgang/{fnr}")
    public boolean harTilgang(
            @PathVariable("fnr") String fnr
    ) {
        // TODO TAG-363: Bare en testcontroller. Skal fjernes
        return tilgangskontrollService.harSkrivetilgangTilKandidat(fnr);
    }

    @GetMapping("/abac")
    public XacmlResponse kallAbac() {
        // TODO TAG-363: Bare en testcontroller. Skal fjernes
        return abacClient.ping();
    }

    @GetMapping("/innlogget-bruker")
    public ResponseEntity<Veileder> innloggetBruker() {
        return ResponseEntity.ok(tokenUtils.hentInnloggetVeileder());
    }

}
