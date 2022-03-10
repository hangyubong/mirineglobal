# NAVER株価日別データのクローリング及び視覚化
___
### ※要求事項定義
>1.社名と取得開始日を入力してもらい、指定した日付から現時点まで日別株価情報をクローリングする。<br>
> 
>2.クロリングしたデータをcsvファイルとして保存し、出力する。<br>
&nbsp;&nbsp;&nbsp;-出力データ(日付、終値、ボリューム)<br>
&nbsp;&nbsp;&nbsp;-Plotを利用したグラフの出力
>
> 3.ドッカーを利用して実装する。<br>
&nbsp;&nbsp;&nbsp;-Dockerfile, docker-compose.yml作成<br>
&nbsp;&nbsp;&nbsp;-ドッカーイメージを生成<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(ドッカーコンポーズプロンプトでビルドアップする)<br>
&nbsp;&nbsp;&nbsp;-コンテナ内で実装する
<br>


### ※入力
>パラメータで入力される。- 例）ファイル名(stock.py), 種目名('카카오게임즈'), 取得開始日('yyyy.mm.dd')

<br>

### ※出力
>csvファイルでデータを読み込んできて、日付別の株価を出力し、plotライブラリーを使って株価をグラフで表せる。

<br>

### ※実行方法(イメージ)
####  &nbsp;&nbsp;&nbsp;- 種目名, 取得開始日時入力(日付指定)

<img src="./picture/실행(입력-1).jpg" width="350" height="30"/>

<br>

### ※出力例(イメージ)
#### 1. 出力データ

---

<img src="./picture/데이터출력값1.jpg" width="500" height="200"/>

----
#### 2. Plotチャート   

---
<img src=./picture/plot그래프(candle).jpg" width="800" height="250"/>

---

### ※ドッカーを利用して実装してみる(イメージ)
#### 1. ドッカーイメージ生成

---

<img src="./picture/dockerimages0.jpg" width="800" height="80"/>

---

#### 2. コンテナの動作確認

___

<img src="./picture/dockerps0.jpg" width="800" height="60"/>

___

#### 3. コンテナ内で実装する

---

<img src=./picture/컨테이너에서실행(결과)_0.jpg" width="800" height="300"/>

---
