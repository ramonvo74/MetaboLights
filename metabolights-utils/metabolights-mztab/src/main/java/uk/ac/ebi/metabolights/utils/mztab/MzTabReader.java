/*
 * EBI MetaboLights - http://www.ebi.ac.uk/metabolights
 * Cheminformatics and Metabolism group
 *
 * Last modified: 02/10/13 14:17
 * Modified by:   kenneth
 *
 * Copyright 2013 - European Bioinformatics Institute (EMBL-EBI), European Molecular Biology Laboratory, Wellcome Trust Genome Campus, Hinxton, Cambridge CB10 1SD, United Kingdom
 */

package uk.ac.ebi.metabolights.utils.mztab;

import uk.ac.ebi.pride.jmztab.model.MZTabFile;
import uk.ac.ebi.pride.jmztab.model.Metadata;
import uk.ac.ebi.pride.jmztab.model.SmallMolecule;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class MzTabReader {

    public MZTabFile readMzTab(String mzTabfileName){
        File inputFile = new File(mzTabfileName);
        Metadata metadata = new Metadata();
        MZTabFile mzTabFile = new MZTabFile(metadata);

        try {
            //mzTabFile.

        } catch (Exception e) {
            e.printStackTrace();  //TODO
        }

        return mzTabFile;
    }


    public Collection<SmallMolecule> getSmallMolecules(String mzTabfileName){

        Collection<SmallMolecule> smallMolecules = new ArrayList<SmallMolecule>();
        MZTabFile mzTabFile = readMzTab(mzTabfileName);
        smallMolecules = mzTabFile.getSmallMolecules();
        return smallMolecules;

    }

    public File readMAF(String fileName){
        File file = new File(fileName);
        return file;
    }

}
