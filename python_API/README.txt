Welcome to the Command-line management tool for RichCoin API version 2.

Requirements
============

* Python 2.7.x
* Following additional Python modules need to be installed:
	- yaml
	- atexit
	- readline
	- json
	- prettytable

Chances are you already have all these installed, may be except
for 'prettytable'. You can install it by running:

 $ sudo pip install prettytable

Some trial-and-error would be necessary to ensure that all
dependencies are properly set up in your system.

Running
=======

* Open the settings.yaml file.
* Update the hostname entry so that the tool talks to the
  online API. Do not change the port value.
* Specify your API key.
* Save the changes to settings.yaml file.
* Launch the admin_tool.py script.

* Run the 'help' command on the 'richcoin' shell to see a 
  list of supported commands.

* Alternatively you can specify a custom settings file by
  starting the tool as follows:
  $ ./admin_tool.py custom-settings.yaml

Understanding the Code
======================

* Most of the code related to making HTTP calls are in the
  http_utils.py module (See the HttpClient class). 
* mod_vault.py coordinates calls to the Vault API and the
  mod_scoreboard.py coordinates calls to the Scoreboard API.

* Some important functions:
  - mod_vault.py --> __list_coins()
  - mod_vault.py --> __mint_coin()
  - mod_scoreboard.py --> __get_score_board()
