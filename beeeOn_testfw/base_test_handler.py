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

DB_CON_STRING = "dbname='home7' user='xvampo01' password='1234'"


class BaseTestHandler(i_segment_test_handler.ISegmentTestHandler):
    def __init__(self):
        self.name = "base tests"
        self.dbLastOutput = []
        # can throw
        self.dbConn = psycopg2.connect(DB_CON_STRING)
        self.dbCursor = self.dbConn.cursor()

        self.supportedTests = {}
        self.supportedTests['sql'] = self.processDb
        self.supportedTests['sqlresult'] = self.processDbResult
        self.supportedTests['py'] = self.processPythonScript

    def getSupportedTests(self):
        return self.supportedTests

    def processDb(self, testFilePath):
        # print "processDB"
        sqlFile = utils.readFile(testFilePath).strip(' \t\n\r')
        sqlCommands = sqlFile.split(';')
        # filter nonempty strings
        for command in filter(None,sqlCommands):
            try:
                #print command
                self.dbCursor.execute(command)
                self.dbConn.commit()
                #print .statusmessage
                #print .rowcount

                #self.dbLastOutput = .fetchall()

            except psycopg2.Error as e:
                print self.dbCursor.query
                print e

                self.dbConn.rollback()
                #print e.pgerror
                #print e.diag.message_primary
                return False
        return True

    def processDbResult(self, testFilePath):
        # print "processDBresult"
        self.dbLastOutput = []
        try:
            for row in self.dbCursor.fetchall():
                strtuple = (str(d) for d in row)
                self.dbLastOutput.append(tuple(strtuple,))
        except psycopg2.Error as e:
                self.dbConn.rollback()
                return False
        refData = []
        with open(testFilePath) as input:
            for line in input:
                temp = ()
                for value in line.strip().split('\t'):
                    temp += (value,)
                refData.append(temp)

        if(refData == self.dbLastOutput):
            return True
        else:
            print self.dbCursor.query
            print self.dbLastOutput
            print refData
            return False

    def processPythonScript(self, testFilePath):
        # print "processPythonScript"
        workingDir = os.getcwd()

        fullpath = os.path.abspath(testFilePath)
        path, filename = os.path.split(fullpath)
        filename, ext = os.path.splitext(filename)
        sys.path.append(path)
        module = import_module(filename)
        # reload(module)
        del sys.path[-1]

        os.chdir(path)
        try:
            cl = getattr(module, "testPlugin_main")
            moduleReturnValue = cl(self.dbConn)
        except Exception as e:
            print utils.warning("Py module thrown exception:")
            print e
            moduleReturnValue = False

        os.chdir(workingDir)

        return moduleReturnValue