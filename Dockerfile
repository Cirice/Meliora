FROM ubuntu:trusty 

MAINTAINER "Hossein Abedi" 


############################################################
# Setup user
############################################################

USER root

############################################################
# Setup environment variables
############################################################

ENV HOME_DIR /home

############################################################
# Creating directories/folders
############################################################

RUN mkdir -p $HOME_DIR/word2vec
ADD . $HOME_DIR/word2vec

############################################################
# Updating the OS and installing JDK
############################################################


RUN apt-get -y update && \
    apt-get -y install \
    language-pack-en \
    vim \
    libopenblas-dev



# add webupd8 repository
RUN \
    echo "===> add webupd8 repository..."  && \
    echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee /etc/apt/sources.list.d/webupd8team-java.list  && \
    echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main" | tee -a /etc/apt/sources.list.d/webupd8team-java.list  && \
    apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886  && \
    apt-get update  && \
    \
    echo "===> install Java"  && \
    echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections  && \
    echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections  && \
    DEBIAN_FRONTEND=noninteractive  apt-get install -y --force-yes oracle-java8-installer oracle-java8-set-default  && \
    \
    \
    echo "===> clean up..."  && \
    rm -rf /var/cache/oracle-jdk8-installer  && \
    apt-get clean  && \
    rm -rf /var/lib/apt/lists/*

RUN locale-gen en_US.UTF-8  
ENV LANG en_US.UTF-8  
ENV LANGUAGE en_US:en  
ENV LC_ALL en_US.UTF-8  

############################################################
# Exposing ports
############################################################

EXPOSE 9300 9200 2181 9092

############################################################
# Running the uberjar
############################################################

WORKDIR $HOME_DIR/word2vec

CMD ["sh", "start.sh"]
