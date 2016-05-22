/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freelancertech.poc.itext;

import com.itextpdf.text.DocumentException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author SimonPascal
 */
public class Main {
    public static void main(String[] args) throws DocumentException, IOException {
        final byte[] flux=ParseHtmlTable1.createPdf();
        Path path=Paths.get("F:", "etat.pdf");
        Files.write(path, flux);
    }
}
