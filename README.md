scala-cassandra
===============

Cassandra library in Scala using Hector. Basic ORM support using Hector.

Install Cassandra on Ubuntu
---------------------------

sudo apt-get update
sudo gedit /etc/apt/sources.list

Add:
deb http://www.apache.org/dist/cassandra/debian 10x main  
deb-src http://www.apache.org/dist/cassandra/debian 10x main  

sudo apt-get update

gpg --keyserver wwwkeys.pgp.net --recv-keys <KEY_FROM_ABOVE>  
sudo apt-key add ~/.gnupg/pubring.gpg  
sudo apt-get update  

sudo apt-get install cassandra

sudo cassandra -f

1. Kill cassandra pid(s)
2. Start cassandra: sudo /etc/init.d/cassandra start

Use the Cassandra CLI
---------------------

cassandra-cli -host localhost -port 9160

CREATE KEYSPACE demo
USE demo;
CREATE COLUMN FAMILY users;
LIST users;

TODOs
-----
1. Create keyspace and create column-family from models
2. Make implicit definition of EntityManagerImpl reach out better