#!/bin/bash

start=$(date +"%s")

ssh $2@$1 -i key.txt -o StrictHostKeyChecking=no << ENDSSH
docker pull cosmopk/cosmo-backend:latest

CONTAINER_NAME=cosmo-backend
if [ "\$(docker ps -qa -f name=\$CONTAINER_NAME)" ]; then
    if [ "\$(docker ps -q -f name=\$CONTAINER_NAME)" ]; then
        echo "Container is running -> stopping it..."
        docker stop \$CONTAINER_NAME;
    fi
fi

docker run -d --rm -p 8000:8000 --name \$CONTAINER_NAME cosmopk/cosmo-backend:latest

exit
ENDSSH


if [ $? -eq 0 ]; then
  exit 0
else
  exit 1
fi

end=$(date +"%s")

diff=$(($end - $start))

echo "Deployed in : ${diff}s"
