from datetime import datetime
import time

class ModuleHandler(object):
    def __init__(self, settings):
        self.settings = settings

    def handle_command(self, command):
        raise NotImplementedError

def ts_to_str(timestamp):
    t = datetime.fromtimestamp(timestamp/1000.0)
    return t.strftime('%Y-%m-%d %H:%M:%S')

def str_to_ts(string):
    return long(time.mktime(time.strptime(string, "%Y-%m-%d %H:%M:%S"))) * 1000

def error_code_to_str(code):
    if code == 0:
        return 'N/A'
    elif code == 100100:
        return 'Timestamp too old'
    elif code == 100101:
        return 'Incorrect solution'
    elif code == 100102:
        return 'Isomorphic solution'
    elif code == 100103:
        return 'Malformed solution'
    elif code == 100104:
        return 'Invalid coin ID'
    elif code == 100105:
        return 'Invalid attempt ID'
    elif code == 200100:
        return 'Unexpected database error'
    elif code == 400101:
        return 'Missing username header'
    elif code == 400102:
        return 'Missing application header'
    elif code == 400103:
        return 'Faulty server configuration'
    else:
        return 'Unexpected error'

