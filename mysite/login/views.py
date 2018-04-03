# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render, redirect
from django.contrib.auth.models import User
from django.contrib.auth import authenticate
from django.contrib.auth import login as auth_login
from django.contrib.auth import logout as auth_logout
from django.contrib.auth.decorators import login_required
from django.http import HttpResponseRedirect, HttpResponse
from django.shortcuts import redirect
from django.template import loader
from login.forms import UserForm

# Create your views here.
def sign_up(request):
    if request.method == 'POST':
        user_form = UserForm(data = request.POST)
        if user_form.is_valid():
          user = user_form.save()
          user.set_password(user.password)
          user.save()
          print "User created!"
          return redirect('/login')
    elif request.method == 'GET':
        user_form = UserForm()
    else:
        return HttpResponse("Invalid HTTP Method")
    template = loader.get_template('login/sign_up.html')
    context = {
            'user_form': user_form,
    }
    return HttpResponse(template.render(context, request))

def login(request):
    if request.user.is_authenticated:
        return redirect('/homepage')

    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        user = authenticate(username = username, password = password)
        if user:
            auth_login(request, user)
            return redirect('/homepage')
        else:
            return HttpResponse("Invalid login details supplied.")
    elif request.method == 'GET':
        return render(request, 'login/login.html', {})
    else:
        return HttpResponse("Invalid HTTP Method")

@login_required(login_url='/login/')
def logout(request):
    auth_logout(request)
    return redirect('/login')
