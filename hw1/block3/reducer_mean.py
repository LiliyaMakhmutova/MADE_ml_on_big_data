#!/usr/bin/env python

from statistics import mean
import sys

counts = []
means = []

for line in sys.stdin:
    if line == "":
        break
    line = line.strip()
    count, mean = line.split(' ')

    try:
        count = int(count)
        mean = float(mean)    
    except ValueError:    
        continue
    else:
        counts.append(count)
        means.append(mean)
    
weighted_sum = 0
counts_sum = 0
for count, mean in zip(counts, means):
    weighted_sum += count * mean
    counts_sum += count


print(weighted_sum / counts_sum)
