#!/usr/bin/env bash

if [ $# -ne 1 ]; then
  echo '[ERROR] please add your macro type and condig file password as args e.g) ./run_docker.sh {type}'
  exit 1
fi

type=$1
echo '[INFO] type:' $type

# build docker
./gradlew clean :dockerBuild -Dtype=$type

# run docker
sudo docker run -d -p 4444:4444 -p 11619:11619 --shm-size=128m -v /tmp/logs:/tmp/logs --name macro_app macro 

