package com.stratumtech.realtyauthuser.service;

import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.entity.Administrator;
import com.stratumtech.realtyauthuser.repository.AgentRepository;
import com.stratumtech.realtyauthuser.repository.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final AgentRepository agentRepository;
    private final AdministratorRepository administratorRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthUserDetails authenticate(String email, String password) {
        log.info("Попытка логина: email={}", email);
        Optional<Agent> agentOpt = agentRepository.findByEmail(email.toLowerCase());
        if (agentOpt.isPresent()) {
            Agent agent = agentOpt.get();
            log.info("Найден агент: {}", agent.getEmail());
            boolean passwordMatch = passwordEncoder.matches(password, agent.getPassword());
            log.info("Пароль совпадает? {}", passwordMatch);
            log.info("isBlocked: {}", agent.getIsBlocked());
            if (!passwordMatch) {
                throw new RuntimeException("Invalid password");
            }
            if (Boolean.TRUE.equals(agent.getIsBlocked())) {
                throw new RuntimeException("User is blocked");
            }
            log.info("Успешная аутентификация агента: {}", agent.getAgentUuid());
            return new AuthUserDetails(
                agent.getAgentUuid(),
                "AGENT",
                0,
                null
            );
        }
        Optional<Administrator> adminOpt = administratorRepository.findByEmail(email.toLowerCase());
        if (adminOpt.isPresent()) {
            Administrator admin = adminOpt.get();
            log.info("Найден админ: {}", admin.getEmail());
            boolean passwordMatch = passwordEncoder.matches(password, admin.getPassword());
            log.info("Пароль совпадает? {}", passwordMatch);
            log.info("isBlocked: {}", admin.getIsBlocked());
            if (!passwordMatch) {
                throw new RuntimeException("Invalid password");
            }
            if (Boolean.TRUE.equals(admin.getIsBlocked())) {
                throw new RuntimeException("User is blocked");
            }
            String role = admin.getRoleId() == 1 ? "ADMIN" : "REGIONAL_ADMIN";
            int region = admin.getRegionId() != null ? admin.getRegionId() : 0;
            log.info("Успешная аутентификация администратора: {} role={}", admin.getAdminUuid(), role);
            return new AuthUserDetails(
                admin.getAdminUuid(),
                role,
                region,
                admin.getReferal()
            );
        }
        log.info("Пользователь не найден: {}", email);
        throw new RuntimeException("User not found");
    }

    public record AuthUserDetails(UUID userId, String role, int region, String referralCode) {}
} 