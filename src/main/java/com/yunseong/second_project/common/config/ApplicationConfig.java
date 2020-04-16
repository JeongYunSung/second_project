package com.yunseong.second_project.common.config;

import com.yunseong.second_project.common.domain.CustomUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class ApplicationConfig {

    @Bean
    public AuditorAware providerAuditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null) {
                return Optional.of(0L);
            }
            Object principal = authentication.getPrincipal();
            if (principal == null || !(principal instanceof CustomUser)) {
                return Optional.of(0L);
            }
            return Optional.of(((CustomUser)principal).getId());
        };
    }
}
