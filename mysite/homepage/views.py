import os
import requests
from mysite import settings
import jwt
import datetime
from django.contrib.auth.decorators import login_required
from django.http import HttpResponse, HttpResponseServerError


@login_required(login_url='/login/')
def index(request):
    token = request.COOKIES.get('token') if 'token' in request.COOKIES else generate_signed_token(request.user.username)
    response = fetch_greeting(token)
    response = process_greeting_service_response(request.user.username, token, response)
    return response


def generate_signed_token(username, validity_minutes=settings.GREETING_TOKEN_VALIDITY_MINUTES):
    expiry_timestamp = datetime.datetime.utcnow() + datetime.timedelta(minutes=validity_minutes)
    payload = {'exp': expiry_timestamp, 'username': username}
    secret_key = os.environ['GREETING_SERVICE_SECRET'] if 'GREETING_SERVICE_SECRET' in os.environ else ''
    return jwt.encode(payload, secret_key)


def fetch_greeting(token):
    greeting_service_url = settings.GREETING_SERVICE_URL
    return requests.get(greeting_service_url, params={'token': token})


def process_greeting_service_response(username, token, response):
    if response.status_code == 200:
        response = HttpResponse(response.json()['greeting'])
        response.set_cookie('token', token)
    elif response.status_code == 401 or response.status_code == 400:
        token = generate_signed_token(username)
        response = fetch_greeting(token)
        response = process_greeting_service_response(username, token, response)
    else:
        response = HttpResponseServerError("Try Later. Greeting service seems to be down")
    return response
