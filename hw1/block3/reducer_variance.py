#!/usr/bin/env python

import sys

current_cmv = None

for line in sys.stdin:    
    try:
        line = line.strip()
        count, mean, variance = line.split(' ')
        count = int(count)
        mean = float(mean)
        variance = float(variance)        
    except:    
        continue
    else:
        if current_cmv:
            # count & update var
            current_count = current_cmv[0]
            current_mean = current_cmv[1]
            current_variance = current_cmv[2]            
            current_variance = (current_count*current_variance + count*variance)/(current_count+count) + (current_count*count)*((current_mean-mean)/(current_count+count))**2
            current_mean = (current_count*current_mean + count*mean)/(current_count+count)
            current_count = current_count + count
            current_cmv = (current_count, current_mean, current_variance)
        else:
            current_cmv = (count, mean, variance)


print(current_cmv[2])