import pandas as pd
from bs4 import BeautifulSoup
import requests
import traceback
from datetime import datetime
import csv
import matplotlib.pyplot as plt

# 함수정리
def get_krx_code():
    try:
        url = 'http://kind.krx.co.kr/corpgeneral/corpList.do?method=download&searchType=13{0}' #한국거래소 상장법인목록 다운로드
        stock_code = pd.read_html(url, header=0)[0] #url html의 table가지고옴 / header=0으로 0번째줄 컬럼설정 / [0]으로 인덱싱하여 데이터프레임 타입으로 만들어준다.
        stock_code['종목코드'] = stock_code['종목코드'].map('{:06d}'.format) # url에 사용하기 위해 '종목코드' 항목을 0을 채워넣은 6자리 숫자 포맷으로 '종목코드'에 다시 저장해준다.
        stock_code = stock_code[['회사명', '종목코드', '상장일']] # 추출할 컬럼만 선정.
        stock_code = stock_code.rename(columns={'회사명': 'company', '종목코드': 'code', '상장일': 'listing_date'}) # 컬럼명 한글->영문으로 변경.
        stock_code['listing_date'] = pd.to_datetime(stock_code['listing_date']) # 상장일 컬럼 datetime으로 날짜형식 적용.

        # <회사명과 종목코드만 추출하여 csv파일에 저장>
        stock_code = stock_code.sort_values(by='code')
        krxList = stock_code[['code', 'company']]
        # write CSV
        with open(file='company.csv', mode='w', encoding='utf-8-sig', newline='') as f:
            csv_f = csv.writer(f)
            for i in range(len(krxList)):
                value = [krxList.code.values[i], krxList.company.values[i]]
                print(value)
                csv_f.writerow(value)

        print(stock_code) #'회사명', '종목코드', '상장일' 출력확인
        return stock_code

    except Exception as e:
        traceback.print_exc()


def get_stock_price(code):
    df = pd.DataFrame()
    url = f"http://finance.naver.com/item/sise_day.nhn?code={code}"
    header = {'User-agent': 'Mozilla/5.0'}
    bs = BeautifulSoup(requests.get(url=url, headers=header).text, 'html.parser')

    pgrr = bs.find("td", class_="pgRR")
    last_page = int(pgrr.a["href"].split('=')[-1])

    for page in range(1, last_page + 1):
        req = requests.get(f'{url}&page={page}', headers=header)
        df = pd.concat([df, pd.read_html(req.text, encoding='euc-kr')[0]], ignore_index=True)

    df = df.rename(columns={'날짜': 'date', '종가': 'close', '전일비': 'diff'
        , '시가': 'open', '고가': 'high', '저가': 'low', '거래량': 'volume'})  # 영문으로 컬럼명 변경
    df['date'] = pd.to_datetime(df['date'])
    df = df.dropna() # 결측값 제거
    df[['close', 'diff', 'open', 'high', 'low', 'volume']] = \
        df[['close', 'diff', 'open', 'high', 'low', 'volume']].astype(int)  # int형으로 변경. 출력 값 소수점(.0)제거
    df = df[['date', 'open', 'high', 'low', 'close', 'diff', 'volume']]  # 컬럼 순서 정렬
    df = df.sort_values(by='date')  # 날짜순으로 정렬

    df = df.reset_index(drop=True)

    return df

item_name = '카카오'
stock = get_krx_code().query("company=='{}'".format(item_name))['code'].to_string(index=False) # company컬럼의 item_name 종목 코드를 가지고옴
print(stock) # item_name에 따른 종목코드 출력
df = get_stock_price(stock)
# df.head()
print(df)

"""plot 그리기"""
plt.figure(figsize=(10, 4))
plt.plot(df['date'], df['close'])

plt.show()



