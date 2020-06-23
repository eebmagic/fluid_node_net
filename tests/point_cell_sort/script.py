from matplotlib import pyplot as plt
from random import randint

# width and height NOT INCLUSIVE
def make_points(count: int, width: int, height: int):
    out = set()
    for i in range(count):
        point = (randint(0, width-1), randint(0, height-1))
        while point in out:
            point = (randint(0, width-1), randint(0, height-1))
        out.add(point)
    return out


def pointSort(points: set, width: int, height: int, resolution: int):
    buckets = []
    for y in range(resolution):
        row = []
        for x in range(resolution):
            row.append(set())
        buckets.append(row)

    w_size = int(width / resolution)
    h_size = int(height / resolution)

    print(f"w_size: {w_size}")
    print(f"h_size: {h_size}")

    for point in points:
        x, y = point
        x_cell = x // w_size
        y_cell = y // h_size

        if x_cell >= resolution:
            x_cell = resolution - 1
        
        if y_cell >= resolution:
            y_cell = resolution - 1

        buckets[y_cell][x_cell].add(point)

    return buckets


# params
width = 12
height = 12
buck_res = 5
node_count = 20

# make and sort
points = make_points(node_count, width, height)
bucks = pointSort(points, width, height, buck_res)

# printouts
total_size = 0
for row in bucks:
    for col in row:
        print(f"{len(col)}\t|", end='')
        total_size += len(col)
    print("")

print(f"total point: {len(points)}")
print(f"total in cells: {total_size}")
print(bucks[0][0])
print(bucks[-1][-1])