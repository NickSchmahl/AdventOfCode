import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

void main() throws IOException {
    Path day3File = Path.of("Aoc25/exercises/Day3.txt");

    try (final Stream<String> lines = Files.lines(day3File)) {
        final long password_part_one = lines
                .map(Bank::parse)
                .map(Bank::highestJoltage)
                .reduce(0, Integer::sum);
        System.out.println("Part one: " + password_part_one);
    }

    try (final Stream<String> lines = Files.lines(day3File)) {
        final long password_part_two = lines
                .map(Bank::parse)
                .map(Bank::highestJoltage2)
                .reduce(0L, Long::sum);
        System.out.println("Part two: " + password_part_two);
    }
}

record Battery(int joltage) {
    Battery {
        if (joltage < 1 || joltage > 9) {
            throw new IllegalArgumentException();
        }
    }
}

record Bank(List<Battery> batteries) {
    int highestJoltage() {
        int size = batteries.size();
        Battery biggestBattery = findBiggestBattery(batteries.subList(0, size - 1));
        Battery biggestAfterFirst = findBiggestBattery(batteries.subList(batteries.indexOf(biggestBattery) + 1, size));
        return biggestBattery.joltage * 10 + biggestAfterFirst.joltage;
    }

    long highestJoltage2() {
        int digitsRemaining = 12;
        int startIndex = 0;
        List<Battery> searchRange = batteries.subList(startIndex, batteries.size() - digitsRemaining + 1);
        long result = 0;
        while (digitsRemaining >= 0 && batteries.size() - startIndex > digitsRemaining) {
            final Battery biggestBattery = findBiggestBattery(searchRange);
            int indexOfBiggestBattery = searchRange.indexOf(biggestBattery);
            result = (result * 10) + biggestBattery.joltage;

            digitsRemaining--;
            if (digitsRemaining == 0) {
                return result;
            }
            startIndex += indexOfBiggestBattery + 1;
            searchRange = batteries.subList(startIndex, batteries.size() - digitsRemaining + 1);
        }
        while (digitsRemaining > 0) {
            result = (result * 10) + batteries.get(batteries.size() - digitsRemaining).joltage;
            digitsRemaining--;
        }
        return result;
    }

    static Bank parse(final String s) {
        return new Bank(s.chars().boxed().map(num -> num - 48).map(Battery::new).toList());
    }

    static Battery findBiggestBattery(List<Battery> batteries) {
        return batteries.stream().max(Comparator.comparing(Battery::joltage)).orElseThrow();
    }
}

static class Day3Test {

    static Stream<Arguments> highestJoltageArguments() {
        return Stream.of(
                Arguments.of("12345", 45),
                Arguments.of("987654321111111", 98),
                Arguments.of("811111111111119", 89),
                Arguments.of("234234234234278", 78),
                Arguments.of("818181911112111", 92)
        );
    }

    @ParameterizedTest
    @MethodSource("highestJoltageArguments")
    void highestJoltageTest(String bank, int expected) {
        assertEquals(expected, Bank.parse(bank).highestJoltage());
    }

    static Stream<Arguments> highestJoltage2Arguments() {
        return Stream.of(
                Arguments.of("987654321111111", 987654321111L),
                Arguments.of("811111111111119", 811111111119L),
                Arguments.of("234234234234278", 434234234278L),
                Arguments.of("818181911112111", 888911112111L)
        );
    }

    @ParameterizedTest
    @MethodSource("highestJoltage2Arguments")
    void highestJoltage2Test(String bank, long expected) {
        assertEquals(expected, Bank.parse(bank).highestJoltage2());
    }
}

