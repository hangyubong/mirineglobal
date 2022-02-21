import sys

print('The command line arguments are:')
#print(sys.argv)
#print(sys.argv[0])
print(sys.argv[4])
for i in sys.argv:
    print(i)

print('\n\nPATH is')
#print(sys.path)
for i in sys.path:
    print(i)

# 실행예 (Terminal실행 -> python sysArgv.py
# 실행예 (Terminal실행 -> python sysArgv.py we play football 2022.2.21
# -- argv[] 인덱스 값 확인