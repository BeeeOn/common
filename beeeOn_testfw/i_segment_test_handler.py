#!/usr/bin/python

import sys
import glob
import os
import StringIO

class ISegmentTestHandler:
    def __init__(self):
        pass

    def getSupportedTests(self):
        '''
        return {'extension':function(testFilePath), ...}
        'extension' should not be same in more than one segment
        function should return boolean as result of single test
        '''
        raise NotImplementedError