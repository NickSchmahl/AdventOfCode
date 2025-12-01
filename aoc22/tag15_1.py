example = """Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3"""

test = """Sensor at x=3797530, y=3451192: closest beacon is at x=3316341, y=3328308
Sensor at x=3779164, y=33938: closest beacon is at x=4608350, y=708806
Sensor at x=1331810, y=3260896: closest beacon is at x=2075597, y=3280016
Sensor at x=393374, y=696899: closest beacon is at x=2021690, y=453306
Sensor at x=2928048, y=923094: closest beacon is at x=2021690, y=453306
Sensor at x=2386726, y=3645023: closest beacon is at x=2075597, y=3280016
Sensor at x=1900159, y=2381031: closest beacon is at x=1649961, y=2000000
Sensor at x=2601378, y=2979844: closest beacon is at x=2218962, y=2701963
Sensor at x=2254818, y=32199: closest beacon is at x=2021690, y=453306
Sensor at x=2689643, y=375840: closest beacon is at x=2021690, y=453306
Sensor at x=909141, y=2842547: closest beacon is at x=2218962, y=2701963
Sensor at x=3915731, y=2454320: closest beacon is at x=4268501, y=1853073
Sensor at x=1693574, y=1344104: closest beacon is at x=1649961, y=2000000
Sensor at x=1760260, y=3297662: closest beacon is at x=2075597, y=3280016
Sensor at x=1909567, y=3990737: closest beacon is at x=2075597, y=3280016
Sensor at x=2097863, y=3179766: closest beacon is at x=2075597, y=3280016
Sensor at x=3100489, y=3623847: closest beacon is at x=3104748, y=4102403
Sensor at x=2746023, y=2432826: closest beacon is at x=2218962, y=2701963
Sensor at x=3031245, y=3031354: closest beacon is at x=3316341, y=3328308
Sensor at x=277094, y=1999350: closest beacon is at x=1649961, y=2000000
Sensor at x=1763269, y=126349: closest beacon is at x=2021690, y=453306
Sensor at x=3287624, y=2695420: closest beacon is at x=3316341, y=3328308
Sensor at x=2371102, y=1745103: closest beacon is at x=1649961, y=2000000
Sensor at x=3553438, y=1563379: closest beacon is at x=4268501, y=1853073
Sensor at x=1529129, y=2735122: closest beacon is at x=2218962, y=2701963
Sensor at x=2826220, y=3958350: closest beacon is at x=3104748, y=4102403
Sensor at x=3999334, y=3912693: closest beacon is at x=3104748, y=4102403
Sensor at x=240430, y=3829436: closest beacon is at x=-742036, y=3963149
Sensor at x=3455748, y=3814861: closest beacon is at x=3316341, y=3328308"""


class Sensor:
    def __init__(self, x, y, beacon):
        self.x = x
        self.y = y
        self.beacon = beacon

    def __str__(self):
        return f"Sensor at x={self.x}, y={self.y}: closest beacon is at x={self.beacon.x}, y={self.beacon.y}"

    def distanceToBeacon(self):
        return abs(self.x-self.beacon.x) + abs(self.y - self.beacon.y)

    def impossibleBeaconsAt(self, y):
        diff = self.distanceToBeacon() - abs(self.y - y)
        if diff > 0:
            return set([x for x in range(self.x-diff, self.x+diff)])
        else:
            return set()


class Beacon:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __str__(self):
        return f"Beacon at x={self.x}, y={self.y}"


lines = test.split('\n')
sensors = []
beacons = []

for line in lines:
    startX = line.index('=')
    endX = line.index(',')
    startY = line.index('=', endX)
    endY = line.index(':')

    bstartX = line.index('=', endY)
    bendX = line.index(',', bstartX)
    bstartY = line.index('=', bendX)

    beacon = Beacon(int(line[bstartX+1: bendX]), int(line[bstartY+1:]))
    if beacon not in beacons:
        beacons.append(beacon)

    sensor = Sensor(int(line[startX+1:endX]), int(line[startY+1: endY]), beacon)
    sensors.append(sensor)

# y = 10 # for example
y = 2000000 # for test

impossibleAtY = set()
for sensor in sensors:
    impossibleAtY |= sensor.impossibleBeaconsAt(y)

print(len(impossibleAtY))