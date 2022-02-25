# Class : 繰り返される不必要なソースコードを最小限にして
#         現実世界の物事をコンピュータプログラミング上で
#         分かりやすく表現できるプログラミング技術。

# インスタンス : Classで定義されたオブジェクトをプログラム上で利用できるようにした変数。

# クラスのメンバ : クラス内部に含まれる変数。
# クラスの関数 : クラス内部に含まれる関数。

## practice 2.
##  - Car() Classを作って名前と色をいくつかつけてみる。

### =========================================== ###



## practice 1.
##  - Person() classを作って自己紹介ができるように作ってみる。（name, age, job..)
class Person():
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def say_hello(self, to_name):
        print("hi! " + to_name + " im "  + self.name)

    def introduce(self):
        print("myname is! " + self.name + " im "  + str(self.age) + " age~")

# wonie = Person("wonie", 20)
# nicole = Person("nicole", 21)
# jenny = Person("jenny", 22)
#
# wonie.say_hello("あずみ")
# nicole.say_hello("たなか")
# jenny.say_hello("よしひろ")
#
# wonie.introduce()
# nicole.introduce()
# jenny.introduce()

# class相続
class Police(Person):
    def arrest(self, to_arrest):
        print("あなたを逮捕する, " + to_arrest)

class Programmer(Person):
    def program(self, to_program):
        print("今回開発するものは: " + to_program)

wonie = Person("wonie", 20)
nicole = Police("nicole", 21)
jenny = Programmer("jenny", 22)

wonie.introduce()
nicole.introduce()
nicole.arrest("wonie")
jenny.introduce()
jenny.program("世界地図探索機")
### =========================================== ###