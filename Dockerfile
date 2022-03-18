FROM python:3.7-slim-stretch
USER root

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y
RUN apt-get -y install locales && \
    localedef -f UTF-8 -i ja_JP ja_JP.UTF-8
ENV LANG ja_JP.UTF-8
ENV LANGUAGE ja_JP:ja
ENV LC_ALL ja_JP.UTF-8

RUN apt-get install -y vim less
RUN pip install --upgrade pip
RUN pip install --upgrade setuptools

COPY stock.py /app/
COPY es.py /app/
COPY logstash.conf /app/
COPY requirements.txt /app/

WORKDIR /app
RUN pip install -r requirements.txt