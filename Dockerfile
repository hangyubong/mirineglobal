FROM python:3.7-slim-stretch
USER bami #user

RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y #基本的に指定するsetting
RUN apt-get -y install locales && \
    localedef -f UTF-8 -i ja_JP ja_JP.UTF-8 #言語setting
ENV LANG ja_JP.UTF-8
ENV LANGUAGE ja_JP:ja
ENV LC_ALL ja_JP.UTF-8

RUN apt-get install -y vim less #vim(=vi)コンテナ内で使う基本的なLinuxアプリケーション / less(comand)
RUN pip install --upgrade pip #Pythonの最新にアップデート
RUN pip install --upgrade setuptools

COPY stock.py /app/
COPY company.csv /app/
COPY requirements.txt /app/
WORKDIR /app
RUN pip install -r requirements.txt #使ってるライブラリインストール