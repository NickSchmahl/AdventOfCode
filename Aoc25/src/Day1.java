public static final int MAX_DISTANCE = 1000;
public static final int DIAL_SIZE = 100;

void main() throws IOException {
    Dial dial_1 = Dial.initialDial();

    Path day1File = Path.of("exercises/Day1.txt");
    long passwort_part_one = Files.readString(day1File)
            .lines()
            .map(Rotation::parse)
            .map(rotation -> {
                dial_1.rotate(rotation);
                return dial_1.position;
            })
            .filter(Position::istZero)
            .count();

    System.out.println("Part one: " + passwort_part_one);

    Dial dial_2 = new Dial(50);
    long passwort_part_two = Files.readString(day1File)
            .lines()
            .map(Rotation::parse)
            .map(dial_2::rotate)
            .reduce(0, Integer::sum);
    System.out.println("Part two: " + passwort_part_two);
}

static class Dial {
    Position position;

    Dial(int position) {
        this(new Position(position));
    }

    Dial(Position position) {
        this.position = position;
    }

    static Dial initialDial() {
        return new Dial(50);
    }

    int rotate(Rotation rotation) {
        Distance distance = rotation.distance;
        // Return rotations past 0
        System.out.println(this);
        System.out.println(rotation);
        int rotations = rotation.direction == Direction.RIGHT
                ? position.rotationsPast0FromAdd(distance)
                : position.rotationsPast0FromSubtract(distance);
        System.out.println(" -> " + rotations + " -> ");
        // Change position
        this.position = rotation.direction == Direction.RIGHT
                ? position.add(distance) : position.subtract(distance);
        return rotations;
    }

    @Override
    public String toString() {
        return "Dial[position" + position + "]";
    }
}

record Rotation(Direction direction, Distance distance) {
    static Rotation parse(String s) {
        Distance distance = new Distance(Integer.parseInt(s.substring(1)));
        return s.startsWith("L")
                ? new Rotation(Direction.LEFT, distance)
                : new Rotation(Direction.RIGHT, distance);
    }


    @Override
    public String toString() {
        return String.format(
                "%s%s",
                direction == Direction.RIGHT ? "R" : "L",
                distance.distance
                );
    }

}

enum Direction { LEFT, RIGHT }

record Distance(int distance) {
    Distance {
        if (distance < 0) {
            throw new IllegalArgumentException("Invalid distance: " + distance);
        }
    }
}

record Position(int position) {
    Position {
        if (position < 0 || position > DIAL_SIZE - 1) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    Position add(Distance distance) {
        return new Position((this.position + distance.distance) % DIAL_SIZE);
    }

    int rotationsPast0FromAdd(Distance distance) {
        return (this.position + distance.distance) / DIAL_SIZE;
    }

    Position subtract(Distance distance) {
        return new Position((this.position + MAX_DISTANCE - distance.distance) % DIAL_SIZE);
    }

    int rotationsPast0FromSubtract(Distance distance) {
        boolean positionWasZero = this.position == 0;
        int fullRotations = distance.distance / DIAL_SIZE;
        int rawSubtract = this.position - (distance.distance % DIAL_SIZE);
        return (rawSubtract <= 0 ? fullRotations + 1 : fullRotations) - (positionWasZero ? 1 : 0);
    }

    boolean istZero() {
        return position == 0;
    }
}

