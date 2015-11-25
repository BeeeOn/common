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
ADA_PORT = 8453

class AdaTestHandler(i_segment_test_handler.ISegmentTestHandler):

    def __init__(self):
        self.name = "ada-server tests"
        self.lastSocketOutput = ""
        self.dbLastOutput = []
        self.host = HOST
        self.port = ADA_PORT

        self.supportedTests = {}
        self.supportedTests['inputada'] = self.processInput
        self.supportedTests['outputada'] = self.processOutput

    def getSupportedTests(self):
        return self.supportedTests;

    def testPrint(self, filePath):
        print "hello fw"

    def processInput(self, testFilePath):

        try:
            self.socket = utils.SecuredSocket(self.host, self.port)
        except:
            print utils.failure("FAIL: cannot connect to ada_server socket")
            raise

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
