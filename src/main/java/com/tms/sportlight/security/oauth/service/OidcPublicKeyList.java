package com.tms.sportlight.security.oauth.service;

import java.util.List;
import lombok.Data;

@Data
public class OidcPublicKeyList {

    private List<OidcPublicKey> keys;
}
