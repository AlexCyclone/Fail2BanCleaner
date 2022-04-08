package icu.cyclone.bancleaner.service.converter;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {UnbanConverter.class})
@ExtendWith(SpringExtension.class)
class UnbanConverterTest {

    @Autowired
    public UnbanConverter converter;

    @ParameterizedTest
    @MethodSource("convertData")
    public void convertTest(
        String template,
        String regEx,
        String testValue,
        boolean expectation
    ) {
        var result = converter.toRegex(template);
        assertEquals(regEx, result);
        assertEquals(expectation, testValue.matches(result));
    }

    private static Stream<Arguments> convertData() {
        return Stream.of(
            Arguments.of("192.168.0.*", "192\\.168\\.0\\..*", "192.168.0.195", true),
            Arguments.of("192.16?.0.*", "192\\.16.\\.0\\..*", "192.16.0.", false),
            Arguments.of("192.16?.0.*", "192\\.16.\\.0\\..*", "192.161.0.", true),
            Arguments.of("192.168.0.*", "192\\.168\\.0\\..*", "192.168.1.1", false),
            Arguments.of("?92.*.0.*", ".92\\..*\\.0\\..*", "192.168.0.1", true)
        );
    }
}