package com.example;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fincatto.nfe310.classes.NFAmbiente;
import com.fincatto.nfe310.classes.lote.envio.NFLoteEnvio;
import com.fincatto.nfe310.classes.lote.envio.NFLoteEnvioRetorno;
import com.fincatto.nfe310.classes.lote.envio.NFLoteEnvioRetornoDados;
import com.fincatto.nfe310.classes.nota.NFNotaProcessada;
import com.fincatto.nfe310.parsers.NotaParser;
import com.fincatto.nfe310.utils.NFGeraCadeiaCertificados;
import com.fincatto.nfe310.webservices.WSFacade;

@RestController
@RequestMapping("/demo/")
public class ServletInitializer extends SpringBootServletInitializer {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private NFeConfigTeste config;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DemoApplication.class);
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
	public String nfe() {
		NFLoteEnvio lote = new NFLoteEnvio();
		try {
			final NFLoteEnvioRetornoDados retorno = new WSFacade(config).enviaLote(lote);
			NFLoteEnvioRetorno nFLoteEnvioRetorno = retorno.getRetorno();
			mongoTemplate.save(nFLoteEnvioRetorno);
			retorno.getRetorno().getStatus();
			nFLoteEnvioRetorno.getStatus();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "nota fiscal";
	}
	
	@RequestMapping("cacerts")
	public void cacerts() {
	    try {
	        FileUtils.writeByteArrayToFile(new File("C:\\producao.cacerts"), NFGeraCadeiaCertificados.geraCadeiaCertificados(NFAmbiente.PRODUCAO, "senha"));
	        FileUtils.writeByteArrayToFile(new File("C:\\homologacao.cacerts"), NFGeraCadeiaCertificados.geraCadeiaCertificados(NFAmbiente.HOMOLOGACAO, "senha"));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
