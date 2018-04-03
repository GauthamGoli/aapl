import os
import requests
from mysite import settings
import jwt
import datetime
from django.contrib.auth.decorators import login_required
from django.http import HttpResponse, HttpResponseBadRequest, HttpResponseServerError


@login_required(login_url='/login/')
def index(request):
    set_token_cookie = False

    if 'token' in request.COOKIES:
        token = request.COOKIES.get('token')
    else:
        token = generate_signed_token(request.user.username)
        set_token_cookie = True

    response = fetch_greeting(token)
    print response
    if response.status_code == 200:
        response = HttpResponse(response.json()['greeting'])
        response.set_cookie('token', token) if set_token_cookie else None
    elif response.status_code == 401:
        response = HttpResponseBadRequest("Bad Request")
    else:
        response = HttpResponseServerError("Try Later")

    return response


def generate_signed_token(username, validity_minutes=30):
    expiry_timestamp = datetime.datetime.utcnow() + datetime.timedelta(minutes=validity_minutes)
    payload = {'exp': expiry_timestamp, 'username': username}
    secret_key = os.environ['GREETING_SERVICE_SECRET'] if 'GREETING_SERVICE_SECRET' in os.environ else ''
    return jwt.encode(payload, secret_key)


def fetch_greeting(token):
    greeting_service_url = settings.GREETING_SERVICE_URL
    return requests.get(greeting_service_url, params={'token': token})