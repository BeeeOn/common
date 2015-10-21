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
import i_segment_test_handler

import utils
import xml_utils

HOST = '0.0.0.0'
ADA_PORT = 7080
DB_CON_STRING = "dbname='home7' user='xvampo01' password='1234'"

class AdaTestHandler(i_segment_test_handler.ISegmentTestHandler):

    def __init__(self):
        self.name = "ada-server tests"
        self.lastSocketOutput = ""
        self.dbLastOutput = []
        self.host = HOST
        self.port = ADA_PORT
        # can throw
        self.socket = utils.SecuredSocket(self.host, self.port)

        self.dbConn = psycopg2.connect(DB_CON_STRING)
        self.dbCursor = self.dbConn.cursor()

        self.supportedTests = {}
        self.supportedTests['inputada'] = self.processInput
        self.supportedTests['outputada'] = self.processOutput

    def getSupportedTests(self):
        return self.supportedTests;

    def testPrint(self, filePath):
        print "hello fw"

    def processInput(self, testFilePath):
        #print "processInput", self.testFilePath
        input = utils.readFile(testFilePath)
        self.socket.write(input)
        self.lastSocketOutput = self.socket.read()

        return True

    def processOutput(self, testFilePath):
        # print "processOutput"
        referenceOutput = xml_utils.sortAndCanonizeXML(utils.readFile(testFilePath).replace('\n', ''))
        outputCanonicalXML = xml_utils.sortAndCanonizeXML(self.lastSocketOutput)

        if(referenceOutput == outputCanonicalXML):
            return True
        else:
            print ""
            print "R:", referenceOutput
            print "o:", outputCanonicalXML
            return False
