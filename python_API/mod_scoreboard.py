from utils import ModuleHandler
from http_utils import HttpClient, process_response
from prettytable import PrettyTable, ALL
import json
import sys

class ScoreBoardModuleHandler(ModuleHandler):
    def __init__(self, settings):
        super(ScoreBoardModuleHandler,self).__init__(settings)
        self.client = HttpClient(settings['hostname'], settings['port'], settings['apikey'])
    
    def handle_command(self, command):
        if command[0] == 'score':
            self.__get_score_board()
            return True
        return False

    def list_commands(self):
        help = [
            ('score', 'Print the current scoreboard.'),
        ]
        return help

    def __get_score_board_table(self, sizes):
        table = PrettyTable(["Rank", "User"] + sizes + ["Total Score"])
        table.align["User"] = "l"
        table.align["Total Score"] = "r"
        table.padding_width = 1
        table.hrules = ALL
        return table

    def __get_score_board(self):
        response = self.client.send('GET', '/scoreboard/1.0.0')
        payload = process_response(response)
        if payload is not None:
            sb = json.loads(payload)
            if not sb['users']:
                print 'No scores to report'
                print
                return

            sizes = set()
            sizes_map = {}
            user_scores = sb['userScores']
            for us in user_scores:
                for app_score in us['applicationScores']:
                    for solution in app_score['solutions']:
                        sol_size = solution['solutionSize']
                        sizes.add(sol_size)
                        if sizes_map.has_key(sol_size):
                            sizes_map[sol_size] = sizes_map[sol_size] + solution['solutionCount']
                        else:
                            sizes_map[sol_size] = solution['solutionCount']

            sizes_list = sorted(list(sizes))
            table = self.__get_score_board_table(sizes_list)
            rank = 0
            last_total = 1000
            diff = 1
            class_total = 0
            for us in user_scores:
                if last_total > us['totalScore']:
                    rank += diff
                    diff = 1
                else:
                    diff += 1
                scores = [0] * len(sizes_list)
                for app_score in us['applicationScores']:
                    for solution in app_score['solutions']:
                        index = sizes_list.index(solution['solutionSize'])
                        scores[index] += solution['solutionCount']
                table.add_row([rank, us['username']] + scores + [ '{0:.4f}%'.format(us['totalScore']) ] )
                last_total = us['totalScore']
                class_total += us['totalScore']

            total_row = ['Total', '']
            for item in sizes_list:
                total_row.append(sizes_map[item])
            total_row.append('{0:.4f}%'.format(class_total))
            table.add_row(total_row)
                                  
            print table

            if len(user_scores) > 1:
                print '{0} users have non-zero scores'.format(len(user_scores))
            else:
                print '1 user has a non-zero score.'
            print

    def __get_app_scores(self, app_scores):
        result = []
        app_scores.sort(key=lambda app_sc: app_sc['score'], reverse=True)
        for app_score in app_scores:
            result.append('{0} ({1})'.format(app_score['application'], app_score['score']))
        return ', '.join(result)
    
def get_module_handler(settings):
    return ScoreBoardModuleHandler(settings)
