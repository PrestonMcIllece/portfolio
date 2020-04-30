'''
This file contains the logic pertaining to the Lasso Regression statistical model.
'''

'''
Implementation of the specific Lasso model that I calculated in R for my Data Science minor capstone. I chose
to translate the model rather than rebuilding it in Python for clarity and consistency between my minor and major
capstones. Takes a list of dictionary objects representing players, calculates their predicted score based on their
attributes, and returns a dictionary of players' predicted scores as the key and their name as the value.
'''
def run_model(team):
    predicted_scores = {}
    for player in team:
        player_predicted_score = -4.8742602872
        
        #coefficients
        cost = float(player['now_cost']) * (0.1543691695)
        creativity = float(player['creativity']) * (-0.0005484974)
        threat = float(player['threat']) * (0.0023287057)
        goals_conceded = float(player['goals_conceded']) * (-0.6544763400)
        goals_scored = float(player['goals_scored']) * (2.3166202702)
        assists = float(player['assists']) * (2.5464879938)
        own_goals = float(player['own_goals']) * (-1.2840750162)
        penalties_missed = float(player['penalties_missed']) * (-6.4315299121)
        penalties_saved = float(player['penalties_saved']) * (2.7587927408)
        saves = float(player['saves']) * (0.1769758792)
        yellow_cards = float(player['yellow_cards']) * (-0.9779798534)
        red_cards = float(player['red_cards']) * (-3.0056529235)
        minutes = float(player['minutes']) * (0.0382517294)
        bonus = float(player['bonus']) * (1.7657672054)

        player_predicted_score += cost + creativity + threat + goals_conceded + goals_scored + assists + own_goals + penalties_missed + penalties_saved + saves + yellow_cards + red_cards + minutes + bonus
        full_name = player['first_name'] + " " + player['second_name']
        predicted_scores[player_predicted_score] = full_name
    return predicted_scores

