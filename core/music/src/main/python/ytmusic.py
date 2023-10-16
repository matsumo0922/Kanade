from ytmusicapi import YTMusic

class YTMusicClient:

    def __init__(self, file_path):
        self.ytmusic = YTMusic(file_path)

    def search(self, query, filter, scope):
        return self.ytmusic.search(query, filter=filter, scope=scope)
