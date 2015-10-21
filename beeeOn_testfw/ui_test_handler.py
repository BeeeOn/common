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
UI_PORT = 8811
DB_CON_STRING = "dbname='home7' user='xvampo01' password='1234'"

IGNORED_ATTRIBUTE = "UNKNOWN_ATTRIBUTE"

class UiTestHandler(i_segment_test_handler.ISegmentTestHandler):
    def __init__(self):
        self.name = "ui-server tests"
        self.lastSocketOutput = ""
        self.dbLastOutput = []
        self.host = HOST
        self.port = UI_PORT
        # can throw
        self.socket = utils.SecuredSocket(self.host, self.port)

        self.dbConn = psycopg2.connect(DB_CON_STRING)
        self.dbCursor = self.dbConn.cursor()

        self.supportedTests = {}
        self.supportedTests['input'] = self.processInput
        self.supportedTests['output'] = self.processOutput
        self.supportedTests['osyntax'] = self.processOutputSyntax


    def getSupportedTests(self):
        return self.supportedTests


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

        #print referenceOutput
        #print outputCanonicalXML

        atributeValueToDelete = IGNORED_ATTRIBUTE
        # index of target attribute
        cutReferenceOutput = referenceOutput
        cutOutputCanonicalXML = outputCanonicalXML
        refIdx = cutReferenceOutput.find(atributeValueToDelete)
        while refIdx != -1:
            # remove target from REF
            cutReferenceOutput = cutReferenceOutput.replace(atributeValueToDelete, '', 1)
            # find end of target attribute assignment
            outputIdx = cutOutputCanonicalXML.find("\"",refIdx)
            # remove target from OUT
            cutOutputCanonicalXML = cutOutputCanonicalXML[:refIdx] + cutOutputCanonicalXML[outputIdx:]
            # index of target attribute
            refIdx = cutReferenceOutput.find(atributeValueToDelete)


        if(cutReferenceOutput == cutOutputCanonicalXML):
            return True
        else:
            print ""
            print "R:", referenceOutput
            print "o:", outputCanonicalXML
            return False

    def processOutputSyntax(self, testFilePath):
        # print "processOutput"
        referenceOutputSyntax = xml_utils.getCanonicalXmlFromString(utils.readFile(testFilePath))
        # remove starting declaration element
        serverSyntax = re.sub(r'^\<\?xml.*?\?\>', r'', self.lastSocketOutput)
        # remove all data in attributes
        serverSyntax = re.sub(r'=".*?"', r'=""', serverSyntax)
        # print serverSyntax
        outputCanonicalXMLSyn = xml_utils.getCanonicalXmlFromString(serverSyntax)
        # print referenceOutput , outputCanonicalXML
        if(referenceOutputSyntax == outputCanonicalXMLSyn):
            return True
        else:
            print referenceOutputSyntax
            print outputCanonicalXMLSyn
            return False
