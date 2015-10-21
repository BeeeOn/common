#!/usr/bin/python

import socket
import ssl
import sys
import glob
import os
import StringIO
#xml + canonical xml
import lxml.etree as ET
import shutil
# import ref sql result from file
import json
import csv
#postgres
import psycopg2
#parse arrays from string
import ast
#for python plugins
from importlib import import_module
import argparse
import re
#avoid python bytecode in import scripts
sys.dont_write_bytecode = True
from operator import attrgetter
import xml_utils


def success(text):
    return bcolors.OKGREEN + text + bcolors.ENDC

def failure(text):
    return bcolors.FAIL + text + bcolors.ENDC

def warning(text):
    return bcolors.WARNING + text + bcolors.ENDC

def info(text):
    return bcolors.OKBLUE + text + bcolors.ENDC


class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


   
class SecuredSocket:

    def __init__(self, host, port):
        self.host = host
        self.port = port 
        
        self.normalsock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.normalsock.settimeout(2)
        self.securedSock = ssl.wrap_socket(self.normalsock)  
        self.securedSock.connect((host, port))
        
    def write(self, msgToWrite):
        self.securedSock.send(msgToWrite)
        
    def read(self):
        reply = self.securedSock.recv(1280)
        return reply
        
    def close(self):
        self.securedSock.close() 

def listToTuple(lst):
    val = lst[0:len(lst)]
    yield tuple(val)

def readFile(file):
    with open(file, 'r') as f:
        return f.read()

def executeScriptsFromFile(c, filename):
    sqlFile = readFile(filename).strip(' \t\n\r')
    sqlCommands = sqlFile.split(';')

    # filter nonempty strings
    for command in filter(None,sqlCommands):
        print command
        try:
            c.execute(command)
            ver = c.fetchall()
            print ver[0]

        except psycopg2.Error :
            print "Command skipped: ", command, ";"



def success(text):
    return bcolors.OKGREEN + text + bcolors.ENDC


def failure(text):
    return bcolors.FAIL + text + bcolors.ENDC


def warning(text):
    return bcolors.WARNING + text + bcolors.ENDC


def info(text):
    return bcolors.OKBLUE + text + bcolors.ENDC


class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


'''
def sendToSslSocket( host, port, request ):
   # SET VARIABLES
  packet, reply = "<com>SOME_DATA</com>", ""

  # CREATE SOCKET
  sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  sock.settimeout(10)

  # WRAP SOCKET
  wrappedSocket = ssl.wrap_socket(sock)

  # CONNECT AND PRINT REPLY
  wrappedSocket.connect((host, port))
  wrappedSocket.send(packet)
  reply = wrappedSocket.recv(1280)

  # CLOSE SOCKET CONNECTION
  wrappedSocket.close()
  return reply
'''