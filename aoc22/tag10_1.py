s = """"""

lines = s.split('\n')

cycle = 0
regX = 1
sumSignals = 0

for line in lines:
    pair = line.split(' ')
    
    if len(pair) == 1:
        cycle += 1
        if cycle % 40 == 20:
            sumSignals += cycle * regX
    elif len(pair) == 2:
        cycle += 1
        if cycle % 40 == 20:
            sumSignals += cycle * regX
        cycle += 1
        if cycle % 40 == 20:
            sumSignals += cycle * regX
        regX += int(pair[1])

print(cycle)
print(sumSignals)