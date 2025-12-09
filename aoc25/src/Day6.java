import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

void main() throws IOException {
    Path day6File = Path.of("Aoc25/exercises/Day6.txt");

    String text = Files.readString(day6File);
    long password_part_one = Exercise.parse(text).stream().map(Exercise::solve).reduce(0L, Long::sum);
    System.out.println("Part one: " + password_part_one);

    long password_part_two = Exercise.parsePartTwo(text).stream().map(Exercise::solve).reduce(0L, Long::sum);
    System.out.println("Part two: " + password_part_two);
}

record Exercise(List<Integer> numbers, Operation operation) {
    Exercise() {
        this(new ArrayList<>(), Operation.ADD);
    }

    Exercise withNewNumber(int number) {
        List<Integer> newList = new ArrayList<>(numbers);
        newList.add(number);
        return new Exercise(newList, operation);
    }

    Exercise withOperation(Operation operation) {
        return new Exercise(this.numbers, operation);
    }

    long solve() {
        return switch (operation) {
            case ADD -> numbers.stream().map(i -> (long) i).reduce(0L, Long::sum);
            case MULTIPLY -> numbers.stream().map(i -> (long) i).reduce(1L, (long1, long2) -> long1 * long2);
        };
    }

    static List<Exercise> parse(String s) {
        String[] lines = s.split("\\n");
        int exercises = (int) Arrays.stream(lines[0].split(" ")).filter(str -> !str.isBlank()).count();
        List<List<Integer>> numbers = new ArrayList<>();
        for (int i = 0; i < exercises; i++) {
            numbers.add(new ArrayList<>());
        }

        for (String line : lines) {
            List<String> nums = Arrays.stream(line.split(" ")).map(String::trim).filter(str -> !str.isBlank()).toList();
            if (nums.getFirst().matches("\\d+")) {
                for (int i = 0; i < nums.size(); i++) {
                    numbers.get(i).add(Integer.valueOf(nums.get(i)));
                }
            } else {
                List<Operation> operations = nums.stream().map(Operation::of).toList();
                List<Exercise> exerciseList = new ArrayList<>();
                for (int i = 0; i < nums.size(); i++) {
                    exerciseList.add(new Exercise(numbers.get(i), operations.get(i)));
                }
                return exerciseList;
            }
        }

        return List.of();
    }

    static List<Exercise> parsePartTwo(String s) {
        Object[] array = stringToCharArrayArray(s);
        List<Exercise> exercises = new ArrayList<>();
        Exercise currentExercise = new Exercise();
        int length = ((char[]) array[0]).length;
        for (int col = length - 1; col >= 0; col--) {
            boolean addedNumber = false;
            int number = 0;
            for (Object charsO : array) {
                char[] chars = (char[]) charsO;
                if (col >= chars.length) { // Line with operations is smaller
                    continue;
                }
                char c = chars[col];
                if (c == ' ') { // found space, go on
                    continue;
                }
                if (c >= '0' && c <= '9') { // found number
                    number = number * 10 + (c - 48);
                }
                if (c == '+') {
                    currentExercise = currentExercise.withNewNumber(number);
                    exercises.add(currentExercise.withOperation(Operation.ADD));
                    currentExercise = new Exercise();
                    number = 0;
                    addedNumber = true;
                    break;
                }
                if (c == '*') {
                    currentExercise = currentExercise.withNewNumber(number);
                    exercises.add(currentExercise.withOperation(Operation.MULTIPLY));
                    currentExercise = new Exercise();
                    number = 0;
                    addedNumber = true;
                    break;
                }
            }
            if (!addedNumber && number != 0) {
                currentExercise = currentExercise.withNewNumber(number);
            }
        }

        return exercises;
    }
}

static Object[] stringToCharArrayArray(String s) {
    return s.lines().map(String::toCharArray).toArray();
}

enum Operation {
    ADD, MULTIPLY;

    static Operation of(String s) {
        if (s.equals("+")) {
            return ADD;
        } else if (s.equals("*")) {
            return MULTIPLY;
        } else {
            throw new IllegalArgumentException();
        }
    }
}

@Nested
class Day6Test {
    static Stream<Arguments> solveArguments() {
        return Stream.of(
                Arguments.of(List.of(123, 45, 6), Operation.MULTIPLY, 33210),
                Arguments.of(List.of(328, 64, 98), Operation.ADD, 490),
                Arguments.of(List.of(51, 387, 215), Operation.MULTIPLY, 4243455),
                Arguments.of(List.of(64, 23, 314), Operation.ADD, 401)
        );
    }

    @ParameterizedTest
    @MethodSource("solveArguments")
    void solveExerciseFromExample(List<Integer> numbers, Operation operation, long result) {
        // arrange
        Exercise exercise = new Exercise(numbers, operation);

        // act & assert
        assertEquals(result, exercise.solve());
    }

    @Test()
    void parseExample() {
        // arrange
        Exercise exercise1 = new Exercise(List.of(123, 45, 6), Operation.MULTIPLY);
        Exercise exercise2 = new Exercise(List.of(328, 64, 98), Operation.ADD);
        Exercise exercise3 = new Exercise(List.of(51, 387, 215), Operation.MULTIPLY);
        Exercise exercise4 = new Exercise(List.of(64, 23, 314), Operation.ADD);
        List<Exercise> exercises = List.of(exercise1, exercise2, exercise3, exercise4);

        String s = """
                123 328  51 64\s
                 45 64  387 23\s
                  6 98  215 314
                *   +   *   +""";

        // act & assert
        List<Exercise> result = Exercise.parse(s);
        for (int i = 0; i < result.size(); i++) {
            assertEquals(exercises.get(i), result.get(i));
        }
    }

    @Test()
    void parseExamplePartTwo() {
        // arrange
        Exercise exercise1 = new Exercise(List.of(4, 431, 623), Operation.ADD);
        Exercise exercise2 = new Exercise(List.of(175, 581, 32), Operation.MULTIPLY);
        Exercise exercise3 = new Exercise(List.of(8, 248, 369), Operation.ADD);
        Exercise exercise4 = new Exercise(List.of(356, 24, 1), Operation.MULTIPLY);
        List<Exercise> exercises = List.of(exercise1, exercise2, exercise3, exercise4);

        String s = """
                123 328  51 64\s
                 45 64  387 23\s
                  6 98  215 314
                *   +   *   +""";

        // act & assert
        List<Exercise> result = Exercise.parsePartTwo(s);
        for (int i = 0; i < result.size(); i++) {
            assertEquals(exercises.get(i), result.get(i));
        }
    }
}
