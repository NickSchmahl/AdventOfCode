void main() throws IOException {
    Path day2File = Path.of("Aoc25/exercises/Day2.txt");
    long passwort_part_one = IdChecker.findeInvalidIdsFromStream(
                    IdParser.parse(Files.readString(day2File)
                    ))
            .stream()
            .map(Long::parseLong)
            .reduce(0L, Long::sum);
    System.out.println("Part one: " + passwort_part_one);

    long passwort_part_two = IdChecker.findeInvalidIdsFromStream2(
                    IdParser.parse(Files.readString(day2File)
                    ))
            .stream()
            .map(Long::parseLong)
            .reduce(0L, Long::sum);
    System.out.println("Part two: " + passwort_part_two);
}

static class IdChecker {
    static boolean isRepeated(final String s, int substringSize) {
        boolean foundRepetition = true;
        if (substringSize == 0 || s.length() % substringSize != 0) return false;
        final String substringToCompare = s.substring(0, substringSize);
        for (int substringStart = substringSize; substringStart < s.length() - substringSize + 1; substringStart+=substringSize) {
            final String substring = s.substring(substringStart, substringStart + substringSize);
            if (!substring.equals(substringToCompare)) {
                foundRepetition = false;
                break;
            }
        }
        return foundRepetition;
    }

    static boolean isRepeated(final String s) {
        return IntStream.range(1, s.length()).anyMatch(substringSize -> isRepeated(s, substringSize));
    }

    static List<String> findeInvalidIds(final Stream<String> ids) {
        return ids.filter(id -> isRepeated(id, id.length() / 2)).toList();
    }

    static List<String> findeInvalidIds2(final Stream<String> ids) {
        return ids.filter(IdChecker::isRepeated).toList();
    }

    static List<String> findeInvalidIdsFromStream(final Stream<Range> ranges) {
        return findeInvalidIds(ranges.flatMap(Range::toLongStream).map(Object::toString));
    }

    static List<String> findeInvalidIdsFromStream2(final Stream<Range> ranges) {
        return findeInvalidIds2(ranges.flatMap(Range::toLongStream).map(Object::toString));
    }
}

static class IdParser {
    static Stream<Range> parse(String s) {
        return Arrays.stream(s.split(",")).map(Range::of);
    }
}

record Range(long start, long ende) {
    static Range of(final String s) {
        final var pattern = Pattern.compile("(\\d+)-(\\d+)");
        final var matcher = pattern.matcher(s);
        if (matcher.find()) {
            return new Range(
                    Long.parseLong(matcher.group(1)),
                    Long.parseLong(matcher.group(2))
            );
        } else {
            return null;
        }
    }

    Stream<Long> toLongStream() {
        return LongStream.range(start, ende + 1).boxed();
    }
}
