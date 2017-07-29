## A program for training a Word2Vec model based on news agency corpus (or any corpus of texts given as a set of docunments).

### Usage:
 
 After chosing your language of preference (Java or Python) please follow the instructions below: 
 
* Java branch: 
 * If you prefer to build the image, you must build the Java project using Apache Maven (install Maven if necessary) with `mvn clean package` then build the dokcer image with 
 `sudo docker-compose build`. 
 * For basic configuration see **conf/word2vec-default.properties** file.

* Python branch: please have a look at **conf/default-conf.yml**.

For starting the traning process run the following in shell

`
$ sudo docker-compose up -d
`
### Notice:

For running the program you should have prepared a text file conntaing the docunments where each line designates  a (clean) document.

