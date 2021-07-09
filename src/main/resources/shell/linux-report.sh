#!/bin/bash

function install_software() {
  if ! command -v apt-get &> /dev/null
  then
    if ! command -v curl &> /dev/null
    then
      yum install curl -y
    fi
    if ! commad -v crontab &> /dev/null
    then
      yum install crontabs -y
    fi
  else
    if ! command -v curl &> /dev/null
    then
      apt-get install curl -y
    fi
    if ! commad -v crontab &> /dev/null
    then
      apt-get install crontabs -y
    fi
  fi
}

function report_ip() {
  local text
  local files=(/sys/class/net/*)
  local pos=$(( ${#files[*]} - 1 ))
	local last=${files[$pos]}

  text="["

  for item in "${files[@]}"
  do
      interface=$(basename "$item")
      text=$text"{\"\": #id ,\"interfaceName\" : \"$interface\", \"ip\" : \"$( ifconfig "$interface" | grep "inet " | awk '{print $2}')\"}"
      if [[ ! $item == "$last" ]]
      then
        text="$text,"
      fi
  done
  text=$text"]"

  curl -d "$text" -H "Content-Type: application/json" -X POST http://#masterIp:#masterPort/ip/report
}

fnCalled="$1"

if [ -n "$(type -t "$fnCalled")" ] && [ "$(type -t "$fnCalled")" = function ]
then
  # 上报ip
  ${fnCalled}
else
  echo "安装必要软件"
  # 安装必要软件
  install_software
  echo "添加定时上报任务"
  # 移除定时任务
  crontab -l | grep -v 'bash ~/.hokage/server-report.sh' | crontab -
  # 添加定时任务
  (crontab -l 2> /dev/null); echo "*/1 * * * * bash ~/.hokage/server-report.sh" | crontab -
  echo "完成初始化"
fi
