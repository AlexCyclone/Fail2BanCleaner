package icu.cyclone.bancleaner.service;

import icu.cyclone.bancleaner.configuration.properties.UnbanProperties;
import icu.cyclone.bancleaner.domain.HostInfo;
import icu.cyclone.bancleaner.service.converter.UnbanConverter;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static icu.cyclone.bancleaner.TestObjects.TEMPLATE_HOST_INFO;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {UnbanDecisionService.class, UnbanConverter.class})
@EnableConfigurationProperties(UnbanProperties.class)
@ExtendWith(SpringExtension.class)
class UnbanDecisionServiceTest {

    @Autowired
    public UnbanDecisionService service;

    @ParameterizedTest
    @MethodSource("unbanData")
    public void isUnbanTest(HostInfo hostInfo, boolean isUnban) {
        assertEquals(isUnban, service.isUnban(hostInfo));
    }

    private static Stream<Arguments> unbanData() {
        return Stream.of(
            Arguments.of(TEMPLATE_HOST_INFO, false),
            Arguments.of(TEMPLATE_HOST_INFO.toBuilder().ip("10.50.100.13").build(), true),
            Arguments.of(TEMPLATE_HOST_INFO.toBuilder().countryCode("UA").build(), true),
            Arguments.of(TEMPLATE_HOST_INFO.toBuilder().countryCode("GB").build(), true),
            Arguments.of(TEMPLATE_HOST_INFO.toBuilder().latitude(37.3860518).build(), true),
            Arguments.of(TEMPLATE_HOST_INFO.toBuilder().unbannedCount(99).build(), true)
        );
    }
}