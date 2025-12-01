input = """
Time:        44     80     65     72
Distance:   208   1581   1050   1102
"""

# Time:        44     80     65     72
# Distance:   208   1581   1050   1102

input = [inp.split(':') for inp in input.split("\n")]
times = list(filter(lambda stri : stri.isnumeric(), [str.strip() for str in input[1]][1].split(' ')))
distances = list(filter(lambda stri : stri.isnumeric(), [str.strip() for str in input[2]][1].split(' ')))

time = int(''.join(times))
distance = int(''.join(distances))

print(time)
print(distance)

possibleDistances = []
speed = 0
for buttonPress in range(time+1):
    possibleDistances.append(speed * (time-speed))
    speed = speed + 1

print(len(list(filter(lambda dist: dist > distance, possibleDistances))))

