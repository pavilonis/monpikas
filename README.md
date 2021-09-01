## Server setup on Ubuntu 20.04

Run commands:  
```
sudo apt-get update
sudo apt-get upgrade
```

Install [Docker](https://docs.docker.com/engine/install/ubuntu/)  
Install [Docker Compose](https://docs.docker.com/compose/install/)

Create directory `/opt/monpikas` and put [docker-compose.yml](docker-compose.yml) there  

Inside `/opt/monpikas` directory run command `docker-compose up -d`  
Use command `docker exec -it mysql8 bin/bash` to login to mysql8 container  
Login into mysql server and create database `monpikas`  

Stop and start containers again using  
`docker-compose down`  
`docker-compose up`

Use `-d` (daemon) to start process in background  

OPTIONAL - Install Apache HTTP server for 80 -> 8080 port redirection
```
sudo apt-get install apache2
sudo a2enmod proxy
sudo a2enmod proxy_http
```

`sudo nano /etc/apache2/sites-available/000-default.conf`  
Add these lines under the "VirtualHost" brackets:
```
ProxyRequests Off
ProxyPreserveHost On
ProxyPass / http://localhost:8080/
ProxyPassReverse / http://localhost:8080/
```
Restart Apache:
`sudo service apache2 restart`