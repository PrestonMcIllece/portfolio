'''
This file contains the unit tests for all helper methods.
'''
from unittest import TestCase
from . import views
from . import connect_to_api
from . import lasso

class TestFPL(TestCase):
    '''Calculates 15 players with the highest predicted score in the Premier League and returns a tuple of
    a list of the 15 best players and a dictionary of all the players and their predicted score.'''
    def test_calculate_best_players(self):
        self.assertTrue(type(views.calculate_best_players()[0]) == list) #the first element in the returned tuple should be a list
        self.assertTrue(type(views.calculate_best_players()[1] == dict)) #the second element in the returned tuple should be a dict
        self.assertEqual(len(views.calculate_best_players()[0]), 15) #the returned list should contain exactly 15 elements

    '''Calculates difference between two players predicted scores and returns confidence level'''
    def test_confidence_level(self):
        self.assertEqual(views.confidence_level((1.5, 1.0)), 'only slightly confident') #small differential returns little confidence
        self.assertEqual(views.confidence_level((3.0, 1.0)), 'moderately confident') #medium differential returns moderate confidence
        self.assertEqual(views.confidence_level((8.0, 1.0)), 'extremely confident') #large differential returns high confidence
        self.assertTrue(type(views.confidence_level((8.0, 1.0))) == str) #returns a string

    '''Returns API's json object'''
    def test_connect_to_api(self):
        self.assertTrue(type(connect_to_api.connect("https://fantasy.premierleague.com/api/bootstrap-static/") == dict)) #returns a dict object

    '''Removes accents and lowercases names'''
    def test_format_name(self):
        self.assertEqual(views.format_name('TAMMY ABRAHAM'), 'tammy abraham') #lowercases names
        self.assertEqual(views.format_name('raúl jiménez'), 'raul jimenez') #removes accents
        self.assertEqual(views.format_name('José Ángel Esmorís Tasende'), 'jose angel esmoris tasende') #removes accents and lowercases names
        self.assertEqual(views.format_name("N'Golo Kanté"), "n'golo kante") #doesn't affect apostraphes in names
        self.assertTrue(type(views.format_name('James Maddison')) == str) #returns a string

    '''Returns a tuple of player form values and takes a tuple of player ids. Due to the Coronavirus shutting down the 
    season, everyone's form is currently 0.0'''
    def test_get_form(self):
        self.assertTrue(type(views.get_form((171, 29)) == tuple)) #returns a tuple

    '''Returns a list of every player in the Premier League'''
    def test_list_players(self):
        self.assertTrue(type(views.list_players()) == list) #returns a list


    '''
    All tests below this are somewhat arbitrary and difficult to test as the API
    is always changing and therefore passing these tests will not necessarily always mean
    that the code is operating properly.

    These tests are to simply to demonstrate that the code is working correctly right now.
    '''



    """If two players are entered correctly, returns a string to be displayed on the front end. Otherwise returns '-1'"""
    def test_calculate_comparisons(self):
        correct_return_statement = 'We are only slightly confident that James Maddison will outperform Jack Grealish this week.'

        self.assertEqual(views.calculate_comparisons(['FakeName', 'InvalidName']), '-1') #improperly spelled names should return '-1'
        self.assertEqual(views.calculate_comparisons(['FakeName', 'Jack Grealish']), '-1') #one incorrect name should also return '-1'
        self.assertEqual(views.calculate_comparisons(['James Maddison', 'Jack Grealish']), correct_return_statement) #Correct usage at this point in time.
        self.assertTrue(type(views.calculate_comparisons(['James Maddison', 'Jack Grealish'])) == str) #returns a string

    """If at least 3 players are entered correctly, returns a tuple of a string to be displayed and an int of how
    many player names were misspelled. Otherwise, returns '-1'"""
    def test_parse_players(self):
        zero_correct_names = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o']
        three_correct_names = ['James Maddison', 'Jack Grealish', "N'Golo Kanté", 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o']
        ten_correct_names = ['James Maddison', 'Jack Grealish', "N'Golo Kanté", 'Dean Henderson', 'Nick Pope', 'Mason Mount', 'Reece James', 'Tammy Abraham', 'Teemu Pukki', 'Christian Pulisic', 'k', 'l', 'm', 'n', 'o']
        all_correct_names = ['James Maddison', 'Jack Grealish', "N'Golo Kanté", 'Dean Henderson', 'Nick Pope', 'Mason Mount', 'Reece James', 'Tammy Abraham', 'Teemu Pukki', 'Christian Pulisic', 'Mateo Kovacic', 'Marcos Alonso', 'Gary Cahill', 'Matt Ritchie', 'Callum Hudson-Odoi']

        self.assertEqual(views.parse_players(zero_correct_names), '-1') #returns '-1' since user failed to enter 3 names correctly
        self.assertEqual(views.parse_players(three_correct_names)[1], 12) #second tuple element should be 12 since only 3 names were entered properly
        self.assertEqual(views.parse_players(ten_correct_names)[1], 5) #second tuple element should be 5 since 10 names were entered properly
        self.assertEqual(views.parse_players(all_correct_names)[1], 0) #second tuple element should be 0 since all 15 names were entered properly
        self.assertTrue(type(views.parse_players(all_correct_names)[0]) == str) #first tuple element is a string
        self.assertTrue(type(views.parse_players(all_correct_names)[1]) == int) #second tuple element is an int

    '''Returns dictionary of player's predicted score as the key and their name as the value.'''
    def test_run_model(self):
        player_json = {"chance_of_playing_next_round":100,"chance_of_playing_this_round":100,"code":172780,"cost_change_event":0,"cost_change_event_fall":0,"cost_change_start":5,"cost_change_start_fall":-5,"dreamteam_count":1,"element_type":3,"ep_next":"0.0","ep_this":"0.0","event_points":0,"first_name":"James","form":"0.0","id":171,"in_dreamteam":False,"news":"","news_added":"2020-03-12T14:30:24.243040Z","now_cost":75,"photo":"172780.jpg","points_per_game":"4.2","second_name":"Maddison","selected_by_percent":"18.8","special":False,"status":"a","team":9,"team_code":13,"total_points":119,"transfers_in":3090571,"transfers_in_event":56,"transfers_out":2500581,"transfers_out_event":188,"value_form":"0.0","value_season":"15.9","web_name":"Maddison","minutes":2399,"goals_scored":6,"assists":5,"clean_sheets":9,"goals_conceded":25,"own_goals":0,"penalties_saved":0,"penalties_missed":0,"yellow_cards":4,"red_cards":0,"saves":0,"bonus":14,"bps":501,"influence":"613.6","creativity":"1171.8","threat":"634.0","ict_index":"242.0","influence_rank":34,"influence_rank_type":8,"creativity_rank":2,"creativity_rank_type":2,"threat_rank":37,"threat_rank_type":16,"ict_index_rank":6,"ict_index_rank_type":4}
        self.assertTrue(type(lasso.run_model([player_json])) == dict) #returns a dict object
        self.assertTrue(len([player_json]) == len(lasso.run_model([player_json]))) #length on dict you enter should be the same length as what is returned
        self.assertEqual(lasso.run_model([player_json]), {130.38207096858: 'James Maddison'}) #player's current predicted score
