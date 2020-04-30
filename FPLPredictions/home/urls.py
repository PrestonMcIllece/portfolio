'''
Contains the applications' URLs and links them to the correct views. This ensures that the necessary logic is
run for the given URL.
'''

from django.urls import path
from . import views

urlpatterns = [
    path('', views.home_page, name="home-page"),
    path('input-team/', views.get_name, name="get-name"),
    path('all-players/', views.all_players, name='all-players'),
    path('input-team/team-suggestions/', views.suggest_players, name='suggest-players'),
    path('build-team/', views.build_team, name='from-scratch'),
    path('compare-players/', views.compare_players, name='compare-players'),
    path('compare-players/suggestions/', views.compare_players_suggestions, name='compare-players-suggestions')
]