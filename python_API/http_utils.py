import httplib
import json

class ResponseInfo:
    """
    Contains the metadata and data related to a HTTP response. In
    particular this class can be used as a holder of HTTP response
    code, headers and payload information.
    """
    def __init__(self, response=None):
        """
        Create a new instance of ResponseInfo using the given HTTPResponse
        object.
        """
        self.headers = {}
        if response:
            self.status = response.status
            for header in response.getheaders():
                self.headers[header[0]] = header[1]
            self.payload = response.read()
        else:
            self.status = -1
            self.payload = None

class HttpClient:
    def __init__(self, hostname, port, apikey):
        self.hostname = hostname
        self.port = port
        self.apikey = apikey

    def send(self, method, path, json_payload=None):
        conn = httplib.HTTPSConnection('{0}:{1}'.format(self.hostname, self.port))
        headers = { 'Authorization' : 'Bearer {0}'.format(self.apikey) }
        response_info = None
        try:
            if method == 'POST' or method == 'PUT':
                headers['Content-type'] = 'application/json'
                conn.request(method, path, json_payload, headers=headers)
            else:
                conn.request(method, path, headers=headers)
            response = conn.getresponse()
            return ResponseInfo(response)
        except Exception as ex:
            response_info = ResponseInfo()
            response_info.payload = str(ex)
            return response_info
        finally:
            try:
                conn.close()
            except Exception:
                pass
                        
def process_response(response, expected_status=200):
    if response.status == expected_status:
        return response.payload
    elif response.status == 401:
        print 'Authorization failure! Please verify your API key.'
    elif response.status == 404:
        try:
            error = json.loads(response.payload)
            print '[EC {1}] {0}'.format(error['errorMessage'], error['errorCode'])
        except Exception:
            print 'Requested resource does not exist.'
    elif response.status == -1:
        print 'Error while contacting the backend server: {0}.'.format(response.payload)
    else:
        try:
            error = json.loads(response.payload)
            print '[EC {1}] {0}'.format(error['errorMessage'], error['errorCode'])
        except Exception:
            print 'Backend server returned error: Status={0}; Message={1}'.format(response.status, response.payload)
    return None
