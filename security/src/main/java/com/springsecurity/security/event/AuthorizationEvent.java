package com.springsecurity.security.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationEvent {

    @EventListener
    public void onFailure(AuthorizationDeniedEvent<?> deniedEvent){
        log.error("Login failed for user: {} due to: {}", deniedEvent.getAuthentication().get().getName(), deniedEvent.getAuthorizationResult().toString());
    }
}
