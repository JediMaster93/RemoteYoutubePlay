from BaseHTTPServer import BaseHTTPRequestHandler, HTTPServer
import webbrowser
from ctypes import cast, POINTER
from comtypes import CLSCTX_ALL
from pycaw.pycaw import AudioUtilities, IAudioEndpointVolume
import json
class S(BaseHTTPRequestHandler):
    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()

    def do_GET(self):
        self._set_headers()
        if "init/getVolumeInfo" in self.path:
            data = {}
            range = self.server.volume.GetVolumeRange()
            print range
            print self.server.volume.GetMasterVolumeLevel()
            data["VolumeRange"] = [range[0], range[1]]
            data["MasterVolumeLevel"] = self.server.volume.GetMasterVolumeLevel()
            print json.dumps(data)
            self.wfile.write(json.dumps(data))
        elif "/conf/vol" in self.path:
            pass
        else:
            webbrowser.open(self.path[1:])
            print self.path
    def do_HEAD(self):
        self._set_headers()

    def do_POST(self):
        # Doesn't do anything with posted data
        print "post"
        contentLenght = self.headers.getheaders('content-length')
        print self.headers
        lenght = int(contentLenght[0]) if contentLenght else 0
        data = self.rfile.read(lenght)
        print data
        dataJson = json.loads(data)
        print data,dataJson
        if "VolumeLevel" in dataJson.keys():
            self.server.volume.SetMasterVolumeLevel(int(dataJson["VolumeLevel"]),None)
        self._set_headers()

class MyServer(HTTPServer):
    def __init__(self,*args,**kwargs):
        HTTPServer.__init__(self,*args,**kwargs)
        devices = AudioUtilities.GetSpeakers()
        interface = devices.Activate(
            IAudioEndpointVolume._iid_, CLSCTX_ALL, None)
        volume = cast(interface, POINTER(IAudioEndpointVolume))
        """print("volume.GetMute(): %s" % volume.GetMute())
        print("volume.GetMasterVolumeLevel(): %s" % volume.GetMasterVolumeLevel())
        print("volume.GetVolumeRange(): (%s, %s, %s)" % volume.GetVolumeRange())
        print("volume.SetMasterVolumeLevel()")"""

        self.volume = volume


def run(server_class=MyServer, handler_class=S, port=8080):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)

    print 'Starting httpd...'
    httpd.serve_forever()

if __name__ == "__main__":
    from sys import argv

    if len(argv) == 2:
        run(port=int(argv[1]))
    else:
        run()