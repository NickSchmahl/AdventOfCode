input = """
Time:        44     80     65     72
Distance:   208   1581   1050   1102
"""

# Time:        44     80     65     72
# Distance:   208   1581   1050   1102

input = [inp.split(':') for inp in input.split("\n")]
times = list(filter(lambda stri : stri.isnumeric(), [str.strip() for str in input[1]][1].split(' ')))
distances = list(filter(lambda stri : stri.isnumeric(), [str.strip() for str in input[2]][1].split(' ')))

times = list(map(lambda time : int(time), times))
distances = list(map(lambda time : int(time), distances))

races = []
for race in range(len(times)):
    raceLen = times[race]
    speed = 0
    possibleDistances = []
    for buttonPress in range(raceLen+1):
        possibleDistances.append(speed * (raceLen-speed))
        speed = speed + 1
    races.append(possibleDistances)
print(distances)
print(races)
print('\n')
filteredRaces = []
for index, race in enumerate(races):    
    filteredRaces.append(len(list(filter(lambda distance: distance > distances[index], race))))

print(filteredRaces)

result = 1
for race in filteredRaces:
    result = result * race

print(result)
