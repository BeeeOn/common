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

class AdaappTestHandler(i_segment_test_handler.ISegmentTestHandler):

    def __init__(self):
        self.name = "adaapp tests"

        self.setInstallDir(os.getcwd()+"/adaapp_configs/install_x86.ignore")
        self.supportedTests = {}
        self.supportedTests['etc'] = self.tryConfigs
        self.supportedTests['sh'] = self.executeShell

    def getSupportedTests(self):
        return self.supportedTests;

    def setInstallDir(self, installDir):
        self.installDir = installDir;

    def tryConfigs(self, testFilePath):
        print "HELLO TRY CONFIG"
        return True

    def executeShell(self, testFilePath):
        os.system("./"+testFilePath + " " + self.installDir)
        return True
