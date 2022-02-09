import pandas as pd
from bs4 import BeautifulSoup
import requests
from datetime import datetime
import csv
import matplotlib.pyplot as plt

code = '005930' # 삼성전자 종목코드
url = f"http://finance.naver.com/item/sise_day.nhn?code={code}"
headers = {'User-agent': 'Mozilla/5.0'} # 웹브라우저 접속처럼 인식시키기 위해 정보 추가

req = requests.get(url=url, headers = headers)
bs = BeautifulSoup(req.text, 'html.parser')
pgrr = bs.find('td', class_='pgRR')
pgrr.a["href"].split('=')
['/item/sise_day.nhn?code', '005930&page', '641']
last_page = int(pgrr.a["href"].split('=')[-1])
page_url = '{}&page={}'.format(url, 1)
data = pd.read_html(requests.get(page_url, headers={'User-agent': 'Mozilla/5.0'}).text)[0]

#null값은 제거하기

#페이지를 추출. 전체 페이지 수와 10을 비교해서 작은 값을 추출할 페이지 수(pages)로 지정한다.
page_no = 10
pages = min(last_page, page_no) # 마지막 페이지와 가져올 페이지 수 중에 작은 값 선택

#루프를 돌면서 각 페이지의 일별 시세를 추출하여 병합한다.
df = pd.DataFrame()
for page in range(1, pages +1):
    page_url = '{}&page={}'.format(url, page)
    df = pd.concat([pd.read_html(requests.get(page_url, headers={'User-agent': 'Mozilla/5.0'}).text)[0]])

#추출한 시세의 컬럼명을 수정하고, 데이터 타입 변경, 컬럼 순서를 조정한다.
df = df.rename(columns={'날짜':'date','종가':'close','전일비':'diff'
                ,'시가':'open','고가':'high','저가':'low','거래량':'volume'}) #영문으로 컬럼명 변경
df['date'] = pd.to_datetime(df['date'])

df = df.dropna() # 결측치 제거

df[['close', 'diff', 'open', 'high', 'low', 'volume']] = df[['close','diff', 'open', 'high', 'low', 'volume']].astype(int) # BIGINT형으로 지정한 컬럼을 int형으로 변경
df = df[['date', 'open', 'high', 'low', 'close', 'diff', 'volume']]

df = df.sort_values(by = 'date') # 날짜순으로 정렬

# df.to_csv('삼성전자.csv', encoding='utf-8-sig')

df.head()
print(df)

plt.figure
plt.plot(data['date'], data['close'])
