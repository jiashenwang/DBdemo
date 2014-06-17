package com.example.databasedemo;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class parses XML feeds from stackoverflow.com.
 * Given an InputStream representation of a feed, it returns a List of entries,
 * where each list element represents a single entry (post) in the XML feed.
 */
public class StackOverflowXmlParser {
    private static final String ns = null;

    // We don't use namespaces

    public List<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            //parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Entry> entries = new ArrayList<Entry>();

        
        parser.require(XmlPullParser.START_TAG, ns, "ROWSET");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("ROW")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    // This class represents a single entry (post) in the XML feed.
    // It includes the data members "title," "link," and "summary."
    public static class Entry {
        public final String Ligand_SMILES;
        public final String BindingDB_MonomerID;
        public final String BindingDB_Ligand_Name;
        public final String Ki;
        public final String IC50;
        public final String Link;
        public final String UniProt_Recommended_Name_of_Target_Chain;
        public final String UniProt_Primary_ID_of_Target_Chain;
        

        private Entry(String Ligand_SMILES, String BindingDB_MonomerID, String BindingDB_Ligand_Name,
        				String Ki, String IC50, String Link, String UniProt_Recommended_Name_of_Target_Chain,
        				String UniProt_Primary_ID_of_Target_Chain) 
        {
            this.Ligand_SMILES = Ligand_SMILES;
            this.BindingDB_MonomerID = BindingDB_MonomerID;
            this.BindingDB_Ligand_Name = BindingDB_Ligand_Name;
            this.Ki = Ki;
            this.IC50 = IC50;
            this.Link = Link;
            this.UniProt_Recommended_Name_of_Target_Chain = UniProt_Recommended_Name_of_Target_Chain;
            this.UniProt_Primary_ID_of_Target_Chain = UniProt_Primary_ID_of_Target_Chain;
            
            /*
            this.title = title;
            this.summary = summary;
            this.link = link;
             */
        }
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them
    // off
    // to their respective &quot;read&quot; methods for processing. Otherwise, skips the tag.
    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "ROW");
        /*
        String title = null;
        String summary = null;
        String link = null;
        */
        String ligand_SMILES = null;
        String bindingDB_MonomerID = null;
        String bindingDB_Ligand_Name = null;
        String ki = null;
        String iC50 = null;
        String link = null;
        String uniProt_Recommended_Name_of_Target_Chain = null;
        String uniProt_Primary_ID_of_Target_Chain = null;
        
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("Ligand_SMILES")) {
            	ligand_SMILES = read_Ligand_SMILES(parser);
            } else if (name.equals("BindingDB_MonomerID")) {
            	bindingDB_MonomerID = read_BindingDB_MonomerID(parser);
            } else if (name.equals("BindingDB_Ligand_Name")) {
            	bindingDB_Ligand_Name = readBLN(parser);
            } else if (name.equals("Ki")) {
            	ki = readKi(parser);
            } else if (name.equals("IC50")) {
            	iC50 = readIc(parser);
            } else if (name.equals("Link_to_Ligand-Target_Pair_in_BindingDB")) {
            	link = readLink(parser);
            } else if (name.equals("UniProt_Recommended_Name_of_Target_Chain")) {
            	uniProt_Recommended_Name_of_Target_Chain = readURNOTC(parser);
            } else if (name.equals("UniProt_Primary_ID_of_Target_Chain")) {
            	uniProt_Primary_ID_of_Target_Chain = readUPIOTC(parser);
            }       
            else {
                skip(parser);
            }
        }
        return new Entry(ligand_SMILES, bindingDB_MonomerID, bindingDB_Ligand_Name, ki, iC50,
        			link, uniProt_Recommended_Name_of_Target_Chain, uniProt_Primary_ID_of_Target_Chain);
    }

    // Processes title tags in the feed.
    private String read_Ligand_SMILES(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Ligand_SMILES");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Ligand_SMILES");
        return title;
    }

    // Processes link tags in the feed.
    private String read_BindingDB_MonomerID(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "BindingDB_MonomerID");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "BindingDB_MonomerID");
        return title;
    }

    // Processes link tags in the feed.
    private String readBLN(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "BindingDB_Ligand_Name");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "BindingDB_Ligand_Name");
        return title;
    }
    
    // Processes link tags in the feed.
    private String readKi(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Ki");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Ki");
        return title;
    }
    
    // Processes link tags in the feed.
    private String readIc(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "IC50");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "IC50");
        return title;
    }
    
    // Processes link tags in the feed.
    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "Link_to_Ligand-Target_Pair_in_BindingDB");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "Link_to_Ligand-Target_Pair_in_BindingDB");
        return title;
    }
    
    // Processes link tags in the feed.
    private String readURNOTC(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "UniProt_Recommended_Name_of_Target_Chain");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "UniProt_Recommended_Name_of_Target_Chain");
        return title;
    }
    
    // Processes link tags in the feed.
    private String readUPIOTC(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "UniProt_Primary_ID_of_Target_Chain");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "UniProt_Primary_ID_of_Target_Chain");
        return title;
    }

    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags. i.e.,
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
            case XmlPullParser.END_TAG:
                    depth--;
                    break;
            case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}