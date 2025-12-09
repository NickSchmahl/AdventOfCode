void main() throws IOException {
    Path day4File = Path.of("Aoc25/exercises/Day4.txt");

    long password_part_one = Grid.parse(Files.readString(day4File)).accessibleRolls();
    System.out.println("Part one: " + password_part_one);

    long password_part_two = Grid.parse(Files.readString(day4File)).recursiveAccessibleRolls();
    System.out.println("Part two: " + password_part_two);
}

enum MaybePaperRoll {
    EMPTY, PAPER_ROLL;
}

record Position(int row, int col) {

}

record Grid(List<List<MaybePaperRoll>> paperRolls) {
    int getAdjacentRolls(final Position position) {
        int counter = 0;

        boolean upperRowOkay = position.row() > 0;
        boolean lowerRowOkay = position.row() < paperRolls.size() - 1;
        boolean leftColOkay = position.col() > 0;
        boolean rightColOkay = position.col() < paperRolls.getFirst().size() - 1;

        counter += upperRowOkay && leftColOkay
                && paperRolls.get(position.row() - 1).get(position.col() - 1) == MaybePaperRoll.PAPER_ROLL ?
                1 :
                0; // Up left
        counter += upperRowOkay && paperRolls.get(position.row() - 1).get(position.col()) == MaybePaperRoll.PAPER_ROLL ?
                1 :
                0; // Up
        counter += upperRowOkay && rightColOkay
                && paperRolls.get(position.row() - 1).get(position.col() + 1) == MaybePaperRoll.PAPER_ROLL ?
                1 :
                0; // Up right
        counter += leftColOkay && paperRolls.get(position.row()).get(position.col() - 1) == MaybePaperRoll.PAPER_ROLL ?
                1 :
                0; // Left
        counter += rightColOkay && paperRolls.get(position.row()).get(position.col() + 1) == MaybePaperRoll.PAPER_ROLL ?
                1 :
                0; // Right
        counter += lowerRowOkay && leftColOkay
                && paperRolls.get(position.row() + 1).get(position.col() - 1) == MaybePaperRoll.PAPER_ROLL ?
                1 :
                0; // Down left
        counter += lowerRowOkay && paperRolls.get(position.row() + 1).get(position.col()) == MaybePaperRoll.PAPER_ROLL ?
                1 :
                0; // Down
        counter += lowerRowOkay && rightColOkay
                && paperRolls.get(position.row() + 1).get(position.col() + 1) == MaybePaperRoll.PAPER_ROLL ?
                1 :
                0; // Down right

        return counter;
    }

    int accessibleRolls() {
        int counter = 0;
        for (int row = 0; row < paperRolls.size(); row++) {
            for (int col = 0; col < paperRolls.getFirst().size(); col++) {
                MaybePaperRoll paperRoll = paperRolls.get(row).get(col);
                if (paperRoll == MaybePaperRoll.EMPTY) {
                    continue;
                }

                counter += getAdjacentRolls(new Position(row, col)) < 4 ? 1 : 0;
            }
        }
        return counter;
    }

    List<Position> getAccessibleRollPositions() {
        final List<Position> positions = new ArrayList<>();
        for (int row = 0; row < paperRolls.size(); row++) {
            for (int col = 0; col < paperRolls.getFirst().size(); col++) {
                MaybePaperRoll paperRoll = paperRolls.get(row).get(col);
                if (paperRoll == MaybePaperRoll.EMPTY) {
                    continue;
                }

                Position position = new Position(row, col);
                boolean isLiftable = getAdjacentRolls(position) < 4;
                if (isLiftable) {
                    positions.add(position);
                }
            }
        }
        return positions;
    }

    void removePaperRolls(final List<Position> positions) {
        for (final Position position : positions) {
            paperRolls.get(position.row).set(position.col, MaybePaperRoll.EMPTY);
        }
    }

    int recursiveAccessibleRolls() {
        List<Position> positions = getAccessibleRollPositions();
        int counter = 0;

        while (!positions.isEmpty()) {
            counter += positions.size();
            removePaperRolls(positions);
            positions = getAccessibleRollPositions();
        }

        return counter;
    }

    static Grid parse(String s) {
        return new Grid(s.lines().map(Grid::parseLine).toList());
    }

    static List<MaybePaperRoll> parseLine(String s) {
        return new ArrayList(s.chars().boxed()
                .map(character -> switch (character.byteValue()) {
                    case '.' -> MaybePaperRoll.EMPTY;
                    case '@' -> MaybePaperRoll.PAPER_ROLL;
                    default -> throw new IllegalArgumentException();
                }).toList());
    }
}
