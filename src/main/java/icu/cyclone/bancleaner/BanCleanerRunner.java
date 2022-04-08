package icu.cyclone.bancleaner;

import icu.cyclone.bancleaner.exception.CleanerException;
import icu.cyclone.bancleaner.service.BanService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class BanCleanerRunner implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(BanService.class);
    private final BanService banService;

    @Generated
    @Override
    public void run(String... args) {
        try {
            banService.autoClean();
        } catch (CleanerException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }
}
