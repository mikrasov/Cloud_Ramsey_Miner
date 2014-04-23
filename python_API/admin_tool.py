#!/usr/bin/python

import yaml
import os
import math
import shlex
import readline
import atexit
import sys

def print_help(module_handlers):
    print '\nList of supported commands:'
    cmd_list = [ ('help', 'Print this help message.') ]
    for mh in module_handlers:
        cmd_list += mh.list_commands()

    cmd_list.sort(key=lambda tup: tup[0])

    max_width = max(map(lambda x: len(x[0]), cmd_list))
    width = int(math.ceil((max_width + 2) / 5.0)) * 5
    
    for item in cmd_list:
        print "{0}{1}{2}".format(item[0], ' ' * (width - len(item[0])), item[1])
    print
            
if __name__ == '__main__':
    settings_file_name = 'settings.yaml'
    if len(sys.argv) == 2:        
        settings_file_name = sys.argv[1]
    settings_file = open(settings_file_name, 'r')
    settings = yaml.load(settings_file)
    settings_file.close()

    module_handlers = []
    
    files = [f for f in os.listdir('.') if os.path.isfile(f)]
    for f in files:
        if f.startswith('mod_') and f.endswith('.py'):
            mod_name = os.path.splitext(os.path.basename(f))[0]
            mod = __import__(mod_name)
            module_handlers.append(mod.get_module_handler(settings))
    
    print "Welcome to RichCoin administrator."
    print
    print "Type 'help' for a list of supported commands."
    print

    histfile = '.history'
    if hasattr(readline, "read_history_file"):
        try:
            readline.read_history_file(histfile)
        except IOError:
            pass
        atexit.register(readline.write_history_file, histfile)
    
    readline.set_history_length(100)

    while True:
        command = raw_input('richcoin> ').strip()
        if not command:
            continue

        segments = shlex.split(command)
        if segments[0] == 'help':
            print_help(module_handlers)
            continue
        
        command_handled = False
        for handler in module_handlers:
            if handler.handle_command(segments):
                command_handled = True
                break
            else:
                continue

        if not command_handled:
            print 'Unrecognized command: {0}'.format(command)
