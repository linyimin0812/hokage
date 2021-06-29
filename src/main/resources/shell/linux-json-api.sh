#!/bin/bash

export PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin:/root/bin

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

system_status() {

  local load_avg
  load_avg=$(cat /proc/loadavg | awk '{print "{\"oneMinAverage\": "$1", \"fiveMinAverage\": "$2", \"fifteenMinAverage\": "$3"}"}')
  local mem_status
  mem_status=$(free -b | grep "Mem:" | awk '{print "{\"total\": "$2", \"used\": "$3", \"free\": "$4"}"}')
  local pre_cpu_status
  pre_cpu_status=$(cat /proc/stat | grep "cpu" | awk 'BEGIN {print "["} {print "{\"name\": \""$1"\", \"user\": "$2", \"nice\": "$3", \"system\": "$4", \"idle\": "$5", \"ioWait\": "$6", \"irq\": "$7", \"softIrq\": "$8", \"steal\": "$9", \"guest\": "$10", \"guestNice\": "$11" },"} END {print "]"}' | sed 'N;$s/},/}/;P;D';)

	local files=(/sys/class/net/*)
	local pos=$(( ${#files[*]} - 1 ))
	local last=${files[$pos]}

	local upload_json_output="{"
	local download_json_output="{"

	for interface in "${files[@]}"
	do
		basename=$(basename "$interface")
		out1=$(cat /sys/class/net/"$basename"/statistics/tx_bytes)
		in1=$(cat /sys/class/net/"$basename"/statistics/rx_bytes)
		sleep 1
		out2=$(cat /sys/class/net/"$basename"/statistics/tx_bytes)
		in2=$(cat /sys/class/net/"$basename"/statistics/rx_bytes)
		out_bytes=$((out2 - out1))
		in_bytes=$((in2 - in1))
		upload_json_output="$upload_json_output \"$basename\": $out_bytes"
		download_json_output="$download_json_output \"$basename\": $in_bytes"
		if [[ ! $interface == "$last" ]]
		then
			upload_json_output="$upload_json_output,";
			download_json_output="$download_json_output,";
		fi
	done
	upload_json_output="$upload_json_output}"
	download_json_output="$download_json_output}"

	local cur_cpu_status
	cur_cpu_status=$(cat /proc/stat | grep "cpu" | awk 'BEGIN {print "["} {print "{\"name\": \""$1"\", \"user\": "$2", \"nice\": "$3", \"system\": "$4", \"idle\": "$5", \"ioWait\": "$6", \"irq\": "$7", \"softIrq\": "$8", \"steal\": "$9", \"guest\": "$10", \"guestNice\": "$11" },"} END {print "]"}' | sed 'N;$s/},/}/;P;D';)

	echo "{\"uploadRate\": $upload_json_output, \"downloadRate\": $download_json_output, \"loadAvg\": $load_avg, \"memStat\": $mem_status, \"preCpuStat\": $pre_cpu_status, \"curCpuStat\": $cur_cpu_status}"
}

fnCalled="$1"

if [ -n "$(type -t $fnCalled)" ] && [ "$(type -t $fnCalled)" = function ]; then
    ${fnCalled}
fi