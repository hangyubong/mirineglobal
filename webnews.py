# ライブラリのよびだし。
from bs4 import BeautifulSoup
import requests
from elasticsearch import Elasticsearch
import datetime

# キーワードで資料検索。
search = input("検索のキーワードを入力してください。:")
# 検索のPAGEを入力。
page = int(input("クローリングしてくるPAGEを入力してください。. ex)1(数字で入力):"))  # ex)1 =1PAGE
print("クローリングのPAGE: ", page, "PAGE")

# STARTのPAGEを作る　1,11,21 ...
page_num = 0
if page == 1:
    page_num = 1
elif page == 0:
    page_num = 1
else:
    page_num = page + 9 * (page - 1)

# url - 特定のマスコミを利用（東亜日報)
url = "https://search.naver.com/search.naver?where=news&sm=tab_pge&query=" + search + "&sm=tab_opt&sort=0&photo=0&field=0&pd=0&ds=&de=&docid=&related=0&mynews=1&office_type=1&office_section_code=1&news_office_checked=1020&nso=so%3Ar%2Cp%3Aall&is_sug_officeid=0&start=" + str(page_num)
# https://search.naver.com/search.naver?where=news&query=%EC%86%8C%ED%94%84%ED%8A%B8%EB%B1%85%ED%81%AC&sm=tab_opt&sort=0&photo=0&field=0&pd=0&ds=&de=&docid=&related=0&mynews=1&office_type=1&office_section_code=1&news_office_checked=1020&nso=so%3Ar%2Cp%3Aall&is_sug_officeid=0
print("searchURL: ", url)

# htmlparsing
req = requests.get(url)
html = BeautifulSoup(req.text, "html.parser")

# 検索の結果
articles = html.select("div.group_news > ul.list_news > li div.news_area > a")
# print(articles)

# 検索された記事の数
print('>', len(articles), "個の記事を呼び出し.")

# 1. newsタイトル呼び出し
news_title = []
for i in articles:
    news_title.append(i.attrs['title'])
print('news_title :', news_title)

# 2. newsのURLを呼び出し
news_url = []
for i in articles:
    news_url.append(i.attrs['href'])
print('news_url :', news_url)

# 3. newsの本文内容をクローリング
contents = []
choiceurl = news_url[0]
for i in news_url:
    # 別々の記事のHTMLを呼び出し
    news = requests.get(choiceurl)
    news_html = BeautifulSoup(news.text, "html.parser")

    # 記事の本文内容を持ってくる
    contents = news_html.select("#content > div ") # 共通するPathまでselectする
    for contents in contents: #繰り返し実行するオブジェクトを入れるfor文の作成。
        try:
            text = contents.select_one("div.article_txt").text # 本文の内容をテキストだけ持ってくる
        except AttributeError:
            continue
print("\n" + "本文：" + text)

es = Elasticsearch('http://127.0.0.1:9200')
def insertData():
    index = search
    doc = {
        "title": news_title[0],
        "link": news_url[0],
        "contents": text,
        "timestamp": datetime.datetime.now(),
    }
    es.index(index=index, doc_type="_doc", body=doc)
    print("success create index!")
insertData()
