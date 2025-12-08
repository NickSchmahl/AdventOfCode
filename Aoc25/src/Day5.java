void main() throws IOException {
    Path day5File = Path.of("Aoc25/exercises/Day5.txt");

    String text = Files.readString(day5File);
    List<Range> ranges = text.lines().map(Range::parse).filter(Objects::nonNull).toList();
    List<Long> numbers = findNumbers(text);

    long password_part_one = numberOfFreshIds(numbers, ranges);
    System.out.println("Part one: " + password_part_one);

    var part_two = Range.recursiveMerge(ranges).stream().map(Range::length).reduce(0L, Long::sum);
    System.out.println("Part two: " + part_two);
}

List<Long> findNumbers(String s) {
    return s.lines().map(line -> {
                try {
                    return Long.parseLong(line);
                } catch (NumberFormatException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .toList();
}

long numberOfFreshIds(List<Long> ids, List<Range> ranges) {
    return ids.stream()
            .filter(id -> ranges.stream().anyMatch(range -> range.isWithin(id)))
            .count();
}

record Range(long start, long end) {
    boolean isWithin(long num) {
        return num >= start && num <= end;
    }

    long length() {
        return end - start + 1;
    }

    static Range parse(final String s) {
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

    Range merge(Range range) {
        long start, end;
        if (this.isWithin(range.start) || this.isWithin(range.end) || range.isWithin(this.start) || range.isWithin(this.end)) {
            start = Math.min(this.start, range.start);
            end = Math.max(this.end, range.end);
            return new Range(start, end);
        }
        return null;
    }

    static List<Range> recursiveMerge(final List<Range> ranges) {
        List<Range> oldRanges = ranges;
        List<Range> newRanges = Range.merge(ranges);
        while (!oldRanges.equals(newRanges)) {
            oldRanges = newRanges;
            newRanges = Range.merge(newRanges);
        }
        return newRanges;
    }

    static List<Range> merge(final List<Range> ranges) {
        final List<Range> mergedRanges = new ArrayList<>();
        List<Range> rangesToBeMerged = ranges;
        while (!rangesToBeMerged.isEmpty()) {
            Range rangeToMerge = rangesToBeMerged.getFirst();
            final List<Range> mergeResults = new ArrayList<>();
            // Merge one by one
            for (Range otherRange : rangesToBeMerged) {
                Range newRange = rangeToMerge.merge(otherRange);
                if (newRange != null) {
                    rangeToMerge = newRange;
                }
                mergeResults.add(newRange);
            }
            mergedRanges.add(rangeToMerge);
            if (mergeResults.stream().allMatch(Objects::nonNull)) {
                break;
            }

            assert rangesToBeMerged.size() == mergeResults.size();
            final List<Range> tmp = new ArrayList<>();
            for (int i = 1; i < mergeResults.size(); i++) {
                if (mergeResults.get(i) == null) {
                    tmp.add(rangesToBeMerged.get(i));
                }
            }
            rangesToBeMerged = tmp;
        }

        return mergedRanges;
    }
}
