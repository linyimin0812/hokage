#!/bin/bash

general_info() {

  function formatTime {
    local T=$1
    local D=$((T/60/60/24))
    local H=$((T/60/60%24))
    local M=$((T/60%60))
    local S=$((T%60))
    [[ $D -gt 0 ]] && printf '%d days ' $D
    [[ $H -gt 0 ]] && printf '%d hours ' $H
    [[ $M -gt 0 ]] && printf '%d minutes ' $M
    [[ $D -gt 0 || $H -gt 0 || $M -gt 0 ]] && printf 'and '
    printf '%d seconds' $S
  }

  local kernel
  local hostname
  local uptime_seconds
  local server_time

  kernel=$(uname -s -r -o | awk '{print "\"kernel name\": \""$1"\", \"kernel release\": \""$2"\", \"os\": \""$3"\""}')
  hostname=$(hostname)
  uptime_seconds=$(cat /proc/uptime | awk '{print $1}')
  server_time=$(date '+%F %T' )

  echo "{ $kernel, \"hostname\": \"$hostname\", \"uptime\": \" $(formatTime "${uptime_seconds%.*}") \", \"Server Time\": \"$server_time\" }"
}

interface_ip() {

  local text
  text="["

  for item in $(ifconfig | grep -oP "^[a-zA-Z0-9:]*(?=:)")
  do
      text=$text"{\"interfaceName\" : \"$item\", \"ip\" : \"$( ifconfig "$item" | grep "inet " | awk '{print $2}')\"},"
  done
  text=$(echo "$text" | awk '{print substr($0, 0, length($0)-1)}')
  text=$text"]"
  echo "$text"
}

fnCalled="$1"

# Check if the function call is indeed a function.
if [ -n "$(type -t $fnCalled)" ] && [ "$(type -t $fnCalled)" = function ]; then
    ${fnCalled}
fi