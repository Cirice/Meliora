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

RUN mkdir -p $HOME_DIR/meliora_python
ADD . $HOME_DIR/meliora_python

############################################################
# Updating the OS
############################################################

RUN apt-get -y update && \
     apt-get -y install \
     python3-pip \
     libblas-dev \
     liblapack-dev \
     libatlas-base-dev \
     gfortran \
     python3-scipy \
     vim

RUN locale-gen en_US.UTF-8
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8


############################################################
# Installing the requirements
############################################################

WORKDIR $HOME_DIR/meliora_python/src

RUN pip3 install -r ../requirements.txt

CMD ["python3", "main.py"]

