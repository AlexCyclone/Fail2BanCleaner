package icu.cyclone.bancleaner.service.converter;

import org.springframework.stereotype.Component;

@Component
public class UnbanConverter {

    public String toRegex(String template) {
        return template
            .replaceAll("\\.", "\\\\.")
            .replaceAll("\\*", ".*")
            .replaceAll("\\?", ".");
    }
}