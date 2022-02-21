
import sys

# 1.
# 실행예 (Terminal실행 -> python sysArgv.py
# 실행예 (Terminal실행 -> python sysArgv.py we play football 2022.2.21
# -- argv[] 인덱스 값 확인

# print('The command line arguments are:')
# #print(sys.argv)
# #print(sys.argv[0])
# print(sys.argv[4])
# for i in sys.argv:
#     print(i)
#
# print("\n파일명은 제외시키고 출력하기")
# for v in range(1, len(sys.argv)):
#     print(sys.argv[v])
#
# print('\n\nPATH is')
# #print(sys.path)
# for i in sys.path:
#     print(i)
#=================================================#


# 2.
# 실행예 (Terminal실행 -> python sysArgv.py 3 7

# val1 = int(sys.argv[1])
# val2 = int(sys.argv[2])
# print(val1, "곱하기", val2, "는?", (val1*val2))
#=================================================#


# 3.
# 중국집 메뉴 입력하기
if len(sys.argv) == 1: #기본 파일명 한개는 잡혀있으므로
    print("현재 입력된 메뉴가 없습니다.")

print("\n메뉴의 개수는 : %d 개 입니다." % (len(sys.argv) -1))

print("\n메뉴\n------------")
for i in range(len(sys.argv)):
    # print("sys.argv[%d] = '%s'" %(i, sys.argv[i]))
        print(sys.argv[i]+ "\n")
