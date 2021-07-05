#!/bin/bash

# add crontab task
(crontab -l 2> /dev/null; echo "*/ * * * * /path/to/job -wth args") | crontab -
# remove crontab task
crontab - l | grep -v '/path/to/job -wth args' | crontab -

# 上报ip

# 上报metric

IFS=',' read -r -a ipList <<< "${ipList}"
for ip in "${ipList[@]}"
do
  echo "$ip"
done
