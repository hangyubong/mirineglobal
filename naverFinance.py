from bs4 import BeautifulSoup
from urllib.request import urlopen
from datetime import datetime
import pandas as pd
import csv

import matplotlib.pyplot as plt
from mplfinance.original_flavor import candlestick_ohlc
import matplotlib.dates as mdates

code = "323410"
company = "카카오뱅크"
pages = 1 # 0 이면 모든 페이지를 가지고온다.

#마지막 페이지 찾기
def getLastPageNum(url):
    with urlopen(url) as u:
        html = BeautifulSoup(u, "html.parser")
        last = html.find("td", class_="pgRR")
        s = str(last.a["href"]).split('=')
        lastpage = s[-1]
        return int(lastpage)

#페이지 정보 가져오기
def getPageValue(url, pages):
    df = pd.DataFrame()
    print("\n")
    for page in range(1, pages + 1): #한 페이지씩 증가시키면서 가지고온다.
        pg_url = f'{url}&page={page}'
        pValue = pd.read_html(pg_url, header=0)[0]
        df = df.append(pValue)
        print(f'downloading.. {page:04d}/{pages:04d} : {company} ({code})', end="\r")
    print("\n")
    return df

#증시 정보 가져오기
def ReadStock(code, company, pages):
    try:
        url = f"http://finance.naver.com/item/sise_day.nhn?code={code}"

        #입력 값이 0이면 마지막 페이지 값을 가져온다.
        if pages == 0:
            pages = getLastPageNum(url)

        df = getPageValue(url, pages) #각 페이지마다의 정보를 가져온다.

        df = df.rename(columns={'날짜': 'date', '종가': 'close', '전일비': 'diff',
                                '시가': 'open', '고가': 'high', '저가': 'low',
                                '거래량': 'volume'})
        df = df.dropna()    #결측값 행 제거
        #연산을 위해 int 형으로 전환
        df[['close', 'open', 'high', 'low', 'volume']] \
            = df[['close', 'open', 'high', 'low', 'volume']].astype(int)

        #date를 index에 넣고 date column을 삭제
        df.index = df['date']
        df.index = df.index.str.replace(',', '')
        df = df.drop('date', axis=1)

        #string형인 index를 date형으로 변경
        df.index = pd.to_datetime(df.index, format='%Y-%m-%d')

    except Exception as e:
        print('Exception :', str(e))
        return None
    return df

#csv 파일에 저장한다.
def saveToCSV(stockValue): #csv파일로 저장한다.
        with open(file=f'{company}.csv', mode='w', encoding='cp949', newline='') as f:
            csv_f = csv.writer(f)
            csv_f.writerow(['date', 'open', 'high', 'low', 'close', 'volume'])
            for i in range(len(stockValue)):
                value = [stockValue.index.values[i],
                         stockValue.open.values[i],
                         stockValue.high.values[i],
                         stockValue.low.values[i],
                         stockValue.close.values[i],
                         stockValue.volume.values[i]]
                csv_f.writerow(value)

#캔들 그래프 그리기
def drawChart(df):
    df['number'] = df.index.map(mdates.date2num)
    ohlc = df[['number', 'open', 'high', 'low', 'close']]
    p1 = plt.subplot(1, 1, 1)
    plt.grid(True)
    candlestick_ohlc(p1, ohlc.values, width=.6, colorup='red', colordown='blue')

    plt.legend(loc='best')
    plt.show()

#실행 (각 함수 호출)
df = ReadStock(code, company, pages)

if df is None:
    print('데이타를 가져오지 못했습니다.')
else:
    print(df)
    saveToCSV(df)
    drawChart(df)









