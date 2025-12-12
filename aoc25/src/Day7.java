void main() throws IOException {
    Path day7File = Path.of("Aoc25/exercises/Day7.txt");

    String text = Files.readString(day7File);
    Grid grid = Grid.parse(text);
    TachyonManifold manifold = new TachyonManifold(grid, new ArrayList<>());
    Game game = new Game(manifold);
    List<StepResult> results = game.play();
    System.out.println("Part one: " + results.stream().map(StepResult::splits).reduce(0, Integer::sum));

    System.out.println("Part two: " + game.playQuantumCount());
}

enum Marker {
    START, EMPTY, SPLITTER, BEAM;

    static Marker of(char c) {
        return switch (c) {
            case '.' -> EMPTY;
            case '^' -> SPLITTER;
            case 'S' -> START;
            default -> throw new IllegalArgumentException();
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case START -> "S";
            case EMPTY -> ".";
            case SPLITTER -> "^";
            case BEAM -> "|";
        };
    }
}

record Grid(List<List<Marker>> markers) {
    Marker getMarker(Position pos) {
        try {
            return markers.get(pos.row()).get(pos.col());
        } catch (IndexOutOfBoundsException exception) {
            return null;
        }
    }

    Stream<Position> positions() {
        List<Position> positionList = new LinkedList<>();
        for (int i = 0; i < markers.size(); i++) {
            for (int j = 0; j < markers.get(i).size(); j++) {
                positionList.add(new Position(i, j));
            }
        }
        return positionList.stream();
    }

    Position findStart() {
        return positions().filter(position -> getMarker(position) == Marker.START).findFirst().orElseThrow();
    }

    void markAsBeam(Position position) {
        markers.get(position.row()).set(position.col(), Marker.BEAM);
    }

    static Grid parse(final String s) {
        final List<List<Marker>> returnList = new LinkedList<>();
        s.lines().forEach(line -> {
            List<Marker> markerRow = new LinkedList<>();
            returnList.add(markerRow);
            for (char c : line.toCharArray()) {
                if (c == '\n') {
                    break;
                }
                markerRow.add(Marker.of(c));
            }
        });
        return new Grid(returnList);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (List<Marker> line : markers) {
            for (Marker marker : line) {
                stringBuilder.append(marker);
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}

record Position(int row, int col) {
    Position down() {
        return new Position(this.row + 1, this.col);
    }

    Position right() {
        return new Position(this.row, this.col + 1);
    }

    Position left() {
        return new Position(this.row, this.col - 1);
    }
}

record StepResult(TachyonManifold manifold, int splits) {

}

record Game(TachyonManifold manifold) {
    List<StepResult> play() {
        start();
        final List<StepResult> results = new LinkedList<>();
        while (manifold.beams.stream().anyMatch(beam -> !beam.completed)) {
            StepResult newManifold = manifold.step();
//            System.out.println(newManifold.manifold.playingGrid());
//            System.out.println("Splits: " + newManifold.splits);
//            System.out.println("Completed beams: " + newManifold.manifold.beams.stream().filter(Beam::completed).count());
//            System.out.println("Total beams: " + newManifold.manifold.beams.size());
//            System.out.println();
//            System.out.println();
            results.add(newManifold);
        }
        return results;
    }

    List<TachyonManifold> playQuantum() {
        start();
        final List<TachyonManifold> completedGames = new ArrayList<>();
        final List<TachyonManifold> queue = new ArrayList<>();
        queue.add(manifold);
        while (!queue.isEmpty()) {
            TachyonManifold first = queue.removeFirst();
            List<TachyonManifold> newManifolds = first.quantumStep();
            if (newManifolds == null) {
                completedGames.add(first);
                continue;
            }
            queue.addAll(newManifolds);
        }
        return completedGames;
    }

    int playQuantumCount() {
        start();
        int counter = 0;
        final List<TachyonManifold> queue = new ArrayList<>();
        queue.add(manifold);
        while (!queue.isEmpty()) {
            TachyonManifold first = queue.removeFirst();
            List<TachyonManifold> newManifolds = first.quantumStep();
            if (newManifolds == null) {
                counter++;
                if (counter % 100000 == 0) {
                    System.out.println(counter);
                }
                continue;
            }
            queue.addAll(0, newManifolds);
        }
        return counter;
    }

    void start() {
        final Position start = manifold.grid.findStart();
        manifold.beams.add(new Beam(start.down()));
    }
}

record TachyonManifold(Grid grid, List<Beam> beams) {
    static TachyonManifold of(Grid grid) {
        return new TachyonManifold(grid, new LinkedList<>());
    }

    Grid playingGrid() {
        Grid returnGrid = new Grid(grid.markers);
        beams.stream().flatMap(beam -> beam.positions().stream()).forEach(returnGrid::markAsBeam);
        return returnGrid;
    }

    StepResult step() {
        int splits = 0;
        List<Beam> newBeams = new LinkedList<>();
        Set<Position> newPositions = new HashSet<>();
        for (Beam beam : beams) {
            if (beam.completed) {
                continue;
            }
            Position down = beam.positions.getLast().down();
            if (grid.getMarker(down) == null) {
                beam.completed = true;
            }
            if (grid.getMarker(down) == Marker.SPLITTER) {
                beam.completed = true;
                Position downLeft = down.left();
                Position downRight = down.right();
                if (!newPositions.contains(downLeft)) {
                    newPositions.add(downLeft);
                    newBeams.add(new Beam(downLeft));
                }
                if (!newPositions.contains(downRight)) {
                    newPositions.add(downRight);
                    newBeams.add(new Beam(downRight));
                }
                splits++;
            }
            if (grid.getMarker(down) == Marker.EMPTY) {
                if (newPositions.contains(down)) {
                    beam.completed = true;
                } else {
                    newPositions.add(down);
                    beam.positions.add(down);
                }
            }
        }
        List<Beam> returnedBeams = new LinkedList<>(this.beams);
        returnedBeams.addAll(newBeams);
        this.beams.addAll(newBeams);
        return new StepResult(new TachyonManifold(this.grid, returnedBeams), splits);
    }

    List<TachyonManifold> quantumStep() {
        List<Beam> uncompletedBeams = beams.stream().filter(beam -> !beam.completed).toList();
        assert uncompletedBeams.size() == 1;

        Beam beam = uncompletedBeams.getFirst();
        Position lastPosition = beam.positions.getLast();
        Position newPosition = lastPosition.down();
        if (grid.getMarker(newPosition) == Marker.EMPTY) {
            beam.positions.add(newPosition);
            return List.of(this);
        }
        else if (grid.getMarker(newPosition) == Marker.SPLITTER) {
            beam.completed = true;
            Beam leftBeam = new Beam(newPosition.left());
            List<Beam> leftBeams = new ArrayList<>(beams);
            leftBeams.add(leftBeam);
            Beam rightBeam = new Beam(newPosition.right());
            List<Beam> rightBeams = new ArrayList<>(beams);
            rightBeams.add(rightBeam);

            return List.of(new TachyonManifold(grid, leftBeams), new TachyonManifold(grid, rightBeams));
        }
        else if (grid.getMarker(newPosition) == null) {
            return null;
        }
        else {
            throw new RuntimeException("How?");
        }
    }
}

static final class Beam {
    private final List<Position> positions = new LinkedList<>();
    private boolean completed = false;

    Beam(List<Position> positions) {
        this.positions.addAll(positions);
    }

    Beam(Position position) {
        this.positions.add(position);
    }

    public List<Position> positions() {
        return positions;
    }

    public boolean completed() {
        return completed;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (Beam) obj;
        return Objects.equals(this.positions, that.positions) &&
                this.completed == that.completed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positions, completed);
    }

    @Override
    public String toString() {
        return "Beam[" +
                "positions=" + positions + ", " +
                "completed=" + completed + ']';
    }

}
