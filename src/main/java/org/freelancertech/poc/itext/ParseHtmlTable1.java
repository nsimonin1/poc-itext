/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.freelancertech.poc.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.freelancertech.poc.itext.utils.MyFileUtils;

/**
 *
 * @author SimonPascal
 */
public class ParseHtmlTable1 {

    private static final String STYLE_FILE_NAME = "css/fiche.css";
    private static final String HTML_FILE_NAME = "index.htm";
    final String LOGO="data:image/png;base64,"+MyFileUtils.generateBase64Image("images/logo.png");

    public static byte[] createPdf() throws DocumentException, IOException {

        try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, os);

            document.open();
            ClassLoader classLoader = ParseHtmlTable1.class.getClassLoader();

            CSSResolver cssResolver = new StyleAttrCSSResolver();
            CssFile cssFile = XMLWorkerHelper.getCSS(classLoader.getResourceAsStream(STYLE_FILE_NAME));
            cssResolver.addCss(cssFile);

            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);

            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

            XMLWorker worker = new XMLWorker(css, true);
            XMLParser p = new XMLParser(worker);
            InputStream is = classLoader.getResourceAsStream(HTML_FILE_NAME);
            p.parse(is);
            document.close();
            return os.toByteArray();
        }
    }

    public static byte[] createPdf(String content) throws DocumentException, IOException {

        try (ByteArrayOutputStream os = new ByteArrayOutputStream();) {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, os);

            document.open();
            ClassLoader classLoader = ParseHtmlTable1.class.getClassLoader();

            CSSResolver cssResolver = new StyleAttrCSSResolver();
            CssFile cssFile = XMLWorkerHelper.getCSS(classLoader.getResourceAsStream(STYLE_FILE_NAME));
            cssResolver.addCss(cssFile);

            HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
            htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);

            HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
            CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

            XMLWorker worker = new XMLWorker(css, true);
            XMLParser p = new XMLParser(worker);
            InputStream is = new ByteArrayInputStream(content.getBytes());
            p.parse(is);
            document.close();
            return os.toByteArray();
        }
    }

    public static byte[] convertHtmlToPdf(String content) throws IOException, DocumentException {
        final ClassLoader classLoader = ParseHtmlTable1.class.getClassLoader();
        try (ByteArrayOutputStream file = new ByteArrayOutputStream();
                InputStream is = new ByteArrayInputStream(content.getBytes());) {
            final Document document = new Document();
            final PdfWriter writer = PdfWriter.getInstance(document, file);
            document.open();
            final TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
            tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
            tagProcessorFactory.addProcessor(new ImageTagProcessor(), HTML.Tag.IMG);
            final CssFile cssFile = XMLWorkerHelper.getCSS(classLoader.getResourceAsStream(STYLE_FILE_NAME));
            final CssFilesImpl cssFiles = new CssFilesImpl(cssFile);
            //cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());
            final StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
            final HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(new XMLWorkerFontProvider()));
            hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(tagProcessorFactory);
            final HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(document, writer));
            final Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
            final XMLWorker worker = new XMLWorker(pipeline, true);
            final Charset charset = Charset.forName("UTF-8");
            final XMLParser xmlParser = new XMLParser(true, worker, charset);

            xmlParser.parse(is, charset);
            document.close();
            return file.toByteArray();
        }
    }
}
