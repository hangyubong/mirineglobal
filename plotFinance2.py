import requests
from bs4 import BeautifulSoup
import pandas as pd
import datetime
import traceback
import csv
import matplotlib.pyplot as plt


code = '035720' # 카카오 종목코드.
url = f"http://finance.naver.com/item/sise_day.nhn?code={code}" #크롤링할 주소 url변수 생성.
headers = {'User-agent': 'Mozilla/5.0'} # 웹브라우저 접속처럼 인식시키기 위해 정보 추가
req = requests.get(url=url, headers = headers) #요청할 url을 리퀘스트 변수에 담음
bs = BeautifulSoup(req.text, 'html.parser') #BeautifulSoup기능으로 html구문 분석을 위한 parser사용
print(url)

# 마지막 페이지 구하기
pgrr = bs.find('td', class_='pgRR')
lastPage = int(pgrr.a["href"].split('=')[-1])

df = pd.DataFrame()

for page in range(1, lastPage): #페이지를 불러올 for문 작성.
    try:
        page_url = '{}&page={}'.format(url, page)
        df = pd.concat([pd.read_html(requests.get(page_url, headers={'User-agent': 'Mozilla/5.0'}).text)[0]])

        df = df.dropna()  # 결측값 제거

        str_datefrom = datetime.datetime.strftime(datetime.datetime(year=1999, month=11, day=11), '%Y.%m.%d')
        str_dateto = datetime.datetime.strftime(datetime.datetime.today(), '%Y.%m.%d')

        df_filtered = df[df["날짜"] > str_datefrom]
    except Exception as e:
        traceback.print_exc()

    print(df_filtered)

print("- 조회일자 : " + str_datefrom + " ~ " + str_dateto + " -")

#csv 파일 생성
df.to_csv('카카오.csv', encoding='utf-8-sig')

#csv파일 불러와서 plot그리기
data = pd.read_csv("카카오.csv", encoding="utf-8")

plt.figure
plt.plot(data['날짜'], data['종가'])

plt.show()
