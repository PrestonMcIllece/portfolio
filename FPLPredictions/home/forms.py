'''
Contains the forms to be displayed on the front end.
'''

from django import forms
from django.utils.safestring import mark_safe

'''Form for the Comparison page'''
class CompareTwoForm(forms.Form):
    player1 = forms.CharField(label='Player 1', max_length=100)
    player2 = forms.CharField(label='Player 2', max_length=100)

'''Form for the Input Team page'''
class TeamForm(forms.Form):
    goalkeeper1 = forms.CharField(label='Goalkeeper 1', max_length=100)
    goalkeeper2 = forms.CharField(label='Goalkeeper 2', max_length=100)
    defender1 = forms.CharField(label='Defender 1', max_length=100)
    defender2 = forms.CharField(label='Defender 2', max_length=100)
    defender3 = forms.CharField(label='Defender 3', max_length=100)
    defender4 = forms.CharField(label='Defender 4', max_length=100)
    defender5 = forms.CharField(label='Defender 5', max_length=100)
    midfielder1 = forms.CharField(label='Midfielder 1', max_length=100)
    midfielder2 = forms.CharField(label='Midfielder 2', max_length=100)
    midfielder3 = forms.CharField(label='Midfielder 3', max_length=100)
    midfielder4 = forms.CharField(label='Midfielder 4', max_length=100)
    midfielder5 = forms.CharField(label='Midfielder 5', max_length=100)
    forward1 = forms.CharField(label='Forward 1', max_length=100)
    forward2 = forms.CharField(label='Forward 2', max_length=100)
    forward3 = forms.CharField(label=mark_safe('<br /> Forward 3'), max_length=100)
