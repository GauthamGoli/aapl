from django.conf.urls import url
from login import views

urlpatterns = [ 
        url(r'^sign_up/$', views.sign_up),
        url(r'^$', views.login),
        url(r'^logout$', views.logout),
]
