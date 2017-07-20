package org.nfe.web.controller;

import java.io.File;
import java.util.Date;
import java.util.Map;

import org.nfe.web.config.ConfigNFeEmissao;
import org.nfe.web.db.mongo.entidade.Customer;
import org.nfe.web.db.mongo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.fincatto.nfe310.classes.lote.envio.NFLoteEnvio;
import com.fincatto.nfe310.classes.lote.envio.NFLoteEnvioRetorno;
import com.fincatto.nfe310.classes.lote.envio.NFLoteEnvioRetornoDados;
import com.fincatto.nfe310.classes.nota.NFNotaProcessada;
import com.fincatto.nfe310.parsers.NotaParser;
import com.fincatto.nfe310.webservices.WSFacade;

@Controller
@RequestMapping("/")
public class MainController {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ConfigNFeEmissao config;

	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping("welcome")
	public String welcome(Map<String, Object> model) {
		model.put("time", new Date());
		model.put("message", "Ola mundo");
		return "welcome";
	}

	@RequestMapping("test1")
	public String test1() {
		NotaParser parser = new NotaParser();
		NFNotaProcessada proc = parser
				.notaProcessadaParaObjeto(new File("C:\\procNFe52170702709992000156550010040547971932234084.xml"));
		mongoTemplate.save(proc);
		return "test1";
	}

	@RequestMapping("test")
	public String test() {
		// customerRepository.deleteAll();

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

		return "test";
	}

	@RequestMapping("notafiscal")
	public String nfe() throws Exception {
		NFLoteEnvio lote = new NFLoteEnvio();
		final NFLoteEnvioRetornoDados retorno = new WSFacade(config).enviaLote(lote);
		NFLoteEnvioRetorno nFLoteEnvioRetorno = retorno.getRetorno();
		mongoTemplate.save(nFLoteEnvioRetorno);
		retorno.getRetorno().getStatus();
		nFLoteEnvioRetorno.getStatus();
		return "notafiscal";
	}

	@RequestMapping("status")
	public String status(Map<String, Object> model) throws Exception {
		System.out.println("status");
		//NFStatusServicoConsultaRetorno retorno = new WSFacade(config).consultaStatus(config.getCUF(), NFModelo.NFE);
//		ModelAndView model = new ModelAndView("status");
		model.put("msg", "arg1");
		return "status";
	}
}
