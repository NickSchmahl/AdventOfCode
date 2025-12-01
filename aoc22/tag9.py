class Rope:
    def __init__(self):
        self.knots = [Knot(0,0) for i in range(10)]
    
    def printMe(self):
        for i in range(len(self.knots)):
            print(self.knots[i].getPoint(), end=' ')
        print()
        
    def getPosNine(self):
        return self.knots[9]
    
    def move(self, direction):
        self.knots[0].move(direction)
        for i in range(1, len(self.knots)):
            if not self.knots[i].isBeside(self.knots[i-1]):
                self.knots[i].follow(self.knots[i-1])

class Knot:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def getX(self):
        return self.x
    
    def getY(self):
        return self.y
        
    def getPoint(self):
        return (self.x, self.y)
        
    def setPos(self, x, y):
        self.x = x
        self.y = y
        
    def move(self, direction):
        if direction == 'D':
            self.addY(-1)
        elif direction == 'U':
            self.addY(1)
        elif direction == 'L':
            self.addX(-1)
        elif direction == 'R':
            self.addX(1)
    
    def addX(self, num):
        self.x += num
    
    def addY(self, num):
        self.y += num
    
    def isBeside(self, other):
        return abs(self.x-other.x) in [0,1] and abs(self.y-other.getY()) in [0,1]
    
    def follow(self, other):
        diffX = self.x - other.x
        diffY = self.y - other.y
            
        if diffX == 2 and diffY == 2:
            self.setPos(other.x + 1, other.y + 1)
        elif diffX == -2 and diffY == -2:
            self.setPos(other.x - 1, other.y - 1)
        elif diffX == 2 and diffY == -2:
            self.setPos(other.x + 1, other.y - 1)
        elif diffX == -2 and diffY == 2:
            self.setPos(other.x - 1, other.y + 1)
        elif diffX == 2:
            self.setPos(other.x + 1, other.y)
        elif diffX == -2:
            self.setPos(other.x - 1, other.y)
        elif diffY == 2:
            self.setPos(other.x, other.y+1)
        elif diffY == -2:
            self.setPos(other.x, other.y-1)
            
lines = s.split('\n')

rope = Rope()

counts = [(0,0)]

for line in lines:
    direction, value = line.split(' ')
    value = int(value)
    
    for i in range(value):
        rope.move(direction)
        if rope.getPosNine().getPoint() not in counts:
            counts.append(rope.getPosNine().getPoint())
    
print(len(counts))

