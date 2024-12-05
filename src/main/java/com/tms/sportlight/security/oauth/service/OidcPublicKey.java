package com.tms.sportlight.security.oauth.service;

import lombok.Data;

@Data
public class OidcPublicKey {
    private String kid;
    private String alg;
    private String kty;
    private String use;
    private String n;
    private String e;
}

