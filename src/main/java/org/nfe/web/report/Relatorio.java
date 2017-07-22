package org.nfe.web.report;

import java.io.File;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.text.html.parser.DocumentParser;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.fincatto.nfe310.NFeConfig;
import com.fincatto.nfe310.classes.nota.NFNotaProcessada;
import com.fincatto.nfe310.parsers.NotaParser;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import net.sf.jasperreports.view.JasperViewer;

public class Relatorio {
	public static void gerarNFe() throws JRException {
		// NotaParser parser = new NotaParser();
		// NFNotaProcessada proc = parser
		// .notaProcessadaParaObjeto(new
		// File("C:\\data\\nfe\\NFe52170737259223000340550010003683811111013070.xml"));
		// StringBufferInputStream sin = new StringBufferInputStream(proc.toString());
		// JasperReport report =
		// JasperCompileManager.compileReport(this.getPathToReportPackage() +
		// "Clientes.jrxml");
		JasperPrint print = JasperFillManager.fillReport("", null, new JRBeanCollectionDataSource(null));
		JasperViewer viewer = new JasperViewer(print, false);
		viewer.setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		File file = new File("C:\\data\\nfe\\NFe52170737259223000340550010003683811111013070.xml");
		
//		Document document = JRXmlUtils.parse(file);
//		Map params = new HashMap();
//		params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
//		params.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
//		params.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
//		params.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
//		params.put(JRParameter.REPORT_LOCALE, Locale.US);
//		try (InputStream in = NFeConfig.class.getResourceAsStream("..\\..\\..\\danfe\\danfeR3.jasper")) {
//			JasperPrint print = JasperFillManager.fillReport(in,params);
//			JasperViewer viewer = new JasperViewer(print, false);
//			viewer.setVisible(true);
//		}
		
		NotaParser parser = new NotaParser();
		NFNotaProcessada proc = parser.notaProcessadaParaObjeto(file);
		ArrayList<NFNotaProcessada> list = new ArrayList<NFNotaProcessada>();
		list.add(proc);
		Map<String, Object> params = new HashMap<String, Object>();
		//params.put("PAGAMENTOS", value);
		params.put("SUBREL", NFeConfig.class.getResourceAsStream("..\\..\\..\\danfe\\DANFE_NFCE_ITENS.jasper"));//java.io.InputStream
		params.put("SUBREL_PAGAMENTOS", NFeConfig.class.getResourceAsStream("..\\..\\..\\danfe\\DANFE_NFCE_PAGAMENTOS.jasper"));//java.io.InputStream
		//params.put("URL_CONSULTA", value);//java.lang.String
		//params.put("QR_CODE", value);//java.awt.Image
		//params.put("CHAVE_ACESSO_FORMATADA", value);//java.lang.String
		//params.put("INFORMACOES_COMPLEMENTARES", value);//java.lang.String
		params.put("MOSTRAR_MSG_FINALIZACAO", true);//java.lang.Boolean
		JRBeanCollectionDataSource bean = new JRBeanCollectionDataSource(list);
		try (InputStream in = NFeConfig.class.getResourceAsStream("..\\..\\..\\danfe\\DANFE_NFCE.jasper")) {
			JasperPrint print = JasperFillManager.fillReport(in, params, bean);
			JasperViewer viewer = new JasperViewer(print, false);
			viewer.setVisible(true);
		}
	}
}