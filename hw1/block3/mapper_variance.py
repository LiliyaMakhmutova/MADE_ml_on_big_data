#!/usr/bin/env python

import sys
from statistics import mean, variance

prices = []
count = 0

for line in sys.stdin: 
    try:
        line = line.strip()
        words = line.split(",")
        price = int(words[9])
    except:
        continue
    else:
        prices.append(price)
        count+=1    
        
  
print(count, mean(prices), variance(prices))