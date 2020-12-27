from pixivpy3 import PixivAPI 
import rpyc
import random
import sys
import json

_REQUESTS_KWARGS = {
     #'proxies': {
     #    'https': 'http://127.0.0.1:1080',
     #},
     #'verify': True,
}



class SetuService(rpyc.Service):
    def __init__(self,username,password):
        super().__init__()
        self._username=username
        self._password=password
        self._fglfile= open('src/main/resources/fgl.txt','r')
        self._fgl = self._fglfile.readlines()
        self._fglfile.close()
    def on_connect(self, conn):
        self._api=PixivAPI(**_REQUESTS_KWARGS)
        self._api.login(self._username,self._password)
    def exposed_search(self,keyword,mode):
        #self._illusts=self._api.search_works(keyword,types=["illustration"],per_page=1000,mode=mode).response
        #self._illusts.sort(key=lambda illust:-illust.stats.views_count)
        #return self._illusts[random.randint(0,5)].image_urls.large.replace("i.pximg.net","i.pixiv.cat")
        self._illusts=self._api.search_works(keyword,types=["illustration"],per_page=10,mode=mode,sort="popular").response
        fglFile = open('src/main/resources/fgl.txt', 'r')
        for i in self._illusts:
            if str(i.id) not in self._fgl:
                print(self._fgl)
                f = open('src/main/resources/fgl.txt', 'w')
                self._fgl.append(str(i.id))
                f.write(str(i.id) + '\n')
                f.close()
                return i.image_urls.large.replace("i.pximg.net","i.pixiv.cat")

                break


rpyc.utils.server.ThreadedServer(SetuService(sys.argv[1],sys.argv[2]),port=11451).start()
