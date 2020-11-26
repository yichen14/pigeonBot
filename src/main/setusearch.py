import rpyc
import sys
print(rpyc.connect("localhost",11451).root.search(sys.argv[1]))