package icu.cyclone.bancleaner;

import icu.cyclone.bancleaner.service.BanService;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class BanCleanerRunner implements CommandLineRunner {
    private final BanService banService;

    @Generated
    @Override
    public void run(String... args) {
        banService.autoClean();
    }
}
