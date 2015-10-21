#!/usr/bin/python

import socket
import ssl
import sys
import glob
import os
import StringIO
import lxml.etree as ET

from operator import attrgetter



def getCanonicalXmlFromString(xml):
    try:
        etRoot = ET.fromstring(xml)
        et = ET.ElementTree(etRoot)
        output = StringIO.StringIO()
        #return ET.tostring(etRoot,method='c14n', exclusive=True)
        et.write_c14n(output, exclusive=True)
        retVal = output.getvalue()
    except:
        retVal = "<badxml />"
    return retVal

#
# Function to sort XML elements by id
#  (where the elements have an 'id' attribute that can be cast to an int)
def sortbyid(elem):
    id = elem.get('id')
    if id:
        try:
            return int(id)
        except ValueError:
            return 0
    return 0

#sorting XML from http://dalelane.co.uk/blog/?p=3225

#
# Function to sort XML elements by their text contents
def sortbytext(elem):
    text = elem.text
    if text:
        return text
    else:
        return ''


#
# Function to sort XML attributes alphabetically by key
#  The original item is left unmodified, and it's attributes are
#  copied to the provided sorteditem
def sortAttrs(item, sorteditem):
    attrkeys = sorted(item.keys())
    for key in attrkeys:
        sorteditem.set(key, item.get(key))


#
# Function to sort XML elements
#  The sorted elements will be added as children of the provided newroot
#  This is a recursive function, and will be called on each of the children
#  of items.
def sortElements(items, newroot):
    # The intended sort order is to sort by XML element name
    #  If more than one element has the same name, we want to
    #   sort by their text contents.
    #  If more than one element has the same name and they do
    #   not contain any text contents, we want to sort by the
    #   value of their ID attribute.
    #  If more than one element has the same name, but has
    #   no text contents or ID attribute, their order is left
    #   unmodified.
    #
    # We do this by performing three sorts in the reverse order
    items = sorted(items, key=sortbyid)
    items = sorted(items, key=sortbytext)
    # items = sorted(items, key=attrgetter('tag'))
    items = sorted(items, key=attrgetter('attrib'))

    # Once sorted, we sort each of the items
    for item in items:

        # Create a new item to represent the sorted version
        #  of the next item, and copy the tag name and contents
        newitem = ET.Element(item.tag)
        if item.text and item.text.isspace() == False:
            newitem.text = item.text

        # Copy the attributes (sorted by key) to the new item
        sortAttrs(item, newitem)

        # Copy the children of item (sorted) to the new item
        sortElements(list(item), newitem)

        # Append this sorted item to the sorted root
        newroot.append(newitem)


#
# Function to sort the provided XML file
#  fileobj.filename will be left untouched
#  A new sorted copy of it will be created at fileobj.tmpfilename
def sortFile(fileobj):
    with open(fileobj['filename'], 'r') as original:
        # parse the XML file and get a pointer to the top
        xmldoc = ET.parse(original)
        xmlroot = xmldoc.getroot()

        # create a new XML element that will be the top of
        #  the sorted copy of the XML file
        newxmlroot = ET.Element(xmlroot.tag)

        # create the sorted copy of the XML file
        sortAttrs(xmlroot, newxmlroot)
        sortElements(list(xmlroot), newxmlroot)

        # write the sorted XML file to the temp file
        newtree = ET.ElementTree(newxmlroot)
        with open(fileobj['tmpfilename'], 'wb') as newfile:
            newtree.write(newfile, pretty_print=True)

def sortAndCanonizeXML(xml):

    xmlroot = ET.fromstring(xml)

    # create a new XML element that will be the top of
    #  the sorted copy of the XML file
    newxmlroot = ET.Element(xmlroot.tag)

    # create the sorted copy of the XML file
    sortAttrs(xmlroot, newxmlroot)
    sortElements(list(xmlroot), newxmlroot)

    # write the sorted XML file to the temp file
    newtree = ET.ElementTree(newxmlroot)
    return ET.tostring(newtree, method='c14n', exclusive=True)


if __name__ == "__main__":
    sys.exit(main())