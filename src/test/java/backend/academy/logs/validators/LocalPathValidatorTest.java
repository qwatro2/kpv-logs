package backend.academy.logs.validators;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalPathValidatorTest {
    private final Validator lpv = new LocalPathValidator();

    private static Stream<Arguments> provideDataForValidate() {
        return Stream.of(
                Arguments.of("Aboba.java", false),
                Arguments.of("/Aboba.java", false),
                Arguments.of("./Aboba.java", true),
                Arguments.of("?.java", false),
                Arguments.of("./package/Aboba.java", true),
                Arguments.of("C:/package/Aboba.java", true),
                Arguments.of("C:/package/**/Aboba.java", true),
                Arguments.of("C:/package/**/aboba/**/Aboba.java", true),
                Arguments.of("C:/package/***/aboba/Aboba.java", false),
                Arguments.of("./src/main/java/backend/academy/**.java", false),
                Arguments.of("./src/main/java/backend/academy/**/*.java", true),
                Arguments.of("/src/main/java/backend/academy/**/*.java", true),
                Arguments.of("./src/main/java/backend/academy/aboba/Aboba.java", true),
                Arguments.of("./src/main/java/**/academy/**/*.java", true),
                Arguments.of("./src//main/java/backend/academy/**/*.java", false),
                Arguments.of("./src/main/java/backend/aca*emy/**/*.java", false),
                Arguments.of("./src/backend/**/bad_pattern", false),
                Arguments.of("./src/backend/**/bad_pattern.", false),
                Arguments.of("logs/2024*", true),
                Arguments.of("logs/**/2024-08-31.txt", true),
                Arguments.of("logs/2024-08-31.pb.txt", true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDataForValidate")
    void validate(String path, boolean expected) {
        boolean actual = lpv.validate(path);
        assertEquals(expected, actual);
    }
}
