package org.nfe.web.servlet;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.commons.io.FileUtils;
import org.nfe.web.config.NFeConfigEmissor;
import org.nfe.web.db.mongo.entidade.Customer;
import org.nfe.web.db.mongo.repository.CustomerRepository;
import org.nfe.web.main.MainApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincatto.nfe310.classes.NFAmbiente;
import com.fincatto.nfe310.classes.NFModelo;
import com.fincatto.nfe310.classes.lote.envio.NFLoteEnvio;
import com.fincatto.nfe310.classes.lote.envio.NFLoteEnvioRetorno;
import com.fincatto.nfe310.classes.lote.envio.NFLoteEnvioRetornoDados;
import com.fincatto.nfe310.classes.nota.NFNotaProcessada;
import com.fincatto.nfe310.classes.statusservico.consulta.NFStatusServicoConsultaRetorno;
import com.fincatto.nfe310.parsers.NotaParser;
import com.fincatto.nfe310.utils.NFGeraCadeiaCertificados;
import com.fincatto.nfe310.webservices.WSFacade;

@RestController
@RequestMapping("/demo/")
public class ServletInitializer extends SpringBootServletInitializer {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private NFeConfigEmissor config;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MainApplication.class);
	}
	
	@RequestMapping("test1")
	public String test1() {
		NotaParser parser = new NotaParser();
		NFNotaProcessada proc = parser.notaProcessadaParaObjeto(new File("C:\\procNFe52170702709992000156550010040547971932234084.xml"));
		mongoTemplate.save(proc);
		return proc.toString();
	}
	
	@RequestMapping("test")
	public String test() {
		//customerRepository.deleteAll();

		// save a couple of customers
		customerRepository.save(new Customer("Alice", "Smith"));
		customerRepository.save(new Customer("Bob", "Smith"));

		// fetch all customers
		System.out.println("Customers found with findAll():");
		System.out.println("-------------------------------");
		for (Customer customer : customerRepository.findAll()) {
			System.out.println(customer);
		}
		System.out.println();

		// fetch an individual customer
		System.out.println("Customer found with findByFirstName('Alice'):");
		System.out.println("--------------------------------");
		System.out.println(customerRepository.findByFirstName("Alice"));

		System.out.println("Customers found with findByLastName('Smith'):");
		System.out.println("--------------------------------");
		for (Customer customer : customerRepository.findByLastName("Smith")) {
			System.out.println(customer);
		}
		
		return "test " + customerRepository.count();
	}
	
	@RequestMapping("nfe")
	public String nfe() throws Exception {
		NFLoteEnvio lote = new NFLoteEnvio();
		final NFLoteEnvioRetornoDados retorno = new WSFacade(config).enviaLote(lote);
		NFLoteEnvioRetorno nFLoteEnvioRetorno = retorno.getRetorno();
		mongoTemplate.save(nFLoteEnvioRetorno);
		retorno.getRetorno().getStatus();
		nFLoteEnvioRetorno.getStatus();
		return "nota fiscal";
	}
		
	@RequestMapping("status")
	public String status() throws Exception {
		NFStatusServicoConsultaRetorno retorno;
		retorno = new WSFacade(config).consultaStatus(config.getCUF(), NFModelo.NFE);
		return retorno.toString();
	}
	
}
