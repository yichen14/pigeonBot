from pixivpy3 import PixivAPI 
import rpyc
import random
import sys

_REQUESTS_KWARGS = {
     'proxies': {
         'https': 'http://127.0.0.1:1080',
     },
     'verify': True,
}

class SetuService(rpyc.Service):
    def __init__(self,username,password):
        super().__init__()
        self._username=username
        self._password=password
    def on_connect(self, conn):
        self._api=PixivAPI(**_REQUESTS_KWARGS)
        self._api.login(self._username,self._password)
    def exposed_search(self,keyword,mode):
         self._illusts=self._api.search_works(keyword,types=["illustration"],per_page=1000,mode=mode).response
         self._illusts.sort(key=lambda illust:-illust.stats.views_count)
         return self._illusts[random.randint(0,5)].image_urls.large.replace("i.pximg.net","i.pixiv.cat")

rpyc.utils.server.ThreadedServer(SetuService(sys.argv[1],sys.argv[2]),port=11451).start()
