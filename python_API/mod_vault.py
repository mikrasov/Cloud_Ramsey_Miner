from utils import ModuleHandler, ts_to_str, error_code_to_str
from http_utils import HttpClient, process_response
from prettytable import PrettyTable
import json
import time

class VaultModuleHandler(ModuleHandler):
    def __init__(self, settings):
        super(VaultModuleHandler,self).__init__(settings)
        self.client = HttpClient(settings['hostname'], settings['port'], settings['apikey'])

    def handle_command(self, command):
        if command[0] == 'listcoins':
            self.__list_coins()
            return True
        elif command[0] == 'get':            
            if len(command) != 2:
                print "Invalid call to the 'getcoin' command. Call must be of the form: 'getcoin <coinId>'."
            else:
                self.__get_coin(command[1])
            return True
        elif command[0] == 'mint':
            if len(command) != 2:
                print "Invalid call to the 'mint' command. Call must be of the form: 'mint <file>'."
            else:
                self.__mint_coin(command[1])
            return True
        return False

    def list_commands(self):
        help = [
            ('mint <file>', 'Submit a new RichCoin to be minted using the solution in the specified file.'),
            ('get <coinId>', 'Get the coin with the specified ID from the vault.'),
            ('listcoins', 'List all the coins currently available in the vault.'),
        ]
        return help

    def __list_coins(self):
        response = self.client.send('GET', '/vault/1.0.0')
        payload = process_response(response)
        if payload is not None:
            coin_list = json.loads(payload)
            if len(coin_list) > 0:
                table = self.__get_coin_table()
                for coin in coin_list:
                    table.add_row([coin['coinId'], coin['user'], coin['application'],
                                   ts_to_str(coin['timestamp']), self.__status_str(coin['status']), coin['size']])
                print table
                if len(coin_list) > 1:
                    print '{0} coins available in vault'.format(len(coin_list))
                else:
                    print '1 coin available in vault'
            else:
                print 'No coins available in vault'
            print

    def __get_coin(self, coin_id):                              
        response = self.client.send('GET', '/vault/1.0.0/{0}'.format(coin_id))
        payload = process_response(response)
        if payload is not None:
            coin = json.loads(payload)
            table = self.__get_coin_table(True)
            table.add_row([coin['coinId'], coin['user'], coin['application'], ts_to_str(coin['timestamp']),
                           self.__status_str(coin['status']), coin['reason'], error_code_to_str(coin['reason']), coin['size']])
            print table
            print

    def __status_str(self, status):
        if status == 1:
            return 'Valid'
        elif status == 2:
            return 'Invalid'
        elif status == 0:
            return 'Pending'
        else:
            raise Exception('Invalid status value')
            
    def __mint_coin(self, file_path):
        content = None
        try:
            file_handle = open(file_path, 'r')
            content = file_handle.read()
            file_handle.close()
        except Exception as ex:
            print 'Error while reading from file: {0}'.format(file_path)
            print '{0}'.format(ex)
            return

        solution = ""
        for ch in content:
            if ch != '0' and ch != '1' and not ch.isspace():
                print "Invalid character '{0}' in solution file. Only 0's, 1's and whitespaces are allowed.".format(ch)
                return
            if ch == '0' or ch == '1':
                solution += ch

        req_json = { 'solution' : solution, 'clientTimestamp' : int(time.time() * 1000) }
        response = self.client.send('POST', '/vault/1.0.0', json.dumps(req_json))
        payload = process_response(response, 201)
        if payload:
            candidate = json.loads(payload)
            table = self.__get_coin_table()
            table.add_row([candidate['coinId'], candidate['user'], candidate['application'],
                           ts_to_str(candidate['timestamp']), self.__status_str(candidate['status']), candidate['size']])
            print table
            print 'Solution candidate submitted successfully.'
            print

    def __get_coin_table(self, detailed=False):
        fields = ["Coin ID", "User", "Application", "Timestamp", "Status", "Size"]
        if detailed:
            fields += [ "Error Code", "Message" ]
        table = PrettyTable(fields)
        table.align["Coin ID"] = "l"
        table.align["User"] = "l"
        table.align["Application"] = "l"
        table.align["Timestamp"] = "l"
        table.padding_width = 1
        return table

def get_module_handler(settings):
    return VaultModuleHandler(settings)
