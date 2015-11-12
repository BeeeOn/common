#!/usr/bin/python

from shutil import copyfile
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
        self.supportedTests['etc'] = self.copyConfigs
        self.supportedTests['sh'] = self.executeShell

    def getSupportedTests(self):
        return self.supportedTests;

    def setInstallDir(self, installDir):
        self.installDir = installDir;

    def copyConfigs(self, testFilePath):
        ls_dir = os.listdir(testFilePath);
        for file in ls_dir:
            copyfile(testFilePath+"/"+file, self.installDir+"/etc/beeeon/"+file);
        return True

    def executeShell(self, testFilePath):
        ret_code = os.system("./"+testFilePath + " " + self.installDir)
        if (ret_code == 0):
            return True
        else:
            return False
