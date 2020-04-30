'''
Contains the majority of the backend work for the application. Takes the input from the user, processes the information,
and sends the necessary information to the html files to then render to the user.
'''
from django.http import HttpResponseRedirect
from django.shortcuts import render
from .forms import TeamForm, CompareTwoForm
from .lasso import run_model
from .connect_to_api import connect
from fpl import FPL
from django.http import HttpResponse
import asyncio
import aiohttp
import unicodedata
import requests
import json
from urllib.parse import parse_qs

#globals
APIS = ["https://fantasy.premierleague.com/api/bootstrap-static/"]

'''calls helper function list_players() and sends the list to all-players.html to be rendered to the user'''
def all_players(request):
    players = list_players()
    players.sort()
    return render(request, 'home/all-players.html', {'players': players})

'''calls helper function calculate_best_players(), parses the player names, and sends a string to be displayed on best-team.html'''
def build_team(request):
    best_players_tuple = calculate_best_players()
    best_players_list = best_players_tuple[0]
    best_players_dict = best_players_tuple[1]

    best0, best1, best2, best3, best4 = best_players_list[0], best_players_list[1], best_players_list[2], best_players_list[3], best_players_list[4]
    best5, best6, best7, best8, best9 = best_players_list[5], best_players_list[6], best_players_list[7], best_players_list[8], best_players_list[9]
    best10, best11, best12, best13, best14 = best_players_list[10],  best_players_list[11], best_players_list[12], best_players_list[13], best_players_list[14]

    player0, player1, player2, player3, player4 = best_players_dict[best0], best_players_dict[best1], best_players_dict[best2], best_players_dict[best3], best_players_dict[best4]
    player5, player6, player7, player8, player9 = best_players_dict[best5], best_players_dict[best6], best_players_dict[best7], best_players_dict[best8], best_players_dict[best9]
    player10, player11, player12, player13, player14 = best_players_dict[best10], best_players_dict[best11], best_players_dict[best12], best_players_dict[best13], best_players_dict[best14]

    best_players = "The 15 best possible players are: " + player0 + ", " + player1 + ", " + player2 + ", " + player3 + ", " + player4 + ", " + player5 + ", " + player6 + ", " + player7 + ", " + player8 + ", " + player9 + ", " + player10 + ", " + player11 + ", " + player12 + ", " + player13 + ", and " + player14 + ". Good luck trying to fit them all into your team!"

    return render(request, 'home/best-team.html', {'best_players': best_players})

'''Takes the user's form entries and sends the page to the suggestions page where they will see their results'''
def compare_players(request):
    if request.method  == 'POST':
        form = CompareTwoForm(request.POST)
        if form.is_valid():
            players = ''
            for obj in form.cleaned_data:
                players += form.cleaned_data[obj] + ","
            return HttpResponseRedirect('suggestions/?players=' + players)
    else:
        form = CompareTwoForm()
    return render(request, 'home/compare-two-players.html', {'form': form})

'''
Calls helper function calculate_comparisons() and either tells the front end that the user made an error
entering player names or provides them with a suggestion of which player is predicted to have a better week.
'''
def compare_players_suggestions(request):
    comparison_list = parse_qs(request.META['QUERY_STRING'])['players'][0].split(',')
    comparison = calculate_comparisons(comparison_list)
    if comparison == '-1':
        statement = 'One or both players were entered incorrectly. Please try again.'
        return render(request, 'home/not-enough-players.html', {'statement': statement})
    else:
        return render(request, 'home/compare-two-results.html', {'comparison': comparison})

'''Takes the user's form entries and sends the page to the team-suggestions page where they will see their results'''
def get_name(request):
    if request.method == 'POST':
        form = TeamForm(request.POST)
        if form.is_valid():
            players = ''
            for obj in form.cleaned_data:
                players += form.cleaned_data[obj] + ","
            return HttpResponseRedirect('team-suggestions/?players=' + players)
    else:
        form = TeamForm()
    return render(request, 'home/input-team.html', {'form': form})

'''Returns the website's home page'''
def home_page(request):
    return render(request, 'home/index.html')

'''
Calls helper function parse_players() and either returns that the user didn't enter at least 3 names properly
or outputs the suggestion for how to improve their team.
'''
def suggest_players(request):
    team = parse_qs(request.META['QUERY_STRING'])['players'][0].split(',')
    suggestion_tuple = parse_players(team)
    if suggestion_tuple == '-1':
        statement = "You failed to enter at least three player names correctly. Please try again."
        return render(request, 'home/not-enough-players.html', {'statement': statement})
    else:
        suggestions = suggestion_tuple[0]
        failed_player_entries = suggestion_tuple[1]
        return render(request, 'home/team-results.html', {'suggestions': suggestions, 'failed_player_entries': failed_player_entries})


# Helper methods below this line #

'''
Connects to the API and calls run_model() to run every Premier League player through the model and then calculates
and returns the 15 highest predicted players
'''
def calculate_best_players():
    json_object = connect(APIS[0])
    predicted_scores = run_model(json_object)
    keys = list(predicted_scores)
    keys.sort(reverse = True)
    return (keys[:15], predicted_scores)
    
'''
Provides the logic for the comparison page. Ensures both players were entered properly, calls run_model() to 
get their predicted scores, get_form() to modify their predicted score based on week-to-week data and returns
a suggestion for which player is predicted to perform better this week.
'''
def calculate_comparisons(inputted_team):
    inputted_team = [i for i in inputted_team if i]
    players_list= []
    json_object = connect(APIS[0])
    secondPlayer = False
    for inputted_player in inputted_team:
        for person in json_object:
            json_name = person['first_name'] + " " + person['second_name']
            if format_name(inputted_player) == format_name(json_name):
                players_list.append(person)
                if secondPlayer:
                    player_id_tuple = (first_player_id, person['id'])
                else:
                    secondPlayer = True
                    first_player_id = person['id']
    if (len(players_list) != 2):
        return '-1'
    else:
        predicted_scores = run_model(players_list)
        player1_pred_score, player2_pred_score = list(predicted_scores.keys())[0], list(predicted_scores.keys())[1]
        adj_p1_pred_score, adj_p2_pred_score = player1_pred_score / 38, player2_pred_score / 38

        player_form_tuple = asyncio.run(get_form(player_id_tuple))

        #player1
        if player_form_tuple[0] > 3.0:
            adj_p1_pred_score = adj_p1_pred_score * 2
        elif player_form_tuple[0] == 0.0:
            adj_p1_pred_score = adj_p1_pred_score * 0.5

        #player2
        if player_form_tuple[1] > 3.0:
            adj_p2_pred_score = adj_p2_pred_score * 2
        elif player_form_tuple[1] == 0.0:
            adj_p2_pred_score = adj_p2_pred_score * 0.5

        return 'We are ' + confidence_level((adj_p1_pred_score, adj_p2_pred_score)) + ' that ' + predicted_scores[max(player1_pred_score, player2_pred_score)] +  ' will outperform ' + predicted_scores[min(player1_pred_score, player2_pred_score)] + ' this week.'

'''
Compares two players' adjusted predicted scores and returns a confidence level based on the margin
of difference.
'''
def confidence_level(adj_score_tuple):
    confidence = ''
    if abs(adj_score_tuple[0] - adj_score_tuple[1]) < 1.5:
        confidence = 'only slightly confident'
    elif abs(adj_score_tuple[0] - adj_score_tuple[1]) < 3.0:
        confidence = 'moderately confident'
    else:
        confidence = 'extremely confident'
    return confidence

'''Removes accents and lowercases names to allow users flexibility when entering player names.'''
def format_name(text):
    try:
        text = unicode(text, 'utf-8')
    except NameError:
        pass

    text = str(unicodedata.normalize('NFD', text).encode('ascii', 'ignore').decode("utf-8"))
    final_text = text.lower()
    return final_text

'''Returns a tuple of current player forms from the python fpl package based on the inputted player tuple.'''
async def get_form(inputted_player_tuple):
    session = aiohttp.ClientSession()
    fpl = FPL(session)
    player_one = await fpl.get_player(inputted_player_tuple[0])
    player_two = await fpl.get_player(inputted_player_tuple[1])
    player_form_tuple = (float(player_one.__dict__['form']), float(player_two.__dict__['form']))
    await session.close()
    return player_form_tuple

'''
Connects to the API and adds every Premier League players' name to a list. Then return that list to be displayed
on the front end.
'''
def list_players():
    json_object = connect(APIS[0])
    players = []
    for player in json_object:
        players.append(player['first_name'] + " " + player['second_name'])
    return players

'''
Contains the logic for the Input Team page. Takes user's team information, runs the players through the model
and returns suggestions about their team as well as the number of players they misspelled. Also handles error
checking for if the user fails to enter 3 player names properly.
'''
def parse_players(inputted_team):
    inputted_team = [i for i in inputted_team if i]
    json_object = connect(APIS[0])
    players_list = []
    for inputted_player in inputted_team:
        for person in json_object:
            json_name = person['first_name'] + " " + person['second_name']
            if format_name(inputted_player) == format_name(json_name):
                players_list.append(person)
    player_score_predictions = run_model(players_list)
    keys = list(player_score_predictions.keys())
    keys.sort()

    if (len(keys) < 3):
        return '-1'
    else:
        worst_player_score = keys[0]
        second_worst_player_score = keys[1]
        third_worst_player_score = keys[2]

        worst_player_name = player_score_predictions[worst_player_score]
        second_worst_player_name = player_score_predictions[second_worst_player_score]
        third_worst_player_name = player_score_predictions[third_worst_player_score]

        return_tuple =  ("Your worst predicted players in order are " + worst_player_name + ", " + second_worst_player_name + ", and " + third_worst_player_name + ". You should consider benching these players or finding replacements on the transfer market.", 15 - len(keys))
        return return_tuple
