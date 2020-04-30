'''
This file contains the logic for connecting to the Fantasy Premier League's (FPL) live API
'''
import json
import requests

'''Connects to the input API url and returns the API's json object'''
def connect(url):
    return requests.get(url).json()['elements'] #returns json object