from utils import ModuleHandler

class BaseModuleHandler(ModuleHandler):
    def __init__(self, settings):
        super(BaseModuleHandler,self).__init__(settings)
    
    def handle_command(self, command):
        if command[0] == 'settings':
            self.__print_settings()
            return True
        elif command[0] == 'quit':
            print 'Bye'
            exit(0)
        return False

    def list_commands(self):
        help = [
            ('quit', 'Quit this application.'),
            ('settings', 'Print the current settings used by the application.')
        ]
        return help

    def __print_settings(self):
        print 'RichCoin Administrator Settings'
        print '  Hostname : {0}'.format(self.settings['hostname'])
        print '  Port     : {0}'.format(self.settings['port'])
        print '  API Key  : {0}'.format(self.settings['apikey'])

def get_module_handler(settings):
    return BaseModuleHandler(settings)
