# ライブラリのよびだし。
from bs4 import BeautifulSoup
import requests

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
print("url: ", url)

# htmlparsing
req = requests.get(url)
html = BeautifulSoup(req.text, "html.parser")

# 検索の結果
articles = html.select("div.group_news > ul.list_news > li div.news_area > a")
# print(articles)

# 検索された記事の数
print(len(articles), "個の記事を呼び出し.")

# newsタイトル呼び出し
news_title = []
for i in articles:
    news_title.append(i.attrs['title'])
print(news_title)

# newsのURLを呼び出し
news_url = []
for i in articles:
    news_url.append(i.attrs['href'])
print(news_url)


# newsの内容をクローリング
contents = []
choiceurl = news_url[0]
for i in news_url:
    # 別々の記事のHTMLを呼び出し
    news = requests.get(choiceurl)
    news_html = BeautifulSoup(news.text, "html.parser")

    # 記事の本文内容を持ってくる
    contents = news_html.select("#content > div ") #파싱 soup.select기능을 사용하여 우선 공통되는 주소까지를 설정한다.
    for contents in contents: #반복 실행할 객체를 담는 for문 작성.
        try: #.text로 필요한 정보를 불러올시에 문자가 없는 tr태그들로 인한 오류가 발생하므로 예외처리 진행.
            text = contents.select_one("div.article_txt").text                #순위를 불러올 구문 작성.
        except AttributeError:
            continue
print("\n" + "本文：" + text)